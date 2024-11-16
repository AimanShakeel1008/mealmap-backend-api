package com.mealmap.mealmap_backend_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("adminBearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Access token for admin role"))
                        .addSecuritySchemes("customerBearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Access token for Customer role"))
                        .addSecuritySchemes("restaurantOwnerBearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Access token for Restaurant Owner role"))
                        .addSecuritySchemes("deliveryPersonnelBearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Access token for Delivery Personnel role"))
                )
                .addSecurityItem(new SecurityRequirement().addList("customerBearerAuth"))
                .addSecurityItem(new SecurityRequirement().addList("adminBearerAuth"))
                .addSecurityItem(new SecurityRequirement().addList("restaurantOwnerBearerAuth"))
                .addSecurityItem(new SecurityRequirement().addList("deliveryPersonnelBearerAuth"));
    }
}


