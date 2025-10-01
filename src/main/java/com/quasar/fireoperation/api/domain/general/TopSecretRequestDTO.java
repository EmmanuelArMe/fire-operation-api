package com.quasar.fireoperation.api.domain.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for /TopSecret/ request payload.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopSecretRequestDTO {
    private List<SatelliteDTO> satellites;
}