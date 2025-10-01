package com.quasar.fireoperation.api.domain.general;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PositionDTO class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@DisplayName("PositionDTO Tests")
class PositionDTOTest {

    @Test
    @DisplayName("Should create position with coordinates")
    void constructor_WithCoordinates_CreatesPosition() {
        // Given
        float x = -58.31f;
        float y = -69.55f;

        // When
        PositionDTO position = new PositionDTO(x, y);

        // Then
        assertEquals(x, position.getX());
        assertEquals(y, position.getY());
    }

    @Test
    @DisplayName("Should handle zero coordinates")
    void constructor_ZeroCoordinates_HandlesCorrectly() {
        // When
        PositionDTO position = new PositionDTO(0.0f, 0.0f);

        // Then
        assertEquals(0.0f, position.getX());
        assertEquals(0.0f, position.getY());
    }

    @Test
    @DisplayName("Should handle negative coordinates")
    void constructor_NegativeCoordinates_HandlesCorrectly() {
        // When
        PositionDTO position = new PositionDTO(-100.0f, -200.0f);

        // Then
        assertEquals(-100.0f, position.getX());
        assertEquals(-200.0f, position.getY());
    }

    @Test
    @DisplayName("Should handle large coordinate values")
    void constructor_LargeCoordinates_HandlesCorrectly() {
        // When
        PositionDTO position = new PositionDTO(Float.MAX_VALUE, Float.MAX_VALUE);

        // Then
        assertEquals(Float.MAX_VALUE, position.getX());
        assertEquals(Float.MAX_VALUE, position.getY());
    }

    @Test
    @DisplayName("Should handle floating point precision")
    void constructor_FloatingPointPrecision_HandlesCorrectly() {
        // Given
        float x = 123.456789f;
        float y = 987.654321f;

        // When
        PositionDTO position = new PositionDTO(x, y);

        // Then
        assertEquals(x, position.getX(), 0.000001f);
        assertEquals(y, position.getY(), 0.000001f);
    }

    @Test
    @DisplayName("Should handle NaN values")
    void constructor_NaNValues_HandlesCorrectly() {
        // When
        PositionDTO position = new PositionDTO(Float.NaN, Float.NaN);

        // Then
        assertTrue(Float.isNaN(position.getX()));
        assertTrue(Float.isNaN(position.getY()));
    }

    @Test
    @DisplayName("Should handle infinite values")
    void constructor_InfiniteValues_HandlesCorrectly() {
        // When
        PositionDTO position = new PositionDTO(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);

        // Then
        assertTrue(Float.isInfinite(position.getX()));
        assertTrue(Float.isInfinite(position.getY()));
        assertTrue(position.getX() > 0);
        assertTrue(position.getY() < 0);
    }
}
