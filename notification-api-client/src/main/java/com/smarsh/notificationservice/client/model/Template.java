package com.smarsh.notificationservice.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author Dzmitry_Sulauka
 */
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Template {

    @ApiParam(value = "Guid identifier", hidden = true)
    private String id;

    @NotNull
    @ApiParam(value = "Template type", required = true)
    private TemplateType type;

    @NotNull
    @ApiParam(value = "Template name", required = false)
    private String name;

    @ApiParam(value = "Email subject to display in message", required = true)
    private String subject;

    @ApiParam(value = "Template description", required = false)
    private String description;

    @ApiParam(value = "clientId", required = false)
    private Integer clientId;

    @NotNull
    @ApiParam(value = "Template engine type", required = true)
    private TemplateEngine engine;

    @ApiParam(value = "Flag that indicates is template status.", required = true)
    private boolean isDefault;

    public Template() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public TemplateEngine getEngine() {
        return engine;
    }

    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public TemplateType getType() {
        return type;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
