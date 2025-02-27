package com.maimai.billingcalculationengine.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "title name",
                description = "base description",
                version = "1.0.0",
                contact = @Contact(
                        name = "Mai Zhang",
                        email = "yulezh@proton.me",
                        url = "https://linkedin.com/in/mai-zhang67/"
                )
        ))
public class SwaggerOpenApiConfig {
}
