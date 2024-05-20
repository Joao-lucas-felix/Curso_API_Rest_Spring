package br.com.restwithspringbootandjavaerudio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean //diz para o spring que esse é um objeto a ser gerenciado por um spring
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Course rest API with SpringBoot")
                        .description("This API was made by a student, for a udemy course")
                        .termsOfService("")
                        .license(new License().name("MIT License "))
                );
    }

}
