package com.smarsh.core.rest.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 
 */
@SpringBootApplication
public class MultiversionApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MultiversionApplication.class).profiles("apiVersioning")
                                                                   .run(args);
    }

}
