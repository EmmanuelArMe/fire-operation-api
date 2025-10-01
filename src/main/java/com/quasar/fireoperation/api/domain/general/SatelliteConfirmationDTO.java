package com.quasar.fireoperation.api.domain.general;

import lombok.*;

/**
 * Response DTO for satellite split POST confirmation.
 * <p>
 * This class represents the response returned when satellite information
 * is successfully stored in the split operation mode.
 * </p>
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SatelliteConfirmationDTO {

    private String message;
    private String satelliteName;
    private String instructions;

    /**
     * Factory method to create a successful satellite confirmation.
     *
     * @param satelliteName name of the satellite
     * @return confirmation DTO with success message
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    public static SatelliteConfirmationDTO success(String satelliteName) {
        return new SatelliteConfirmationDTO(
            "Información del satélite " + satelliteName + " guardada exitosamente",
            satelliteName,
            "Los datos han sido almacenados. Envía información de los 3 satélites para obtener la ubicación."
        );
    }
}
