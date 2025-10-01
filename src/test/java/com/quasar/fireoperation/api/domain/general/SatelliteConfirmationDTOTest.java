package com.quasar.fireoperation.api.domain.general;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SatelliteConfirmationDTO class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("SatelliteConfirmationDTO Tests")
class SatelliteConfirmationDTOTest {

    @Test
    @DisplayName("Should create DTO with constructor")
    void constructor_WithParameters_CreatesDTO() {
        // Given
        String message = "Test message";
        String satelliteName = "kenobi";
        String instructions = "Test instructions";

        // When
        SatelliteConfirmationDTO dto = new SatelliteConfirmationDTO(message, satelliteName, instructions);

        // Then
        assertEquals(message, dto.getMessage());
        assertEquals(satelliteName, dto.getSatelliteName());
        assertEquals(instructions, dto.getInstructions());
    }

    @Test
    @DisplayName("Should create DTO with default constructor")
    void defaultConstructor_CreatesEmptyDTO() {
        // When
        SatelliteConfirmationDTO dto = new SatelliteConfirmationDTO();

        // Then
        assertNull(dto.getMessage());
        assertNull(dto.getSatelliteName());
        assertNull(dto.getInstructions());
    }

    /**
     * Provides test cases for satellite success confirmations.
     * Each argument contains: satellite name and expected confirmation message.
     */
    static Stream<Arguments> satelliteSuccessProvider() {
        return Stream.of(
            Arguments.of("kenobi", "Información del satélite kenobi guardada exitosamente"),
            Arguments.of("skywalker", "Información del satélite skywalker guardada exitosamente"),
            Arguments.of("sato", "Información del satélite sato guardada exitosamente"),
            Arguments.of("test-satellite_123", "Información del satélite test-satellite_123 guardada exitosamente")
        );
    }

    @ParameterizedTest
    @MethodSource("satelliteSuccessProvider")
    @DisplayName("Should create success confirmation for different satellite names")
    void success_DifferentSatelliteNames_CreatesCorrectConfirmation(String satelliteName, String expectedMessage) {
        // When
        SatelliteConfirmationDTO dto = SatelliteConfirmationDTO.success(satelliteName);

        // Then
        assertEquals(expectedMessage, dto.getMessage());
        assertEquals(satelliteName, dto.getSatelliteName());
        assertEquals("Los datos han sido almacenados. Envía información de los 3 satélites para obtener la ubicación.", dto.getInstructions());
    }

    @Test
    @DisplayName("Should handle null satellite name in success method")
    void success_NullSatelliteName_HandlesGracefully() {
        // When
        SatelliteConfirmationDTO dto = SatelliteConfirmationDTO.success(null);

        // Then
        assertEquals("Información del satélite null guardada exitosamente", dto.getMessage());
        assertNull(dto.getSatelliteName());
        assertEquals("Los datos han sido almacenados. Envía información de los 3 satélites para obtener la ubicación.", dto.getInstructions());
    }

    @Test
    @DisplayName("Should handle empty satellite name in success method")
    void success_EmptySatelliteName_HandlesCorrectly() {
        // When
        SatelliteConfirmationDTO dto = SatelliteConfirmationDTO.success("");

        // Then
        assertEquals("Información del satélite  guardada exitosamente", dto.getMessage());
        assertEquals("", dto.getSatelliteName());
        assertEquals("Los datos han sido almacenados. Envía información de los 3 satélites para obtener la ubicación.", dto.getInstructions());
    }

    @Test
    @DisplayName("Should allow setting properties")
    void setters_SetProperties_UpdatesValues() {
        // Given
        SatelliteConfirmationDTO dto = new SatelliteConfirmationDTO();
        String newMessage = "New message";
        String newSatelliteName = "new-satellite";
        String newInstructions = "New instructions";

        // When
        dto.setMessage(newMessage);
        dto.setSatelliteName(newSatelliteName);
        dto.setInstructions(newInstructions);

        // Then
        assertEquals(newMessage, dto.getMessage());
        assertEquals(newSatelliteName, dto.getSatelliteName());
        assertEquals(newInstructions, dto.getInstructions());
    }
}
