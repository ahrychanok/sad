package com.smarsh.blueprint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.smarsh.core.rest.service.deployment.BootWebApplication;

@SpringBootApplication
@ComponentScan(basePackages = { "com.smarsh.core.rest.client", "com.smarsh.blueprint"})
public class BlueprintServiceApp extends BootWebApplication {

    static {
        System.setProperty("server.tls.enabled", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(BlueprintServiceApp.class, args);
    }

}
