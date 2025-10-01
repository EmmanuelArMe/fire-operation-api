package com.quasar.fireoperation.api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LocationException class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("LocationException Tests")
class LocationExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void constructor_WithMessage_CreatesException() {
        // Given
        String message = "No se pudo determinar la posición.";

        // When
        LocationException exception = new LocationException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with null message")
    void constructor_WithNullMessage_CreatesException() {
        // When
        LocationException exception = new LocationException(null);

        // Then
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with empty message")
    void constructor_WithEmptyMessage_CreatesException() {
        // Given
        String message = "";

        // When
        LocationException exception = new LocationException(message);

        // Then
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Should be instance of RuntimeException")
    void exception_IsRuntimeException() {
        // When
        LocationException exception = new LocationException("Test message");

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }
}
