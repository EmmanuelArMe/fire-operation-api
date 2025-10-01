package com.quasar.fireoperation.api.rest;

import com.quasar.fireoperation.api.business.MessageBusiness;
import com.quasar.fireoperation.api.domain.general.*;
import com.quasar.fireoperation.api.exception.LocationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TopSecretSplitRest controller.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@WebMvcTest(TopSecretSplitRest.class)
@DisplayName("TopSecretSplitRest Controller Tests")
class TopSecretSplitRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageBusiness messageBusiness;

    @Autowired
    private ObjectMapper objectMapper;

    private TopSecretSplitRest.SatelliteRequest validSatelliteRequest;
    private ResponseDTO expectedResponse;

    @BeforeEach
    void setUp() {
        validSatelliteRequest = new TopSecretSplitRest.SatelliteRequest();
        validSatelliteRequest.setDistance(100.0f);
        validSatelliteRequest.setMessage(Arrays.asList("este", "", "", "mensaje", ""));

        expectedResponse = new ResponseDTO(
            new PositionDTO(-58.31f, -69.55f),
            "este es un mensaje secreto"
        );
    }

    @Test
    @DisplayName("Should save satellite data and return confirmation")
    void postSatelliteSplit_ValidRequest_ReturnsConfirmation() throws Exception {
        // Given
        doNothing().when(messageBusiness).saveSatelliteSplit(anyString(), anyFloat(), anyList());

        // When & Then
        mockMvc.perform(post("/top-secret-split/kenobi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSatelliteRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Información del satélite kenobi guardada exitosamente"))
                .andExpect(jsonPath("$.satelliteName").value("kenobi"))
                .andExpect(jsonPath("$.instructions").value("Los datos han sido almacenados. Envía información de los 3 satélites para obtener la ubicación."));

        verify(messageBusiness).saveSatelliteSplit("kenobi", 100.0f, Arrays.asList("este", "", "", "mensaje", ""));
    }

    @Test
    @DisplayName("Should handle different satellite names")
    void postSatelliteSplit_DifferentSatellites_ReturnsCorrectConfirmation() throws Exception {
        // Given
        doNothing().when(messageBusiness).saveSatelliteSplit(anyString(), anyFloat(), anyList());

        // When & Then for skywalker
        mockMvc.perform(post("/top-secret-split/skywalker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSatelliteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.satelliteName").value("skywalker"));

        // When & Then for sato
        mockMvc.perform(post("/top-secret-split/sato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSatelliteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.satelliteName").value("sato"));
    }

    @Test
    @DisplayName("Should return 500 when service throws exception during save")
    void postSatelliteSplit_ServiceException_Returns500() throws Exception {
        // Given
        doThrow(new RuntimeException("Database error"))
            .when(messageBusiness).saveSatelliteSplit(anyString(), anyFloat(), anyList());

        // When & Then
        mockMvc.perform(post("/top-secret-split/kenobi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSatelliteRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should process split data and return location")
    void getTopSecretSplit_ValidData_ReturnsResponse() throws Exception {
        // Given
        when(messageBusiness.processTopSecretSplit()).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/top-secret-split")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.position.x").value(-58.31f))
                .andExpect(jsonPath("$.position.y").value(-69.55f))
                .andExpect(jsonPath("$.message").value("este es un mensaje secreto"));

        verify(messageBusiness).processTopSecretSplit();
    }

    @Test
    @DisplayName("Should return 404 when insufficient satellite data")
    void getTopSecretSplit_InsufficientData_Returns404() throws Exception {
        // Given
        when(messageBusiness.processTopSecretSplit())
            .thenThrow(new LocationException("Información insuficiente de satélites."));

        // When & Then
        mockMvc.perform(get("/top-secret-split")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Información insuficiente de satélites."));
    }

    @Test
    @DisplayName("Should return 400 for malformed POST request")
    void postSatelliteSplit_MalformedRequest_Returns400() throws Exception {
        // When & Then - Using truly malformed JSON (missing closing brace and bracket)
        mockMvc.perform(post("/top-secret-split/kenobi")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"distance\": 100.0, \"message\": [\"test\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle empty message arrays")
    void postSatelliteSplit_EmptyMessage_SavesSuccessfully() throws Exception {
        // Given
        TopSecretSplitRest.SatelliteRequest emptyMessageRequest = new TopSecretSplitRest.SatelliteRequest();
        emptyMessageRequest.setDistance(150.0f);
        emptyMessageRequest.setMessage(List.of());

        doNothing().when(messageBusiness).saveSatelliteSplit(anyString(), anyFloat(), anyList());

        // When & Then
        mockMvc.perform(post("/top-secret-split/kenobi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyMessageRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.satelliteName").value("kenobi"));

        verify(messageBusiness).saveSatelliteSplit("kenobi", 150.0f, List.of());
    }

    @Test
    @DisplayName("Should handle zero distance")
    void postSatelliteSplit_ZeroDistance_SavesSuccessfully() throws Exception {
        // Given
        validSatelliteRequest.setDistance(0.0f);
        doNothing().when(messageBusiness).saveSatelliteSplit(anyString(), anyFloat(), anyList());

        // When & Then
        mockMvc.perform(post("/top-secret-split/kenobi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSatelliteRequest)))
                .andExpect(status().isOk());

        verify(messageBusiness).saveSatelliteSplit("kenobi", 0.0f, Arrays.asList("este", "", "", "mensaje", ""));
    }

    @Test
    @DisplayName("Should handle negative distance")
    void postSatelliteSplit_NegativeDistance_SavesSuccessfully() throws Exception {
        // Given
        validSatelliteRequest.setDistance(-100.0f);
        doNothing().when(messageBusiness).saveSatelliteSplit(anyString(), anyFloat(), anyList());

        // When & Then
        mockMvc.perform(post("/top-secret-split/kenobi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSatelliteRequest)))
                .andExpect(status().isOk());

        verify(messageBusiness).saveSatelliteSplit("kenobi", -100.0f, Arrays.asList("este", "", "", "mensaje", ""));
    }

    @Test
    @DisplayName("Should handle special characters in satellite name")
    void postSatelliteSplit_SpecialCharacterSatelliteName_SavesSuccessfully() throws Exception {
        // Given
        doNothing().when(messageBusiness).saveSatelliteSplit(anyString(), anyFloat(), anyList());

        // When & Then
        mockMvc.perform(post("/top-secret-split/test-satellite_123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSatelliteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.satelliteName").value("test-satellite_123"));
    }
}
