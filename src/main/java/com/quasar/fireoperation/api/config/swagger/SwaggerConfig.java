package com.quasar.fireoperation.api.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Swagger API documentation for Quasar Fire Operation API.
 *
 * <p>
 * This class provides the necessary configuration to generate and expose the API documentation
 * using Swagger/OpenAPI 3. It configures the OpenAPI object with essential metadata, including
 * the title, version, description, contact information, and servers for the Star Wars-inspired
 * rescue mission API that implements trilateration algorithms for spacecraft location and
 * message reconstruction.
 * </p>
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures the OpenAPI object for the Swagger documentation of Quasar Fire Operation API.
     *
     * <p>
     * This method initializes the OpenAPI object with comprehensive metadata for the space rescue
     * API, including title, version, description, contact information, license, and server
     * configuration. The API documentation describes endpoints for determining spacecraft location
     * using trilateration algorithms and reconstructing intercepted distress messages from
     * rebel satellites.
     * </p>
     *
     * @return an {@link OpenAPI} object containing the complete configuration for the Swagger API documentation
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Quasar Fire Operation API")
                        .version("1.0.0")
                        .description("""
                                Quasar Fire Operation API - A Star Wars-inspired space rescue mission API
                               \s
                                This API implements advanced trilateration algorithms to determine the exact location\s
                                of imperial spacecraft in distress and reconstruct intercepted messages from rebel satellites.
                              \s""")
                        .contact(new Contact()
                                .name("Emmanuel Arenilla")
                                .email("emmanueldevtest01@gmail.com")
                                .url("https://github.com/EmmanuelArMe/fire-operation-api")));
    }
}
