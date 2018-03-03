package com.smarsh.notificationservice.api.configuration.properties.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Dzmitry_Sulauka
 */
@ConfigurationProperties(prefix = "notification.service.templates.s3")
public class StorageLocationPropertiesConfig implements StorageLocationProperties {

    private String uri;
    private String bucket;
    private String value;
    private String secret;

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
