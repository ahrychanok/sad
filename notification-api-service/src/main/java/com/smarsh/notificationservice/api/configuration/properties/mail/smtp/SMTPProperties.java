package com.smarsh.notificationservice.api.configuration.properties.mail.smtp;

/**
 * @author Dzmitry_Sulauka
 */
public interface SMTPProperties {

    String getHost();

    int getPort();

    String getProtocol();

    String getUsername();

    String getPassword();

    String getAuth();

    String getTlsEnabled();

    String getQuitWait();

    String getDefaultSender();

    String getDefaultSubject();

    String getConnectionTimeout();

    String getTimeout();

    String getWriteTimeout();

}
