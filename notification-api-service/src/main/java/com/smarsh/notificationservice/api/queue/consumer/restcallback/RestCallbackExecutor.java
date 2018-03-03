package com.smarsh.notificationservice.api.queue.consumer.restcallback;

import com.smarsh.core.rest.client.constants.AuditHeaderNames;
import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.core.rest.client.template.RestClientTemplate;
import com.smarsh.core.rest.client.uri.factory.DefaultUriFactory;
import com.smarsh.core.rest.client.uri.factory.UriFactory;
import com.smarsh.notificationservice.api.model.RestCallbackData;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

/**
 * @author Dzmitry_Sulauka
 */
public class RestCallbackExecutor implements MessageCallback<RestCallbackData> {

    private final RestClientOperations template;


    public RestCallbackExecutor(RestClientOperations template) {
        this.template = template;
    }

    @Override
    public void onMessage(RestCallbackData data) {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("status", data.getStatus().toValue());
        UriFactory uriFactory = new DefaultUriFactory(data.getUrl());
        URI uri = uriFactory.createUri("", requestParams);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuditHeaderNames.USER_ID, "1"); //TODO change header value
        template.exchange(
                uri,
                data.getMethod(),
                requestParams,
                headers,
                RestClientTemplate.VoidResponseDto.class
        );
    }
}
