package com.smarsh.notificationservice.api;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.smarsh.archiveapi.client.ArchiveApiClientContextConfiguration;
import com.smarsh.archiveapi.client.service.StorageService;
import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.Storage;
import com.smarsh.core.io.location.AwsS3Location;
import com.smarsh.core.rest.client.RestClientContextConfiguration;
import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.core.rest.service.RestServiceContextConfiguration;
import com.smarsh.keyservice.client.KeyserviceClientContextConfiguration;
import com.smarsh.notificationservice.api.configuration.properties.consumer.InternalConsumerPropertiesConfiguration;
import com.smarsh.notificationservice.api.configuration.properties.mail.smtp.SMTPProperties;
import com.smarsh.notificationservice.api.configuration.properties.storage.StorageLocationPropertiesConfig;
import com.smarsh.notificationservice.api.converter.TemplateTypeConverter;
import com.smarsh.notificationservice.api.model.RestCallbackData;
import com.smarsh.notificationservice.api.queue.consumer.emailprocessing.NotificationFinalizingListener;
import com.smarsh.notificationservice.api.queue.consumer.emailprocessing.NotificationProcessorListener;
import com.smarsh.notificationservice.api.queue.consumer.emailprocessing.RetryingListener;
import com.smarsh.notificationservice.api.queue.consumer.restcallback.RestCallbackExecutor;
import com.smarsh.notificationservice.api.queue.consumer.restcallback.RestCallbackSenderService;
import com.smarsh.notificationservice.api.retry.RetryTemplateFactory;
import com.smarsh.notificationservice.api.service.ConstructorService;
import com.smarsh.notificationservice.api.service.FetchResourceService;
import com.smarsh.notificationservice.api.service.QueueSenderService;
import com.smarsh.notificationservice.api.service.TemplateCachingService;
import com.smarsh.notificationservice.api.service.TemplateStoringService;
import com.smarsh.notificationservice.api.service.builder.MessageBuilderAdapter;
import com.smarsh.notificationservice.api.service.builder.adapter.AutoDetectingAdapter;
import com.smarsh.notificationservice.api.service.builder.adapter.FileMessageBuilderAdapter;
import com.smarsh.notificationservice.api.service.builder.adapter.TemplateMessageBuilderAdapter;
import com.smarsh.notificationservice.api.service.builder.adapter.TextMessageBuilderAdapter;
import com.smarsh.notificationservice.api.service.filetree.FileTreeLoaderAdapter;
import com.smarsh.notificationservice.api.service.filetree.TemplatesMapBuilderService;
import com.smarsh.notificationservice.api.service.filetree.impl.AwsS3TreeLoaderAdapter;
import com.smarsh.notificationservice.api.service.filetree.impl.FileSystemTreeLoaderAdapter;
import com.smarsh.notificationservice.api.service.filetree.impl.TemplatesMapBuilderServiceImpl;
import com.smarsh.notificationservice.api.service.impl.FetchResourceServiceImpl;
import com.smarsh.notificationservice.api.service.impl.MimeMessageConstructorServiceImpl;
import com.smarsh.notificationservice.api.service.impl.QueueSenderServiceImpl;
import com.smarsh.notificationservice.api.service.impl.TemplateCachingServiceImpl;
import com.smarsh.notificationservice.api.service.impl.TemplateServiceImpl;
import com.smarsh.notificationservice.api.service.impl.TemplateStoringServiceImpl;
import com.smarsh.notificationservice.client.model.AdapterType;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.notificationservice.client.service.TemplateService;
import com.smarsh.services.messagequeueing.QueueEndpoint;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collection;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * @author Dzmitry_Sulauka
 */
@Configuration
@PropertySource({ "classpath:notification.service.application.properties", "classpath:notification.service.smtp.properties"})
@EnableConfigurationProperties({ StorageLocationPropertiesConfig.class})
@ComponentScan(basePackageClasses = { NotificationServiceContextConfiguration.class})
public class NotificationServiceContextConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public Docket notificationServiceDocket() {

        TypeResolver typeResolver = new TypeResolver();

