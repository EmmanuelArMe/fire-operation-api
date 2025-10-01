package com.quasar.fireoperation.api.domain.general;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TopSecretRequestDTO class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("TopSecretRequestDTO Tests")
class TopSecretRequestDTOTest {

    @Test
    @DisplayName("Should create request with satellites list")
    void constructor_WithSatellites_CreatesRequest() {
        // Given
        List<SatelliteDTO> satellites = Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, Arrays.asList("este", "", "", "mensaje", "")),
            new SatelliteDTO("skywalker", 115.5f, Arrays.asList("", "es", "", "", "secreto")),
            new SatelliteDTO("sato", 142.7f, Arrays.asList("este", "", "un", "", ""))
        );

        // When
        TopSecretRequestDTO request = new TopSecretRequestDTO(satellites);

        // Then
        assertEquals(satellites, request.getSatellites());
        assertEquals(3, request.getSatellites().size());
    }

    @Test
    @DisplayName("Should handle empty satellites list")
    void constructor_EmptySatellites_HandlesCorrectly() {
        // Given
        List<SatelliteDTO> satellites = Collections.emptyList();

        // When
        TopSecretRequestDTO request = new TopSecretRequestDTO(satellites);

        // Then
        assertEquals(satellites, request.getSatellites());
        assertTrue(request.getSatellites().isEmpty());
    }

    @Test
    @DisplayName("Should handle null satellites list")
    void constructor_NullSatellites_HandlesCorrectly() {
        // When
        TopSecretRequestDTO request = new TopSecretRequestDTO(null);

        // Then
        assertNull(request.getSatellites());
    }

    @Test
    @DisplayName("Should handle single satellite")
    void constructor_SingleSatellite_HandlesCorrectly() {
        // Given
        List<SatelliteDTO> satellites = List.of(
                new SatelliteDTO("kenobi", 100.0f, List.of("test"))
        );

        // When
        TopSecretRequestDTO request = new TopSecretRequestDTO(satellites);

        // Then
        assertEquals(1, request.getSatellites().size());
        assertEquals("kenobi", request.getSatellites().getFirst().getName());
    }

    @Test
    @DisplayName("Should handle more than three satellites")
    void constructor_MoreThanThreeSatellites_HandlesCorrectly() {
        // Given
        List<SatelliteDTO> satellites = Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, List.of("test")),
            new SatelliteDTO("skywalker", 115.5f, List.of("test")),
            new SatelliteDTO("sato", 142.7f, List.of("test")),
            new SatelliteDTO("extra", 200.0f, List.of("test"))
        );

        // When
        TopSecretRequestDTO request = new TopSecretRequestDTO(satellites);

        // Then
        assertEquals(4, request.getSatellites().size());
    }

    @Test
    @DisplayName("Should handle satellites with null values")
    void constructor_SatellitesWithNullValues_HandlesCorrectly() {
        // Given
        List<SatelliteDTO> satellites = Arrays.asList(
            new SatelliteDTO(null, 0.0f, null),
            new SatelliteDTO("", Float.NaN, Collections.emptyList())
        );

        // When
        TopSecretRequestDTO request = new TopSecretRequestDTO(satellites);

        // Then
        assertEquals(2, request.getSatellites().size());
        assertNull(request.getSatellites().get(0).getName());
        assertTrue(Float.isNaN(request.getSatellites().get(1).getDistance()));
    }
}
