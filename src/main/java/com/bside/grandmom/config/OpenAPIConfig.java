package com.bside.grandmom.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        Server prodHttpsServer = new Server();
        prodHttpsServer.setDescription("prod Https Server");
        prodHttpsServer.setUrl("https://223.130.131.239.nip.io/grandmom");

        Server devHttpsServer = new Server();
        devHttpsServer.setDescription("dev Http Server");
        devHttpsServer.setUrl("http://localhost:8080/grandmom");


        SecurityScheme uid = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("uid");

        SecurityScheme did = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("did");

        OpenAPI openAPI = new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("uid", uid)
                        .addSecuritySchemes("did", did)
                )
                .addSecurityItem(new SecurityRequirement().addList("uid").addList("did"))
                .info(apiInfo());


        openAPI.setServers(Arrays.asList(prodHttpsServer, devHttpsServer));
        return openAPI;
    }

    private Info apiInfo() {
        return new Info()
                .title("GrandMom's Day")
                .description("할머니의 하루 Backend API Swagger UI")
                .version("0.0.1");
    }
}
