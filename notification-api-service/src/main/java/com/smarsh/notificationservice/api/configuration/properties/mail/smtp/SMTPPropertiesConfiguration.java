package com.smarsh.notificationservice.api.configuration.properties.mail.smtp;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Dzmitry_Sulauka
 */
@ConfigurationProperties(prefix = "notification.service.mail.server")
public class SMTPPropertiesConfiguration implements SMTPProperties {

    private String host;

    private int port;

    private String protocol;

    private String username;

    private String password;

    private String auth;

    private String tlsEnabled;

    private String quitWait;

    private String defaultSender;

    private String defaultSubject;

    private String connectionTimeout;

    private String timeout;

    private String writeTimeout;

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTlsEnabled() {
        return tlsEnabled;
    }

    public void setTlsEnabled(String tlsEnabled) {
        this.tlsEnabled = tlsEnabled;
    }

    public String getQuitWait() {
        return quitWait;
    }

    public void setQuitWait(String quitWait) {
        this.quitWait = quitWait;
    }

    @Override
    public String getDefaultSender() {
        return defaultSender;
    }

    public void setDefaultSender(String defaultSender) {
        this.defaultSender = defaultSender;
    }

    @Override
    public String getDefaultSubject() {
        return defaultSubject;
    }

    public void setDefaultSubject(String defaultSubject) {
        this.defaultSubject = defaultSubject;
    }

    @Override
    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(String connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    @Override
    public String getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(String writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
}
