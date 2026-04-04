package com.abtk.product.web.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
//                .servers(Arrays.asList(new Server().url("/api")))// 👈 关键：设置全局服务器路径
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("JWT"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("business-api")
                .pathsToMatch("/**")
                .pathsToExclude("/user/register", "/auth/**", "/api/auth/**", "/api/login")
                .addOpenApiCustomiser(openApi -> openApi.addSecurityItem(
                        new SecurityRequirement().addList("JWT")
                ))
                .build();
    }

    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("auth-api")
                .pathsToMatch("/api/auth/**")
                .pathsToMatch("/api/login")
                .build();
    }
}
