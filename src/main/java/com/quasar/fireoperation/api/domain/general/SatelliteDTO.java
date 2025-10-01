package com.quasar.fireoperation.api.domain.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO representing a satellite input.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteDTO {
    private String name;
    private float distance;
    private List<String> message;
}