package com.github.almostfamiliar.product.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> builder.failOnUnknownProperties(true).build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(
            new Info()
                .title("Product Service API")
                .version("v0.0.1")
                .description("""
This service manages products in categories and offers the necessary CRUD operationsproducts in categories and offers the necessary CRUD operations.

Currencies get converted against the fixer.io api.
""" ));
  }
}
