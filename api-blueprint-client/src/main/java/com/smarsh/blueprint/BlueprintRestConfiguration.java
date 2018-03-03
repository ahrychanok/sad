package com.smarsh.blueprint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smarsh.core.rest.client.uri.factory.DefaultUriFactory;
import com.smarsh.core.rest.client.uri.factory.UriFactory;

@Configuration
public class BlueprintRestConfiguration {
    
    @Bean
    public UriFactory blueprintUriFactory() {
        return new DefaultUriFactory("aaa");
    }

}
