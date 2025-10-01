package com.quasar.fireoperation.api.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GlobalExceptionHandler class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle LocationException and return 404")
    void handleNotFound_LocationException_Returns404() {
        // Given
        LocationException exception = new LocationException("No se pudo determinar la posición.");

        // When
        ResponseEntity<String> response = exceptionHandler.handleNotFound(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se pudo determinar la posición.", response.getBody());
    }

    @Test
    @DisplayName("Should handle MessageException and return 404")
    void handleNotFound_MessageException_Returns404() {
        // Given
        MessageException exception = new MessageException("No se pudo determinar el mensaje.");

        // When
        ResponseEntity<String> response = exceptionHandler.handleNotFound(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se pudo determinar el mensaje.", response.getBody());
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException and return 400")
    void handleBadRequest_IllegalArgumentException_Returns400() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Se requieren distancias de 3 satélites.");

        // When
        ResponseEntity<String> response = exceptionHandler.handleBadRequest(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Se requieren distancias de 3 satélites.", response.getBody());
    }

    @Test
    @DisplayName("Should handle generic Exception and return 500")
    void handleGeneral_GenericException_Returns500() {
        // Given
        Exception exception = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<String> response = exceptionHandler.handleGeneral(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }

    @Test
    @DisplayName("Should handle null exception message gracefully")
    void handleNotFound_NullMessage_HandlesGracefully() {
        // Given
        LocationException exception = new LocationException(null);

        // When & Then
        assertDoesNotThrow(() -> {
            ResponseEntity<String> response = exceptionHandler.handleNotFound(exception);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        });
    }

    @Test
    @DisplayName("Should handle empty exception message")
    void handleNotFound_EmptyMessage_HandlesCorrectly() {
        // Given
        LocationException exception = new LocationException("");

        // When
        ResponseEntity<String> response = exceptionHandler.handleNotFound(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("", response.getBody());
    }

    @Test
    @DisplayName("Should handle very long exception message")
    void handleGeneral_LongMessage_HandlesCorrectly() {
        // Given
        String longMessage = "A".repeat(1000);
        Exception exception = new RuntimeException(longMessage);

        // When
        ResponseEntity<String> response = exceptionHandler.handleGeneral(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }
}
