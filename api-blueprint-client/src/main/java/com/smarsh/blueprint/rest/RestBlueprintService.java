package com.smarsh.blueprint.rest;

import com.smarsh.blueprint.BlueprintService;
import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.core.rest.client.uri.factory.UriFactory;

public class RestBlueprintService implements BlueprintService {

    private final RestClientOperations clientOperations;

    private final UriFactory uriFactory;

    public RestBlueprintService(RestClientOperations clientOperations, UriFactory uriFactory) {
        super();
        this.clientOperations = clientOperations;
        this.uriFactory = uriFactory;
    }

    @Override
    public String getHello(String world) {
        return clientOperations.get(uriFactory.createUri("/greetings"), StringResponse.class);
    }
    
   

}
