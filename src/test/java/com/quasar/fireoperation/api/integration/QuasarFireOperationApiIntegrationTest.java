package com.quasar.fireoperation.api.integration;

import com.quasar.fireoperation.api.domain.general.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasar.fireoperation.api.rest.TopSecretSplitRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the complete Quasar Fire Operation API.
 * Tests end-to-end functionality across all layers.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Quasar Fire Operation API Integration Tests")
class QuasarFireOperationApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Clear any existing satellite data before each test to ensure isolation
        try {
            mockMvc.perform(get("/top-secret-split"))
                    .andReturn();
        } catch (Exception _) {
            // Ignore exceptions during cleanup
        }
    }

    @Test
    @DisplayName("Should process complete TopSecret request end-to-end")
    void topSecretEndToEnd_ValidRequest_ReturnsSuccess() throws Exception {
        // Given
        TopSecretRequestDTO request = new TopSecretRequestDTO(Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, Arrays.asList("este", "", "", "mensaje", "")),
            new SatelliteDTO("skywalker", 115.5f, Arrays.asList("", "es", "", "", "secreto")),
            new SatelliteDTO("sato", 142.7f, Arrays.asList("este", "", "un", "", ""))
        ));

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.position").exists())
                .andExpect(jsonPath("$.position.x").isNumber())
                .andExpect(jsonPath("$.position.y").isNumber())
                .andExpect(jsonPath("$.message").value("este es un mensaje secreto"));
    }

    @Test
    @DisplayName("Should process complete TopSecretSplit workflow end-to-end")
    void topSecretSplitEndToEnd_ValidWorkflow_ReturnsSuccess() throws Exception {
        // Step 1: Send first satellite
        TopSecretSplitRest.SatelliteRequest kenobiRequest = new TopSecretSplitRest.SatelliteRequest();
        kenobiRequest.setDistance(100.0f);
        kenobiRequest.setMessage(Arrays.asList("este", "", "", "mensaje", ""));

        mockMvc.perform(post("/top-secret-split/kenobi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kenobiRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.satelliteName").value("kenobi"));

        // Step 2: Send second satellite
        TopSecretSplitRest.SatelliteRequest skywalkerRequest = new TopSecretSplitRest.SatelliteRequest();
        skywalkerRequest.setDistance(115.5f);
        skywalkerRequest.setMessage(Arrays.asList("", "es", "", "", "secreto"));

        mockMvc.perform(post("/top-secret-split/skywalker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skywalkerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.satelliteName").value("skywalker"));

        // Step 3: Send third satellite
        TopSecretSplitRest.SatelliteRequest satoRequest = new TopSecretSplitRest.SatelliteRequest();
        satoRequest.setDistance(142.7f);
        satoRequest.setMessage(Arrays.asList("este", "", "un", "", ""));

        mockMvc.perform(post("/top-secret-split/sato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(satoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.satelliteName").value("sato"));

        // Step 4: Get final result
        mockMvc.perform(get("/top-secret-split")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.position").exists())
                .andExpect(jsonPath("$.position.x").isNumber())
                .andExpect(jsonPath("$.position.y").isNumber())
                .andExpect(jsonPath("$.message").value("este es un mensaje secreto"));
    }

    @Test
    @DisplayName("Should return 404 when insufficient satellites for split processing")
    void topSecretSplitEndToEnd_InsufficientSatellites_Returns404() throws Exception {
        // Given - Only send 2 satellites (ensuring clean state)
        TopSecretSplitRest.SatelliteRequest request = new TopSecretSplitRest.SatelliteRequest();
        request.setDistance(100.0f);
        request.setMessage(List.of("test"));

        mockMvc.perform(post("/top-secret-split/kenobi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/top-secret-split/skywalker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // When & Then - Try to get result with only 2 satellites
        mockMvc.perform(get("/top-secret-split")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Información insuficiente de satélites."));
    }

    @Test
    @DisplayName("Should handle invalid trilateration configuration")
    void topSecretEndToEnd_InvalidConfiguration_Returns404() throws Exception {
        // Given - distances that result in invalid trilateration
        TopSecretRequestDTO request = new TopSecretRequestDTO(Arrays.asList(
            new SatelliteDTO("kenobi", 1000000.0f, List.of("test")),
            new SatelliteDTO("skywalker", 1000000.0f, List.of("message")),
            new SatelliteDTO("sato", 1000000.0f, List.of("here"))
        ));

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle malformed JSON requests")
    void endToEnd_MalformedJson_Returns400() throws Exception {
        // When & Then - Using truly malformed JSON (missing closing brace)
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"satellites\": [{\"name\": \"kenobi\", \"distance\": 100.0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle empty message reconstruction")
    void topSecretEndToEnd_EmptyMessages_ThrowsMessageException() throws Exception {
        // Given - satellites with completely empty messages
        TopSecretRequestDTO request = new TopSecretRequestDTO(Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, Arrays.asList("", "", "")),
            new SatelliteDTO("skywalker", 115.5f, Arrays.asList("", "", "")),
            new SatelliteDTO("sato", 142.7f, Arrays.asList("", "", ""))
        ));

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se pudo determinar el mensaje."));
    }

    @Test
    @DisplayName("Should handle insufficient satellites in request")
    void topSecretEndToEnd_InsufficientSatellites_Returns400() throws Exception {
        // Given - request with only 2 satellites
        TopSecretRequestDTO request = new TopSecretRequestDTO(Arrays.asList(
            new SatelliteDTO("kenobi", 100.0f, List.of("test")),
            new SatelliteDTO("skywalker", 115.5f, List.of("message"))
        ));

        // When & Then
        mockMvc.perform(post("/top-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Se requieren distancias de 3 satélites."));
    }
}
