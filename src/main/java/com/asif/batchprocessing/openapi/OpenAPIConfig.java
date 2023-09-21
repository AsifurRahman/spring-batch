package com.asif.batchprocessing.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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

    @Value("${app.openapi.local-url}")
    private String localUrl;


    @Bean
    public OpenAPI myOpenAPI() {

        Server localServer = new Server();
        localServer.setUrl(localUrl);
        localServer.setDescription("Server URL in Local environment");


        Contact contact = new Contact();
//        contact.setEmail("asifur.rahman@technonext.com");
//        contact.setName("TechnoNext Ltd");
//        contact.setUrl("https://www.technonext.com");

        Info info = new Info()
                .title("Spring Batch Service API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}