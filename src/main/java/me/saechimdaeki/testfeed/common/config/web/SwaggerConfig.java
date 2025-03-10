package me.saechimdaeki.testfeed.common.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TestFeed API")
                        .version("1.0")
                        .description("TestFeed 프로젝트의 API 명세서입니다."));
    }
}