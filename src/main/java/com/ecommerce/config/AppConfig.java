package com.ecommerce.config;
import org.springframework.beans.factory.annotation.Value;import org.springframework.context.annotation.*;import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Value("${app.cors.allowed-origins}") private String allowedOrigins;
    @Override public void addCorsMappings(CorsRegistry r){r.addMapping("/**").allowedOrigins(allowedOrigins).allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS").allowedHeaders("*").allowCredentials(true).maxAge(3600);}
}
