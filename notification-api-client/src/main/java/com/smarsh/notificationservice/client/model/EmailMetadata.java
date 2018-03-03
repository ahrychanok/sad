package com.smarsh.notificationservice.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author Dzmitry_Sulauka
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailMetadata {

    @ApiModelProperty(required = true, dataType = "string", notes = "Url for updating notification status", example = "url")
    private String callbackUrl;

    @ApiModelProperty(dataType = "string", notes = "Subject of Notification email", example = "Email subject")
    private String subject;


    @ApiModelProperty(required = true,
                      notes = "Recipients email addresses",
                      dataType = "array",
                      allowableValues = "firstname.lastname@domain.com")
    private Collection<String> recipients;

    @ApiModelProperty(notes = "CC email addresses", dataType = "array", allowableValues = "firstname.lastname@domain.com")
    private Collection<String> cc;

    @NotNull
    @ApiModelProperty(notes = "Template type for getting specific email message template", example = "oauthRequest")
    private TemplateType templateType;

    @ApiModelProperty(notes = "TemplateId for getting specific email message template", example = "oauthRequest")
    private String templateId;

    @ApiModelProperty(dataType = "int",
                      notes = "ClientId for getting specific email message template based on client and templateId",
                      example = "1")
    private Integer clientId;

    @ApiModelProperty(notes = "Context map for filling template or providing message subject and body")
    private LinkedHashMap<String, Object> context;

    @ApiModelProperty(notes = "Email file location")
    private EmailLocation location;

    @ApiModelProperty(notes = "Email from adress")
    private String sender;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Collection<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(Collection<String> recipients) {
        this.recipients = recipients;
    }

    public Collection<String> getCc() {
        return cc;
    }

    public void setCc(Collection<String> cc) {
        this.cc = cc;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public LinkedHashMap<String, Object> getContext() {
        return context;
    }

    public void setContext(LinkedHashMap<String, Object> context) {
        this.context = context;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public EmailLocation getLocation() {
        return location;
    }

    public void setLocation(EmailLocation location) {
        this.location = location;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