        return new Docket(DocumentationType.SWAGGER_2).groupName("notificationService")
            .useDefaultResponseMessages(false)
            .apiInfo(new ApiInfoBuilder().title("Notification Service")
                .version("1.0")
                .build())
            .alternateTypeRules(newRule(typeResolver.resolve(Collection.class, WildcardType.class), typeResolver.resolve(
                List.class, WildcardType.class)))
            .select()
            .paths(Predicates.not(PathSelectors.regex("/error")))
            .build();
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        engine.setTemplateResolver(templateResolver);
        return engine;
    }

    @Bean
    public MessageCallback<EmailMetadata> notificationProcessorListener(
            MessageBuilderAdapter messageBuilderAdapter,
            RestCallbackSenderService restCallbackSenderService,
            JavaMailSender javaMailSender,
            SMTPProperties properties,
            InternalConsumerPropertiesConfiguration configuration) {

        RetryTemplate flowRetryTemplate =
                RetryTemplateFactory.createRetryTemplate(configuration.getRetryCount(),
                        configuration.getRetryTimeout());

        return new NotificationFinalizingListener(restCallbackSenderService, new RetryingListener(
                new NotificationProcessorListener(messageBuilderAdapter, javaMailSender, properties), flowRetryTemplate));
    }

    @Bean
    public MessageCallback<RestCallbackData> restCallbackExecutor(RestClientOperations restClientOperations) {

        return new RestCallbackExecutor(restClientOperations);
    }

    @Bean
    public QueueSenderService queueSenderService(QueueEndpoint<EmailMetadata> queueProcessingEndpoint) {
        return new QueueSenderServiceImpl(queueProcessingEndpoint);
    }

    @Bean
    public FetchResourceService fetchTemplateService(Storage storage, Location s3Location) {
        return new FetchResourceServiceImpl(storage, s3Location);
    }

    @Bean
    public MessageBuilderAdapter templateBuilderService(
        TemplateEngine templateEngine,
        TemplateCachingService cachingService,
        ConstructorService constructorService,
        StorageService storageService,
        Storage storage,
        SMTPProperties properties) {

        AutoDetectingAdapter autoDetectingAdapter = new AutoDetectingAdapter();
        autoDetectingAdapter.register(AdapterType.FILE, new FileMessageBuilderAdapter(storageService, storage,
            constructorService));
        autoDetectingAdapter.register(AdapterType.TEXT, new TextMessageBuilderAdapter(constructorService));
        autoDetectingAdapter.register(AdapterType.TEMPLATE, new TemplateMessageBuilderAdapter(templateEngine, cachingService,
                constructorService, properties.getDefaultSubject()));

        return autoDetectingAdapter;
    }

    @Bean
    public ConstructorService emailSenderService(JavaMailSender javaMailSender) {
        return new MimeMessageConstructorServiceImpl(javaMailSender);
    }

    @Bean
    public RestCallbackSenderService restCallbackSenderService(QueueEndpoint<RestCallbackData> restCallbackQueueEndpoint) {
        return new RestCallbackSenderService(restCallbackQueueEndpoint);
    }

    @Bean
    public TemplateService templateUploadingService(
        TemplateStoringService storingService,
        TemplateCachingService cachingService) {
        return new TemplateServiceImpl(storingService, cachingService);
    }

    @Bean
    public TemplateStoringService xmlTransformationService(
        FetchResourceService fetchResourceService,
        Storage storage,
        Location location) {
        return new TemplateStoringServiceImpl(fetchResourceService, storage, location);
    }

    @Bean
    public TemplatesMapBuilderService templatesMapBuilderService(
        Location location,
        TemplateStoringService transformationService) {

        FileTreeLoaderAdapter adapter;
        if (location instanceof AwsS3Location) {
            adapter = new AwsS3TreeLoaderAdapter(location);
        } else {
            adapter = new FileSystemTreeLoaderAdapter(location);
        }

        return new TemplatesMapBuilderServiceImpl(adapter, transformationService);
    }

    @Bean
    public TemplateCachingService cachingService(TemplatesMapBuilderService templatesMapBuilderService) {
        return new TemplateCachingServiceImpl(templatesMapBuilderService);
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new TemplateTypeConverter());
    }

    /**
     * Dependent configurations.
     */
    @Configuration
    @Import({
        RestClientContextConfiguration.class,
        RestServiceContextConfiguration.class,
        KeyserviceClientContextConfiguration.class,
        ArchiveApiClientContextConfiguration.class})
    static class DependentConfigurations {
    }

}
