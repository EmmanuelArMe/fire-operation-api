package com.quasar.fireoperation.api.rest;

import com.quasar.fireoperation.api.business.MessageBusiness;
import com.quasar.fireoperation.api.domain.general.*;
import com.quasar.fireoperation.api.exception.LocationException;
import com.quasar.fireoperation.api.exception.MessageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TopSecretRest controller.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@WebMvcTest(TopSecretRest.class)
@DisplayName("TopSecretRest Controller Tests")
class TopSecretRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageBusiness messageBusiness;

    @Autowired
    private ObjectMapper objectMapper;

    private TopSecretRequestDTO validRequest;
    private ResponseDTO expectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = new TopSecretRequestDTO(Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, Arrays.asList("este", "", "", "mensaje", "")),
            new SatelliteDTO("skywalker", 115.5f, Arrays.asList("", "es", "", "", "secreto")),
            new SatelliteDTO("sato", 142.7f, Arrays.asList("este", "", "un", "", ""))
        ));

        expectedResponse = new ResponseDTO(
            new PositionDTO(-58.31f, -69.55f),
            "este es un mensaje secreto"
        );
    }

    @Test
    @DisplayName("Should return 200 and response when processing valid request")
    void postTopSecret_ValidRequest_Returns200() throws Exception {
        // Given
        when(messageBusiness.processTopSecret(any(TopSecretRequestDTO.class)))
            .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.position.x").value(-58.31f))
                .andExpect(jsonPath("$.position.y").value(-69.55f))
                .andExpect(jsonPath("$.message").value("este es un mensaje secreto"));
    }

    @Test
    @DisplayName("Should return 404 when LocationException is thrown")
    void postTopSecret_LocationException_Returns404() throws Exception {
        // Given
        when(messageBusiness.processTopSecret(any(TopSecretRequestDTO.class)))
            .thenThrow(new LocationException("No se pudo determinar la posición."));

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se pudo determinar la posición."));
    }

    @Test
    @DisplayName("Should return 404 when MessageException is thrown")
    void postTopSecret_MessageException_Returns404() throws Exception {
        // Given
        when(messageBusiness.processTopSecret(any(TopSecretRequestDTO.class)))
            .thenThrow(new MessageException("No se pudo determinar el mensaje."));

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se pudo determinar el mensaje."));
    }

    @Test
    @DisplayName("Should return 500 when unexpected exception is thrown")
    void postTopSecret_UnexpectedException_Returns500() throws Exception {
        // Given
        when(messageBusiness.processTopSecret(any(TopSecretRequestDTO.class)))
            .thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    @Test
    @DisplayName("Should return 400 when request body is malformed")
    void postTopSecret_MalformedRequest_Returns400() throws Exception {
        // When & Then - Using truly malformed JSON (missing closing bracket and brace)
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"satellites\": [{\"name\": \"kenobi\", \"distance\": 100.0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 415 when content type is not JSON")
    void postTopSecret_InvalidContentType_Returns415() throws Exception {
        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.TEXT_PLAIN)
                .content("some text"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Should handle empty satellites list")
    void postTopSecret_EmptySatellitesList_ProcessesRequest() throws Exception {
        // Given
        TopSecretRequestDTO emptyRequest = new TopSecretRequestDTO(List.of());
        when(messageBusiness.processTopSecret(any(TopSecretRequestDTO.class)))
            .thenThrow(new LocationException("Se requieren distancias de 3 satélites."));

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isNotFound());
    }
}
