package com.quasar.fireoperation.api.domain.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the position (x, y) coordinates.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionDTO {
    private float x;
    private float y;
}