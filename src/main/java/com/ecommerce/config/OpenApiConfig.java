package com.ecommerce.config;
import io.swagger.v3.oas.annotations.*;import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;import io.swagger.v3.oas.annotations.info.*;import io.swagger.v3.oas.annotations.security.SecurityScheme;import org.springframework.context.annotation.Configuration;
@Configuration
@OpenAPIDefinition(info=@Info(title="E-Commerce API",version="1.0.0",description="Full-featured E-Commerce REST API",contact=@Contact(name="Dev Team",email="dev@ecommerce.com")))
@SecurityScheme(name="bearerAuth",type=SecuritySchemeType.HTTP,scheme="bearer",bearerFormat="JWT")
public class OpenApiConfig {}
