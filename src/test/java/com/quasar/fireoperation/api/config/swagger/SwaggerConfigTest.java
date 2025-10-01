package com.quasar.fireoperation.api.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SwaggerConfig class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("SwaggerConfig Tests")
class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;

    @BeforeEach
    void setUp() {
        swaggerConfig = new SwaggerConfig();
    }

    @Test
    @DisplayName("Should create OpenAPI configuration")
    void customOpenAPI_CreatesValidConfiguration() {
        // When
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        // Then
        assertNotNull(openAPI);
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getInfo());
    }

    @Test
    @DisplayName("Should have correct API title")
    void customOpenAPI_HasCorrectTitle() {
        // When
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // Then
        assertEquals("Quasar Fire Operation API", info.getTitle());
    }

    @Test
    @DisplayName("Should have correct API version")
    void customOpenAPI_HasCorrectVersion() {
        // When
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // Then
        assertEquals("1.0.0", info.getVersion());
    }

    @Test
    @DisplayName("Should have API description")
    void customOpenAPI_HasDescription() {
        // When
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // Then
        assertNotNull(info.getDescription());
        assertTrue(info.getDescription().contains("Quasar Fire Operation API"));
        assertTrue(info.getDescription().contains("trilateration"));
        assertTrue(info.getDescription().contains("Star Wars"));
    }

    @Test
    @DisplayName("Should have contact information")
    void customOpenAPI_HasContactInfo() {
        // When
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();
        Contact contact = info.getContact();

        // Then
        assertNotNull(contact);
        assertEquals("Emmanuel Arenilla", contact.getName());
        assertEquals("emmanueldevtest01@gmail.com", contact.getEmail());
        assertEquals("https://github.com/EmmanuelArMe/fire-operation-api", contact.getUrl());
    }

    @Test
    @DisplayName("Should contain key features in description")
    void customOpenAPI_DescriptionContainsKeyFeatures() {
        // When
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        String description = openAPI.getInfo().getDescription();

        // Then
        assertTrue(description.contains("trilateration algorithms"));
        assertTrue(description.contains("Star Wars-inspired"));
        assertTrue(description.contains("space rescue mission"));
        assertTrue(description.contains("imperial spacecraft"));
    }

    @Test
    @DisplayName("Should contain mission objectives in description")
    void customOpenAPI_DescriptionContainsMissionObjectives() {
        // When
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        String description = openAPI.getInfo().getDescription();

        // Then
        assertTrue(description.contains("determine the exact location"));
        assertTrue(description.contains("reconstruct intercepted messages"));
        assertTrue(description.contains("rebel satellites"));
        assertTrue(description.contains("in distress"));
    }

    @Test
    @DisplayName("Should have Star Wars themed description")
    void customOpenAPI_HasStarWarsTheme() {
        // When
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        String description = openAPI.getInfo().getDescription();

        // Then
        assertTrue(description.contains("Star Wars"));
        assertTrue(description.contains("imperial spacecraft"));
        assertTrue(description.contains("rebel satellites"));
        assertTrue(description.contains("space rescue mission"));
    }
}
