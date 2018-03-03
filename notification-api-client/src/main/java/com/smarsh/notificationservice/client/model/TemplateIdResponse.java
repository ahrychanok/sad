package com.smarsh.notificationservice.client.model;

import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateIdResponse {

    @ApiParam(value = "Guid identifier", hidden = true)
    private String id;

    public TemplateIdResponse() {
    }

    public TemplateIdResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
