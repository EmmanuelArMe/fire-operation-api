package com.quasar.fireoperation.api.rest;

import com.quasar.fireoperation.api.business.MessageBusiness;
import com.quasar.fireoperation.api.domain.general.ResponseDTO;
import com.quasar.fireoperation.api.domain.general.SatelliteConfirmationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for /top-secret-split API.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@RestController
@RequestMapping("/top-secret-split")
@Tag(
        name = "TopSecretSplit Rest",
        description = "Endpoints for receiving data from individual satellites and processing it"
)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@Slf4j
public class TopSecretSplitRest {

    MessageBusiness messageBusiness;

    /**
     * Endpoint to submit data from a single satellite.
     *
     * @param satelliteName Name of the satellite (path variable)
     * @param request       Request body containing distance and message
     * @return Confirmation of data saved
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */

    @Operation(
            summary = "Submit data from a single satellite",
            description = "Saves the distance and message from a specific satellite"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Satellite data saved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SatelliteConfirmationDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            )
    })
    @PostMapping("/{satelliteName}")
    public ResponseEntity<SatelliteConfirmationDTO> postSatelliteSplit(
            @PathVariable String satelliteName,
            @RequestBody SatelliteRequest request) {
        log.info("Recibida solicitud POST /top-secret-split/{} con distancia: {}",
                satelliteName, request.getDistance());
        log.debug("Datos del satélite {}: distancia={}, mensaje={}",
                satelliteName, request.getDistance(), request.getMessage());

        try {
            messageBusiness.saveSatelliteSplit(
                    satelliteName,
                    request.getDistance(),
                    request.getMessage()
            );

            SatelliteConfirmationDTO confirmation = SatelliteConfirmationDTO.success(satelliteName);
            log.info("Información del satélite {} guardada exitosamente", satelliteName);
            return ResponseEntity.ok(confirmation);
        } catch (Exception ex) {
            log.error("Error procesando datos del satélite {}: {}", satelliteName, ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Endpoint to process and retrieve the final position and message
     * after receiving data from all satellites.
     *
     * @return Final position and message
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */

    @Operation(
            summary = "Process and retrieve final position and message",
            description = "Processes stored satellite data to determine the position and message"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Position and message retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Insufficient data from satellites",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<ResponseDTO> getTopSecretSplit() {
        log.info("Recibida solicitud GET /top-secret-split para procesar datos almacenados");

        try {
            ResponseDTO response = messageBusiness.processTopSecretSplit();
            log.info("Respuesta enviada exitosamente para /top-secret-split - Posición: ({}, {})",
                    response.getPosition().getX(), response.getPosition().getY());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error procesando solicitud /top-secret-split: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Inner class for satellite split POST request.
     *
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @Setter
    @Getter
    public static class SatelliteRequest {
        private float distance;
        private List<String> message;
    }
}
