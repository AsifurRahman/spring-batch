package com.technonext.rmsbackend.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    @Value("${rms.openapi.local-url}")
    private String localUrl;

    @Value("${rms.openapi.dev-url}")
    private String devUrl;

    @Value("${rms.openapi.prod-url}")
    private String prodUrl;


    @Bean
    public OpenAPI myOpenAPI() {

        Server localServer = new Server();
        localServer.setUrl(localUrl);
        localServer.setDescription("Server URL in Local environment");

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
//        contact.setEmail("asifur.rahman@technonext.com");
//        contact.setName("TechnoNext Ltd");
//        contact.setUrl("https://www.technonext.com");

        Info info = new Info()
                .title("RMS Service API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints");

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(info)
                .servers(List.of(localServer, devServer, prodServer));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}