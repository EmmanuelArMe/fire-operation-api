package com.quasar.fireoperation.api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MessageException class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("MessageException Tests")
class MessageExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void constructor_WithMessage_CreatesException() {
        // Given
        String message = "No se pudo determinar el mensaje.";

        // When
        MessageException exception = new MessageException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with null message")
    void constructor_WithNullMessage_CreatesException() {
        // When
        MessageException exception = new MessageException(null);

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
        MessageException exception = new MessageException(message);

        // Then
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Should be instance of RuntimeException")
    void exception_IsRuntimeException() {
        // When
        MessageException exception = new MessageException("Test message");

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }
}
