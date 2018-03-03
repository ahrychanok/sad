package com.smarsh.notificationservice.api.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Collection;

/**
 * @author Dzmitry_Sulauka
 */
public class EmailMessage {

    private Collection<String> to;
    private Collection<String> cc;
    private String subject;
    private String body;

    public EmailMessage() {
    }

    public EmailMessage(Collection<String> to, Collection<String> cc, String subject, String body) {
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
    }

    public Collection<String> getTo() {
        return to;
    }

    public void setTo(Collection<String> to) {
        this.to = to;
    }

    public Collection<String> getCc() {
        return cc;
    }

    public void setCc(Collection<String> cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
