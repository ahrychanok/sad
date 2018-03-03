package com.smarsh.notificationservice.client;

import com.smarsh.core.rest.client.RestClientContextConfiguration;
import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.core.rest.client.template.RestClientStreamOperations;
import com.smarsh.core.rest.client.uri.factory.DefaultUriFactory;
import com.smarsh.core.rest.client.uri.factory.UriFactory;
import com.smarsh.notificationservice.client.configuration.NotificationServiceClientProperties;
import com.smarsh.notificationservice.client.configuration.NotificationServiceClientPropertiesConfiguration;
import com.smarsh.notificationservice.client.service.NotificationService;
import com.smarsh.notificationservice.client.service.RestNotificationService;
import com.smarsh.notificationservice.client.service.RestTemplateService;
import com.smarsh.notificationservice.client.service.TemplateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Dzmitry_Sulauka
 */
@Configuration
@EnableConfigurationProperties({ NotificationServiceClientPropertiesConfiguration.class})
@PropertySource("classpath:notification.service.client.application.properties")
public class NotificationServiceClientContextConfiguration {

    @Bean
    public UriFactory notificationServiceUriFactory(NotificationServiceClientProperties properties) throws MalformedURLException {
        URL baseUrl = new URL(properties.getUrl());
        return new DefaultUriFactory(baseUrl.toString());
    }

    @Bean
    public NotificationService notificationService(
        RestClientOperations clientOperations,
        @Qualifier("notificationServiceUriFactory") UriFactory uriFactory) {
        return new RestNotificationService(clientOperations, uriFactory);

    }

    @Bean
    public TemplateService templateService(
        RestClientOperations clientOperations,
        RestTemplate restTemplate,
        @Qualifier("notificationServiceUriFactory") UriFactory uriFactory,
        RestClientStreamOperations restClientStreamOperations) {
        return new RestTemplateService(clientOperations, uriFactory, restTemplate, restClientStreamOperations);

    }

    /**
     * @author Dzmitry_Sulauka
     */
    @Configuration
    @Import({ RestClientContextConfiguration.class})
    static class DependentConfigurations {
    }

}
