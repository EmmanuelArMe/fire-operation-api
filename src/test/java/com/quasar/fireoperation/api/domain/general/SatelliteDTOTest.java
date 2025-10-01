package com.quasar.fireoperation.api.domain.general;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SatelliteDTO class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("SatelliteDTO Tests")
class SatelliteDTOTest {

    @Test
    @DisplayName("Should create DTO with constructor")
    void constructor_WithParameters_CreatesDTO() {
        // Given
        String name = "kenobi";
        float distance = 100.0f;
        List<String> message = Arrays.asList("este", "", "", "mensaje", "");

        // When
        SatelliteDTO dto = new SatelliteDTO(name, distance, message);

        // Then
        assertEquals(name, dto.getName());
        assertEquals(distance, dto.getDistance());
        assertEquals(message, dto.getMessage());
    }

    @Test
    @DisplayName("Should handle null name")
    void constructor_NullName_HandlesCorrectly() {
        // When
        SatelliteDTO dto = new SatelliteDTO(null, 100.0f, List.of("test"));

        // Then
        assertNull(dto.getName());
        assertEquals(100.0f, dto.getDistance());
    }

    @Test
    @DisplayName("Should handle empty message list")
    void constructor_EmptyMessage_HandlesCorrectly() {
        // When
        SatelliteDTO dto = new SatelliteDTO("test", 100.0f, List.of());

        // Then
        assertTrue(dto.getMessage().isEmpty());
    }

    @Test
    @DisplayName("Should handle null message list")
    void constructor_NullMessage_HandlesCorrectly() {
        // When
        SatelliteDTO dto = new SatelliteDTO("test", 100.0f, null);

        // Then
        assertNull(dto.getMessage());
    }

    @Test
    @DisplayName("Should handle zero distance")
    void constructor_ZeroDistance_HandlesCorrectly() {
        // When
        SatelliteDTO dto = new SatelliteDTO("test", 0.0f, List.of("test"));

        // Then
        assertEquals(0.0f, dto.getDistance());
    }

    @Test
    @DisplayName("Should handle negative distance")
    void constructor_NegativeDistance_HandlesCorrectly() {
        // When
        SatelliteDTO dto = new SatelliteDTO("test", -100.0f, List.of("test"));

        // Then
        assertEquals(-100.0f, dto.getDistance());
    }

    @Test
    @DisplayName("Should handle large distance values")
    void constructor_LargeDistance_HandlesCorrectly() {
        // When
        SatelliteDTO dto = new SatelliteDTO("test", Float.MAX_VALUE, List.of("test"));

        // Then
        assertEquals(Float.MAX_VALUE, dto.getDistance());
    }

    @Test
    @DisplayName("Should handle message with null elements")
    void constructor_MessageWithNullElements_HandlesCorrectly() {
        // Given
        List<String> messageWithNulls = Arrays.asList("este", null, "", "mensaje", null);

        // When
        SatelliteDTO dto = new SatelliteDTO("test", 100.0f, messageWithNulls);

        // Then
        assertEquals(messageWithNulls, dto.getMessage());
        assertNull(dto.getMessage().get(1));
        assertNull(dto.getMessage().get(4));
    }
}
