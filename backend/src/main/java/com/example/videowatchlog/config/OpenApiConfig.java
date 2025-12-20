package com.example.videowatchlog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("視聴予定リスト API")
                        .version("1.0.0")
                        .description("ドラマ・アニメ・映画を「視聴予定」として登録・管理するための REST API")
                        .contact(new Contact().name("video-watch-list Team")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api/v1")
                                .description("Local development server")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/titles/**", "/series/**", "/episodes/**", "/viewing-records/**")
                .build();
    }
}
