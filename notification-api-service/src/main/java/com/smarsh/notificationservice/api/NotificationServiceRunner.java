package com.smarsh.notificationservice.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Dzmitry_Sulauka
 */
@EnableSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NotificationServiceRunner {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceRunner.class, args);
    }
}
