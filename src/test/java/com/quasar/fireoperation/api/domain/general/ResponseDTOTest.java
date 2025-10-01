package com.quasar.fireoperation.api.domain.general;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ResponseDTO class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("ResponseDTO Tests")
class ResponseDTOTest {

    @Test
    @DisplayName("Should create response with position and message")
    void constructor_WithPositionAndMessage_CreatesResponse() {
        // Given
        PositionDTO position = new PositionDTO(-58.31f, -69.55f);
        String message = "este es un mensaje secreto";

        // When
        ResponseDTO response = new ResponseDTO(position, message);

        // Then
        assertEquals(position, response.getPosition());
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("Should handle null position")
    void constructor_NullPosition_HandlesCorrectly() {
        // Given
        String message = "test message";

        // When
        ResponseDTO response = new ResponseDTO(null, message);

        // Then
        assertNull(response.getPosition());
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("Should handle null message")
    void constructor_NullMessage_HandlesCorrectly() {
        // Given
        PositionDTO position = new PositionDTO(0f, 0f);

        // When
        ResponseDTO response = new ResponseDTO(position, null);

        // Then
        assertEquals(position, response.getPosition());
        assertNull(response.getMessage());
    }

    @Test
    @DisplayName("Should handle empty message")
    void constructor_EmptyMessage_HandlesCorrectly() {
        // Given
        PositionDTO position = new PositionDTO(100f, 200f);
        String message = "";

        // When
        ResponseDTO response = new ResponseDTO(position, message);

        // Then
        assertEquals(position, response.getPosition());
        assertEquals("", response.getMessage());
    }

    @Test
    @DisplayName("Should handle very long message")
    void constructor_LongMessage_HandlesCorrectly() {
        // Given
        PositionDTO position = new PositionDTO(0f, 0f);
        String longMessage = "A".repeat(1000);

        // When
        ResponseDTO response = new ResponseDTO(position, longMessage);

        // Then
        assertEquals(position, response.getPosition());
        assertEquals(longMessage, response.getMessage());
    }

    @Test
    @DisplayName("Should handle special characters in message")
    void constructor_SpecialCharacters_HandlesCorrectly() {
        // Given
        PositionDTO position = new PositionDTO(0f, 0f);
        String message = "¡Hola! @#$%^&*()_+ çñüáéíóú";

        // When
        ResponseDTO response = new ResponseDTO(position, message);

        // Then
        assertEquals(position, response.getPosition());
        assertEquals(message, response.getMessage());
    }
}
