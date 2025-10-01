package com.quasar.fireoperation.api.domain.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard response DTO for location and message.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private PositionDTO position;
    private String message;
}
