package com.quasar.fireoperation.api.rest;

import com.quasar.fireoperation.api.business.MessageBusiness;
import com.quasar.fireoperation.api.domain.general.TopSecretRequestDTO;
import com.quasar.fireoperation.api.domain.general.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for /top-secret API.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */

@RestController
@RequestMapping("/top-secret")
@Tag(
    name = "TopSecret Rest",
     description = "Endpoint for processing data from all satellites to determine position and message"
)
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Validated
@Slf4j
public class TopSecretRest {

    MessageBusiness messageBusiness;

    /**
     * Endpoint to process data from all satellites and determine position and message.
     *
     * @param request Request body containing data from all satellites
     * @return Response with determined position and message
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */

    @Operation(
        summary = "Process data from all satellites",
        description = "Determines the position and message based on data from all satellites"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully processed data and determined position and message",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request due to invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Could not determine position or message with provided data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while processing the request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            )
    })

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> getTopSecret(@RequestBody TopSecretRequestDTO request) {
        log.info("Recibida solicitud POST /top-secret con {} satélites",
                request.getSatellites() != null ? request.getSatellites().size() : 0);
        log.debug("Request completo: {}", request);

        try {
            ResponseDTO response = messageBusiness.processTopSecret(request);
            log.info("Respuesta enviada exitosamente para /top-secret - Posición: ({}, {})",
                    response.getPosition().getX(), response.getPosition().getY());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error procesando solicitud /top-secret: {}", ex.getMessage(), ex);
            throw ex; // Let global exception handler manage the error response
        }
    }
}