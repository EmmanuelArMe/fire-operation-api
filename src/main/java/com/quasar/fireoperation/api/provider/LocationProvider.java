package com.quasar.fireoperation.api.provider;

import com.quasar.fireoperation.api.exception.LocationException;

import java.util.List;

/**
 * Port interface for location calculation.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
public interface LocationProvider {
    float[] getLocation(List<Float> distances) throws LocationException;
}
