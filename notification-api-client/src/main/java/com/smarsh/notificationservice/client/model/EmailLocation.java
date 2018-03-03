package com.smarsh.notificationservice.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author Dzmitry_Sulauka
 */
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailLocation {

    @ApiModelProperty(required = true, dataType = "string", notes = "Device id where eml file stored", example = "deviceId")
    private String deviceId;

    @ApiModelProperty(required = true, dataType = "string", notes = "Key for decrypt file", example = "path")
    private String key;

    @ApiModelProperty(required = true, dataType = "string", notes = "Path", example = "path")
    private String path;

    public EmailLocation() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
