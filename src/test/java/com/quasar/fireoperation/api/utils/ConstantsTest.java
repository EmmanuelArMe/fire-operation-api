package com.quasar.fireoperation.api.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Constants utility class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("Constants Tests")
class ConstantsTest {

    @Test
    @DisplayName("Should have immutable satellite positions map")
    void satellitePositions_IsImmutable() {
        // When & Then
        assertThrows(UnsupportedOperationException.class, () ->
            Constants.SATELLITE_POSITIONS.put("test", new float[]{0, 0}));

        assertThrows(UnsupportedOperationException.class, () ->
            Constants.SATELLITE_POSITIONS.remove("kenobi"));

        assertThrows(UnsupportedOperationException.class, Constants.SATELLITE_POSITIONS::clear);
    }

    @Test
    @DisplayName("Should contain all required satellite positions")
    void satellitePositions_ContainsAllSatellites() {
        // When
        Map<String, float[]> positions = Constants.SATELLITE_POSITIONS;

        // Then
        assertTrue(positions.containsKey("kenobi"));
        assertTrue(positions.containsKey("skywalker"));
        assertTrue(positions.containsKey("sato"));
        assertEquals(3, positions.size());
    }

    @Test
    @DisplayName("Should have correct kenobi position")
    void satellitePositions_KenobiPosition_IsCorrect() {
        // When
        float[] kenobiPosition = Constants.SATELLITE_POSITIONS.get("kenobi");

        // Then
        assertNotNull(kenobiPosition);
        assertEquals(2, kenobiPosition.length);
        assertEquals(-500.0f, kenobiPosition[0]);
        assertEquals(-200.0f, kenobiPosition[1]);
    }

    @Test
    @DisplayName("Should have correct skywalker position")
    void satellitePositions_SkywalkerPosition_IsCorrect() {
        // When
        float[] skywalkerPosition = Constants.SATELLITE_POSITIONS.get("skywalker");

        // Then
        assertNotNull(skywalkerPosition);
        assertEquals(2, skywalkerPosition.length);
        assertEquals(100.0f, skywalkerPosition[0]);
        assertEquals(-100.0f, skywalkerPosition[1]);
    }

    @Test
    @DisplayName("Should have correct sato position")
    void satellitePositions_SatoPosition_IsCorrect() {
        // When
        float[] satoPosition = Constants.SATELLITE_POSITIONS.get("sato");

        // Then
        assertNotNull(satoPosition);
        assertEquals(2, satoPosition.length);
        assertEquals(500.0f, satoPosition[0]);
        assertEquals(100.0f, satoPosition[1]);
    }

    @Test
    @DisplayName("Should not be instantiable")
    void constructor_ThrowsException() throws Exception {
        // Given
        Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                constructor::newInstance);

        // Verify the actual cause is UnsupportedOperationException
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Should return null for non-existent satellite")
    void satellitePositions_NonExistentSatellite_ReturnsNull() {
        // When
        float[] position = Constants.SATELLITE_POSITIONS.get("nonexistent");

        // Then
        assertNull(position);
    }

    @Test
    @DisplayName("Should be case sensitive for satellite names")
    void satellitePositions_CaseSensitive() {
        // When
        float[] upperCaseKenobi = Constants.SATELLITE_POSITIONS.get("KENOBI");
        float[] lowerCaseKenobi = Constants.SATELLITE_POSITIONS.get("kenobi");

        // Then
        assertNull(upperCaseKenobi);
        assertNotNull(lowerCaseKenobi);
    }

    @Test
    @DisplayName("Should have satellite positions that form valid triangle")
    void satellitePositions_FormValidTriangle() {
        // Given
        float[] kenobi = Constants.SATELLITE_POSITIONS.get("kenobi");
        float[] skywalker = Constants.SATELLITE_POSITIONS.get("skywalker");
        float[] sato = Constants.SATELLITE_POSITIONS.get("sato");

        // When - check that satellites are not collinear
        // Three points are collinear if the area of triangle formed by them is 0
        float area = Math.abs((kenobi[0] * (skywalker[1] - sato[1]) +
                              skywalker[0] * (sato[1] - kenobi[1]) +
                              sato[0] * (kenobi[1] - skywalker[1])) / 2.0f);

        // Then
        assertTrue(area > 0, "Satellites should not be collinear to allow valid trilateration");
    }
}
