package br.com.restwithspringbootandjavaerudio.config;

import br.com.restwithspringbootandjavaerudio.serialization.converter.YamlJackson2HttpMessageConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final MediaType aplicationYml = MediaType.parseMediaType("application/x-yaml");
    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlJackson2HttpMessageConverter());
    }

    @Override
    public void addCorsMappings(@NotNull CorsRegistry registry) {
        var allowedOrigins = corsOriginPatterns.split(","); // pega os valores lidos do resources
        registry.addMapping("/**")
                //.allowedMethods("GET", "POST", "PUT") // indica para quais verbos http o cors  vai funcionar.
                .allowedMethods("*")
                .allowedOrigins(allowedOrigins)// indica que as origins permitidas são as que nos lemos
                .allowCredentials(true); // para permitir autenticação
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("x-yaml", aplicationYml)
        ;
    }
}
