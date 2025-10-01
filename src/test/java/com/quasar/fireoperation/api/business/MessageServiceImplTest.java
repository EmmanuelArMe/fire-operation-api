package com.quasar.fireoperation.api.business;

import com.quasar.fireoperation.api.domain.general.*;
import com.quasar.fireoperation.api.exception.LocationException;
import com.quasar.fireoperation.api.exception.MessageException;
import com.quasar.fireoperation.api.provider.LocationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MessageServiceImpl class.
 * Tests all business logic including location calculation and message reconstruction.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtes01@gmail.com)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MessageService Tests")
class MessageServiceImplTest {

    @Mock
    private LocationProvider locationProvider;

    @InjectMocks
    private MessageBusinessImpl messageBusiness;

    private TopSecretRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        List<SatelliteDTO> validSatellites = Arrays.asList(
                new SatelliteDTO("kenobi", 100.0f, Arrays.asList("este", "", "", "mensaje", "")),
                new SatelliteDTO("skywalker", 115.5f, Arrays.asList("", "es", "", "", "secreto")),
                new SatelliteDTO("sato", 142.7f, Arrays.asList("este", "", "un", "", ""))
        );
        validRequest = new TopSecretRequestDTO(validSatellites);
    }

    @Test
    @DisplayName("Should process TopSecret request successfully")
    void processTopSecret_ValidRequest_ReturnsResponse() {
        // Given
        float[] expectedPosition = {-58.31f, -69.55f};
        when(locationProvider.getLocation(anyList())).thenReturn(expectedPosition);

        // When
        ResponseDTO result = messageBusiness.processTopSecret(validRequest);

        // Then
        assertNotNull(result);
        assertNotNull(result.getPosition());
        assertEquals(-58.31f, result.getPosition().getX());
        assertEquals(-69.55f, result.getPosition().getY());
        assertEquals("este es un mensaje secreto", result.getMessage());

        verify(locationProvider).getLocation(Arrays.asList(100.0f, 115.5f, 142.7f));
    }

    @Test
    @DisplayName("Should throw LocationException when position calculation fails")
    void processTopSecret_LocationCalculationFails_ThrowsLocationException() {
        // Given
        when(locationProvider.getLocation(anyList())).thenThrow(new RuntimeException("Position calculation error"));

        // When & Then
        LocationException exception = assertThrows(LocationException.class,
            () -> messageBusiness.processTopSecret(validRequest));

        assertEquals("No se pudo determinar la posición.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw MessageException when message reconstruction fails")
    void processTopSecret_EmptyMessage_ThrowsMessageException() {
        // Given
        float[] validPosition = {-58.31f, -69.55f};
        when(locationProvider.getLocation(anyList())).thenReturn(validPosition);

        List<SatelliteDTO> satellitesWithEmptyMessages = Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, Arrays.asList("", "", "")),
            new SatelliteDTO("skywalker", 115.5f, Arrays.asList("", "", "")),
            new SatelliteDTO("sato", 142.7f, Arrays.asList("", "", ""))
        );
        TopSecretRequestDTO requestWithEmptyMessages = new TopSecretRequestDTO(satellitesWithEmptyMessages);

        // When & Then
        MessageException exception = assertThrows(MessageException.class,
            () -> messageBusiness.processTopSecret(requestWithEmptyMessages));

        assertEquals("No se pudo determinar el mensaje.", exception.getMessage());
    }

    @Test
    @DisplayName("Should save satellite split data successfully")
    void saveSatelliteSplit_ValidData_SavesSuccessfully() {
        // Given
        String satelliteName = "kenobi";
        float distance = 100.0f;
        List<String> message = Arrays.asList("este", "", "", "mensaje", "");

        // When
        assertDoesNotThrow(() -> messageBusiness.saveSatelliteSplit(satelliteName, distance, message));

        // Then - should not throw any exception
    }

    @Test
    @DisplayName("Should process TopSecretSplit successfully with 3 satellites")
    void processTopSecretSplit_ThreeSatellites_ReturnsResponse() {
        // Given
        float[] expectedPosition = {-58.31f, -69.55f};
        when(locationProvider.getLocation(anyList())).thenReturn(expectedPosition);

        // Save 3 satellites
        messageBusiness.saveSatelliteSplit("kenobi", 100.0f, Arrays.asList("este", "", "", "mensaje", ""));
        messageBusiness.saveSatelliteSplit("skywalker", 115.5f, Arrays.asList("", "es", "", "", "secreto"));
        messageBusiness.saveSatelliteSplit("sato", 142.7f, Arrays.asList("este", "", "un", "", ""));

        // When
        ResponseDTO result = messageBusiness.processTopSecretSplit();

        // Then
        assertNotNull(result);
        assertNotNull(result.getPosition());
        assertEquals(-58.31f, result.getPosition().getX());
        assertEquals(-69.55f, result.getPosition().getY());
        assertEquals("este es un mensaje secreto", result.getMessage());
    }

    @Test
    @DisplayName("Should throw LocationException when insufficient satellites for split processing")
    void processTopSecretSplit_InsufficientSatellites_ThrowsLocationException() {
        // Given - only 2 satellites
        messageBusiness.saveSatelliteSplit("kenobi", 100.0f, Arrays.asList("este", "", "", "mensaje", ""));
        messageBusiness.saveSatelliteSplit("skywalker", 115.5f, Arrays.asList("", "es", "", "", "secreto"));

        // When & Then
        LocationException exception = assertThrows(LocationException.class,
            () -> messageBusiness.processTopSecretSplit());

        assertEquals("Información insuficiente de satélites.", exception.getMessage());
    }

    @Test
    @DisplayName("Should clear satellites cache after successful split processing")
    void processTopSecretSplit_SuccessfulProcessing_ClearsSatellitesCache() {
        // Given
        float[] expectedPosition = {-58.31f, -69.55f};
        when(locationProvider.getLocation(anyList())).thenReturn(expectedPosition);

        // Save 3 satellites
        messageBusiness.saveSatelliteSplit("kenobi", 100.0f, Arrays.asList("este", "", "", "mensaje", ""));
        messageBusiness.saveSatelliteSplit("skywalker", 115.5f, Arrays.asList("", "es", "", "", "secreto"));
        messageBusiness.saveSatelliteSplit("sato", 142.7f, Arrays.asList("este", "", "un", "", ""));

        // When
        messageBusiness.processTopSecretSplit();

        // Then - should throw exception because cache was cleared
        LocationException exception = assertThrows(LocationException.class,
            () -> messageBusiness.processTopSecretSplit());

        assertEquals("Información insuficiente de satélites.", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle satellite names case insensitively")
    void saveSatelliteSplit_UppercaseName_SavesWithLowercase() {
        // Given
        messageBusiness.saveSatelliteSplit("KENOBI", 100.0f, List.of("test"));
        messageBusiness.saveSatelliteSplit("skywalker", 115.5f, List.of("test"));
        messageBusiness.saveSatelliteSplit("Sato", 142.7f, List.of("test"));

        // When & Then - should have 3 satellites
        when(locationProvider.getLocation(anyList())).thenReturn(new float[]{0f, 0f});
        assertDoesNotThrow(() -> messageBusiness.processTopSecretSplit());
    }

    @Test
    @DisplayName("Should reconstruct partial messages correctly")
    void processTopSecret_PartialMessages_ReconstructsCorrectly() {
        // Given
        float[] validPosition = {0f, 0f};
        when(locationProvider.getLocation(anyList())).thenReturn(validPosition);

        List<SatelliteDTO> satellitesWithPartialMessages = Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, Arrays.asList("", "mensaje", "")),
            new SatelliteDTO("skywalker", 115.5f, Arrays.asList("un", "", "secreto")),
            new SatelliteDTO("sato", 142.7f, Arrays.asList("", "", ""))
        );
        TopSecretRequestDTO request = new TopSecretRequestDTO(satellitesWithPartialMessages);

        // When
        ResponseDTO result = messageBusiness.processTopSecret(request);

        // Then
        assertEquals("un mensaje secreto", result.getMessage());
    }

    @Test
    @DisplayName("Should handle different message array lengths")
    void processTopSecret_DifferentMessageLengths_ReconstructsCorrectly() {
        // Given
        float[] validPosition = {0f, 0f};
        when(locationProvider.getLocation(anyList())).thenReturn(validPosition);

        List<SatelliteDTO> satellitesWithDifferentLengths = Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, Arrays.asList("este", "es")),
            new SatelliteDTO("skywalker", 115.5f, Arrays.asList("", "", "mensaje")),
            new SatelliteDTO("sato", 142.7f, Arrays.asList("", "", "", "completo"))
        );
        TopSecretRequestDTO request = new TopSecretRequestDTO(satellitesWithDifferentLengths);

        // When
        ResponseDTO result = messageBusiness.processTopSecret(request);

        // Then
        assertEquals("este es mensaje completo", result.getMessage());
    }
}
