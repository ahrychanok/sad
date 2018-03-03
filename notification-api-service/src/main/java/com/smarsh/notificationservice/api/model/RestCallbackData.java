package com.smarsh.notificationservice.api.model;

import com.smarsh.notificationservice.client.model.NotificationStatus;
import org.springframework.http.HttpMethod;

/**
 * @author Dzmitry_Sulauka
 */
public class RestCallbackData {

    private String url;
    private NotificationStatus status;
    private HttpMethod method;

    public RestCallbackData() {
    }

    public RestCallbackData(String url, NotificationStatus status, HttpMethod method) {
        this.url = url;
        this.status = status;
        this.method = method;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }
}
