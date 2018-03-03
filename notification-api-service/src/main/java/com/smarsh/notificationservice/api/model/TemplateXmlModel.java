package com.smarsh.notificationservice.api.model;

import com.smarsh.notificationservice.api.model.adapters.xml.CDATAAdapter;
import com.smarsh.notificationservice.api.model.adapters.xml.TypeAdapter;
import com.smarsh.notificationservice.client.model.TemplateEngine;
import com.smarsh.notificationservice.client.model.TemplateType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Dzmitry_Sulauka
 */
@XmlRootElement(name = "notification-template")
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateXmlModel {

    @XmlElement
    private String id;

    @XmlJavaTypeAdapter(TypeAdapter.class)
    private TemplateType type;

    @XmlElement(name = "client-id")
    private Integer clientId;

    @XmlElement
    private String name;

    @XmlElement
    private String subject;

    @XmlElement
    private String description;

    @XmlElement
    private TemplateEngine engine;

    @XmlJavaTypeAdapter(CDATAAdapter.class)
    private String template;

    @XmlElement
    private boolean isDefault;


    public TemplateXmlModel() {
        //Empty constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TemplateType getType() {
        return type;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public TemplateEngine getEngine() {
        return engine;
    }

    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
