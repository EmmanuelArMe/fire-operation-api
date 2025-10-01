package com.quasar.fireoperation.api.utils;

import java.util.Map;
import java.util.HashMap;

/**
 * Utility class holding satellite positions and constants.
 * <p>
 * This class contains the fixed positions of the rebel satellites
 * used in the trilateration algorithm for spacecraft location.
 * </p>
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
public final class Constants {

    /**
     * Map containing the fixed positions of rebel satellites.
     * - Kenobi: (-500, -200)
     * - Skywalker: (100, -100)
     * - Sato: (500, 100)
     *
     * @see <a href="https://en.wikipedia.org/wiki/Trilateration">Trilateration</a>
     * @since 2025
     * @autor Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    public static final Map<String, float[]> SATELLITE_POSITIONS;

    static {
        Map<String, float[]> positions = new HashMap<>();
        positions.put("kenobi", new float[]{-500, -200});
        positions.put("skywalker", new float[]{100, -100});
        positions.put("sato", new float[]{500, 100});
        SATELLITE_POSITIONS = Map.copyOf(positions);
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws UnsupportedOperationException if instantiation is attempted.
     * @since 2025
     * @autor Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}