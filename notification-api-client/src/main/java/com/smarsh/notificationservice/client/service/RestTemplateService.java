package com.smarsh.notificationservice.client.service;

import com.smarsh.core.rest.client.dto.ResponseDto;
import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.core.rest.client.template.RestClientStreamOperations;
import com.smarsh.core.rest.client.uri.factory.UriFactory;
import com.smarsh.notificationservice.client.model.Template;
import com.smarsh.notificationservice.client.model.TemplateIdResponse;
import com.smarsh.notificationservice.client.model.TemplateType;
import com.smarsh.notificationservice.client.model.exception.RestTemplateServiceException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dzmitry_Sulauka
 */
public class RestTemplateService implements TemplateService {

    private static final String URL = "/notifications/templates";
    private static final String CLIENT_ID_PARAM_NAME = "clientId";
    private final RestClientOperations clientOperations;
    private final UriFactory uriFactory;
    private final RestTemplate restTemplate;
    private final RestClientStreamOperations restClientStreamOperations;

    public RestTemplateService(
        RestClientOperations clientOperations,
        UriFactory uriFactory,
        RestTemplate restTemplate,
        RestClientStreamOperations restClientStreamOperations) {
        this.clientOperations = clientOperations;
        this.uriFactory = uriFactory;
        this.restTemplate = restTemplate;
        this.restClientStreamOperations = restClientStreamOperations;
    }

    @Override
    public TemplateIdResponse uploadTemplate(Template template, MultipartFile multipartFile) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        try {
            map.add("file", new ByteArrayResource(multipartFile.getBytes()) {
                @Override
                public String getFilename() {
                    return "1.html";
                }
            });
        } catch (IOException e) {
            throw new RestTemplateServiceException(e);
        }
        map.add("template", template);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<TemplateIdResponseDto> exchange =
            restTemplate.exchange(uriFactory.createUri(URL.concat("/upload"))
                .toString(), HttpMethod.POST, request, TemplateIdResponseDto.class);

        return exchange.getBody()
            .getResult();
    }

    @Override
    public void updateTemplate(TemplateType type, String id, Template template, MultipartFile file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Template> getTemplateMetadata(Integer clientId, TemplateType type) {

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        Optional.ofNullable(clientId)
            .ifPresent(o -> requestParam.add(CLIENT_ID_PARAM_NAME, o.toString()));
        Map<String, Object> pathVariable = new HashMap<>();
        pathVariable.put("type", type.toValue());

        return clientOperations.get(uriFactory.createUri(URL.concat("/types/{type}"), pathVariable, requestParam),
            TemplatesResponseDto.class);
    }

    @Override
    public InputStream getTemplate(String templateId, Integer clientId, TemplateType type) {

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        Optional.ofNullable(clientId)
            .ifPresent(o -> requestParam.add(CLIENT_ID_PARAM_NAME, o.toString()));
        Map<String, Object> pathVariable = new HashMap<>();
        pathVariable.put("id", templateId);
        pathVariable.put("type", type.toValue());

        AtomicReference<InputStream> inputStream = new AtomicReference<>();
        try {
            RequestCallback callback = req -> {
                List<MediaType> accept = Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON);
                req.getHeaders()
                    .setAccept(accept);
            };
            ResponseExtractor<InputStream> objectResponseExtractor = ex -> {
                InputStream body = ex.getBody();
                inputStream.set(new ByteArrayInputStream(IOUtils.toByteArray(body)));
                return null;
            };
            restClientStreamOperations.exchangeWithExtractorOnStream(uriFactory.createUri(URL.concat("/types/{type}/{id}"),
                pathVariable, requestParam), HttpMethod.GET, callback, objectResponseExtractor);
        } catch (Exception e) {
            throw new RestTemplateServiceException("Error while reading response", e);
        }
        return inputStream.get();
    }

    @Override
    public void deleteTemplate(String templateId, Integer clientId, TemplateType type) {

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        Optional.ofNullable(clientId)
            .ifPresent(o -> requestParams.add(CLIENT_ID_PARAM_NAME, o.toString()));
        Map<String, Object> pathVariable = new HashMap<>();
        pathVariable.put("type", type.toValue());
        pathVariable.put("id", templateId);
        clientOperations.delete(uriFactory.createUri(URL.concat("/types/{type}/{id}"), pathVariable, requestParams));
    }

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException();
    }

    /**
     * @author Dzmitry_Sulauka
     */
    protected static class TemplatesResponseDto extends ResponseDto<Collection<Template>> {
    }

    /**
     * @author Dzmitry_Sulauka
     */
    protected static class TemplateIdResponseDto extends ResponseDto<TemplateIdResponse> {
    }

}
