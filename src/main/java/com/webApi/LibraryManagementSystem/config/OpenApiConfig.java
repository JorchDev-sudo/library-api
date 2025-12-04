
package com.webApi.LibraryManagementSystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "LibraryManagementSystem API",
                version = "1.0.0",
                description = "API RESTful for the loan, books and authors management.",
                contact = @Contact(
                        name = "Jorge Luis Cotera Lopez",
                        email = "jorgecoteralopez@gmail.com"
                )
        )
)

@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class OpenApiConfig {
}
