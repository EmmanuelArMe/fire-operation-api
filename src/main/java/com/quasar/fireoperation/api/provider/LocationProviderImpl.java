package com.quasar.fireoperation.api.provider;

import com.quasar.fireoperation.api.exception.LocationException;
import com.quasar.fireoperation.api.utils.Constants;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of LocationProvider using trilateration.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Component
@Slf4j
public class LocationProviderImpl implements LocationProvider {

    /**
     * Calculates the (x, y) location based on distances from three satellites.
     *
     * @param distances List of distances from the satellites [d1, d2, d3]
     * @return float array with the calculated coordinates [x, y]
     * @throws LocationException if the location cannot be determined
     * @see <a href="https://en.wikipedia.org/wiki/Trilateration">Trilateration</a>
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @Override
    public float[] getLocation(List<Float> distances) throws LocationException {
        log.debug("Iniciando cálculo de trilateración con distancias: {}", distances);

        if (distances == null || distances.size() != 3) {
            log.error("Número inválido de distancias recibidas. Esperadas: 3, Recibidas: {}",
                     distances != null ? distances.size() : "null");
            throw new IllegalArgumentException("Se requieren distancias de 3 satélites.");
        }

        // Validar que las distancias sean razonables
        for (Float distance : distances) {
            if (distance == null || distance < 0 || distance > 100000.0f) {
                log.error("Distancia inválida detectada: {}. Las distancias deben estar entre 0 y 100,000", distance);
                throw new LocationException("Distancias fuera del rango válido para trilateración");
            }
        }

        // Satellite positions
        float[] p1 = Constants.SATELLITE_POSITIONS.get("kenobi");
        float[] p2 = Constants.SATELLITE_POSITIONS.get("skywalker");
        float[] p3 = Constants.SATELLITE_POSITIONS.get("sato");

        log.debug("Posiciones de satélites - Kenobi: {}, Skywalker: {}, Sato: {}",
                 Arrays.toString(p1), Arrays.toString(p2), Arrays.toString(p3));

        float d1 = distances.get(0);
        float d2 = distances.get(1);
        float d3 = distances.get(2);

        log.debug("Distancias asignadas - d1: {}, d2: {}, d3: {}", d1, d2, d3);

        // Algorithm coefficients calculation based on trilateration formulas
        float[] coefficients1 = calculateLinearCoefficients(p2, p1);
        float coefficientA = coefficients1[0];
        float coefficientB = coefficients1[1];
        float constantC = calculateTriangulationConstant(d1, d2, p1, p2);

        float[] coefficients2 = calculateLinearCoefficients(p3, p2);
        float coefficientD = coefficients2[0];
        float coefficientE = coefficients2[1];
        float constantF = calculateTriangulationConstant(d2, d3, p2, p3);

        log.debug("Coeficientes calculados - A: {}, B: {}, C: {}, D: {}, E: {}, F: {}",
                 coefficientA, coefficientB, constantC, coefficientD, coefficientE, constantF);

        float denominator = coefficientA * coefficientE - coefficientD * coefficientB;
        if (Math.abs(denominator) < 1e-10) {
            log.error("Determinante muy pequeño o cero: {}. No se puede resolver el sistema de ecuaciones", denominator);
            throw new LocationException("Configuración de satélites inválida para trilateración");
        }

        float x = (constantC * coefficientE - constantF * coefficientB) / denominator;
        float y = (coefficientA * constantF - coefficientD * constantC) / denominator;

        // Validar que la posición calculada sea razonable
        if (Float.isNaN(x) || Float.isNaN(y) || Float.isInfinite(x) || Float.isInfinite(y)) {
            log.error("Posición calculada inválida: x={}, y={}", x, y);
            throw new LocationException("No se pudo determinar una posición válida");
        }

        // Validar que la posición esté dentro de un rango razonable
        if (Math.abs(x) > 50000 || Math.abs(y) > 50000) {
            log.error("Posición calculada fuera del rango válido: x={}, y={}", x, y);
            throw new LocationException("Posición calculada fuera del área de cobertura");
        }

        log.info("Ubicación calculada exitosamente: x={}, y={}", x, y);
        return new float[]{x, y};
    }

    /**
     * Calcula los coeficientes lineales para el algoritmo de trilateración.
     *
     * @param point1 coordenadas del primer punto [x, y]
     * @param point2 coordenadas del segundo punto [x, y]
     * @return array con los coeficientes [A, B] donde A = 2*(x1-x2) y B = 2*(y1-y2)
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    private float[] calculateLinearCoefficients(float[] point1, float[] point2) {
        float coefficientA = 2 * (point1[0] - point2[0]);
        float coefficientB = 2 * (point1[1] - point2[1]);

        log.trace("Coeficientes lineales calculados para puntos {} y {}: A={}, B={}",
                 Arrays.toString(point1), Arrays.toString(point2), coefficientA, coefficientB);

        return new float[]{coefficientA, coefficientB};
    }

    /**
     * Calcula la constante de trilateración para dos puntos y sus distancias.
     *
     * @param d1 distancia del primer punto
     * @param d2 distancia del segundo punto
     * @param p1 coordenadas del primer punto [x, y]
     * @param p2 coordenadas del segundo punto [x, y]
     * @return constante calculada para el algoritmo de trilateración
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    private float calculateTriangulationConstant(float d1, float d2, float[] p1, float[] p2) {
        float constant = d1*d1 - d2*d2 - p1[0]*p1[0] + p2[0]*p2[0] - p1[1]*p1[1] + p2[1]*p2[1];

        log.trace("Constante de trilateración calculada para d1={}, d2={}, p1={}, p2={}: {}",
                 d1, d2, Arrays.toString(p1), Arrays.toString(p2), constant);

        return constant;
    }
}