package com.smarsh.notificationservice.api.configuration.core;


import com.smarsh.notificationservice.api.configuration.properties.mail.smtp.SMTPProperties;
import com.smarsh.notificationservice.api.configuration.properties.mail.smtp.SMTPPropertiesConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author Dzmitry_Sulauka
 */
@Configuration
@EnableConfigurationProperties({SMTPPropertiesConfiguration.class})
public class JavaMailSenderConfiguration {


    @Bean
    public JavaMailSender javaMailSender(SMTPProperties properties) {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(properties.getHost());
        mailSender.setPort(properties.getPort());
        mailSender.setProtocol(properties.getProtocol());
        mailSender.setUsername(properties.getUsername());
        mailSender.setPassword(properties.getPassword());

        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", properties.getAuth());
        javaMailProperties.setProperty("mail.smtp.starttls.enable", properties.getTlsEnabled());
        javaMailProperties.setProperty("mail.smtp.quitwait", properties.getQuitWait());

        javaMailProperties.setProperty("mail.smtp.timeout", properties.getTimeout());
        javaMailProperties.setProperty("mail.smtp.connectiontimeout", properties.getConnectionTimeout());
        javaMailProperties.setProperty("mail.smtp.writetimeout", properties.getWriteTimeout());


        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }


}
