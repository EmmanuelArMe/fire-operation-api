package com.quasar.fireoperation.api.business;

import com.quasar.fireoperation.api.domain.general.*;
import com.quasar.fireoperation.api.exception.*;
import com.quasar.fireoperation.api.provider.LocationProvider;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of MessageService.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MessageBusinessImpl implements MessageBusiness {

    LocationProvider locationProvider;
    // For split requests (Nivel 3)
    private final Map<String, SatelliteDTO> splitSatellites = new ConcurrentHashMap<>();

    /**
     * Processes the TopSecret request to determine position and message.
     * @param request The TopSecretRequestDTO containing satellite data.
     * @return The ResponseDTO with calculated position and message.
     * @throws LocationException if position cannot be determined.
     * @throws MessageException if message cannot be reconstructed.
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @Override
    public ResponseDTO processTopSecret(TopSecretRequestDTO request) {
        log.info("Procesando solicitud TopSecret con {} satélites", request.getSatellites().size());
        log.debug("Datos de satélites recibidos: {}", request.getSatellites());

        List<Float> distances = new ArrayList<>();
        List<List<String>> messages = new ArrayList<>();
        for (SatelliteDTO sat : request.getSatellites()) {
            distances.add(sat.getDistance());
            messages.add(sat.getMessage());
        }

        ResponseDTO response = getResponseDTO(distances, messages);
        log.info("TopSecret procesado exitosamente. Posición: ({}, {}), Mensaje: '{}'",
                response.getPosition().getX(), response.getPosition().getY(), response.getMessage());

        return response;
    }

    /**
     * Helper method to calculate position and reconstruct message.
     * @param distances List of distances from satellites.
     * @param messages List of message arrays from satellites.
     * @return The ResponseDTO with calculated position and reconstructed message.
     * @throws LocationException if position cannot be determined.
     * @throws MessageException if message cannot be reconstructed.
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    private ResponseDTO getResponseDTO(List<Float> distances, List<List<String>> messages) {
        log.debug("Calculando posición con distancias: {}", distances);

        float[] positionArr;
        try {
            positionArr = locationProvider.getLocation(distances);
            log.debug("Posición calculada exitosamente: ({}, {})", positionArr[0], positionArr[1]);
        } catch (IllegalArgumentException ex) {
            log.error("Error al calcular la posición: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error al calcular la posición: {}", ex.getMessage(), ex);
            throw new LocationException("No se pudo determinar la posición.");
        }

        log.debug("Reconstruyendo mensaje desde {} arrays de mensajes", messages.size());
        String message = getMessage(messages);
        if (message.isBlank()) {
            log.warn("No se pudo reconstruir el mensaje. Arrays recibidos: {}", messages);
            throw new MessageException("No se pudo determinar el mensaje.");
        }

        log.debug("Mensaje reconstruido exitosamente: '{}'", message);
        return new ResponseDTO(new PositionDTO(positionArr[0], positionArr[1]), message);
    }

    /**
     * Saves satellite data for split requests (Nivel 3).
     * @param name The name of the satellite.
     * @param distance The distance from the satellite.
     * @param message The message array from the satellite.
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @Override
    public void saveSatelliteSplit(String name, float distance, List<String> message) {
        log.info("Guardando información del satélite: {} con distancia: {}", name, distance);
        log.debug("Mensaje del satélite {}: {}", name, message);

        splitSatellites.put(name.toLowerCase(), new SatelliteDTO(name, distance, message));

        log.info("Información guardada. Total de satélites almacenados: {}", splitSatellites.size());
        log.debug("Satélites actuales en memoria: {}", splitSatellites.keySet());
    }

    /**
     * Processes the TopSecretSplit request to determine position and message
     * after receiving data from all satellites.
     * @return The ResponseDTO with calculated position and message.
     * @throws LocationException if position cannot be determined.
     * @throws MessageException if message cannot be reconstructed.
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @Override
    public ResponseDTO processTopSecretSplit() {
        log.info("Procesando TopSecretSplit con {} satélites almacenados", splitSatellites.size());
        log.debug("Satélites disponibles: {}", splitSatellites.keySet());

        if (splitSatellites.size() < 3) {
            log.warn("Información insuficiente de satélites. Requeridos: 3, Disponibles: {}", splitSatellites.size());
            throw new LocationException("Información insuficiente de satélites.");
        }

        List<SatelliteDTO> list = new ArrayList<>(splitSatellites.values());
        List<Float> distances = new ArrayList<>();
        List<List<String>> messages = new ArrayList<>();
        for (SatelliteDTO sat : list) {
            distances.add(sat.getDistance());
            messages.add(sat.getMessage());
        }

        ResponseDTO response = getResponseDTO(distances, messages);
        log.info("TopSecretSplit procesado exitosamente. Limpiando caché de satélites.");

        // Clear stored satellite data after processing
        splitSatellites.clear();

        return response;
    }

    /**
     * Reconstructs the message from arrays received by satellites.
     *
     * @param messages Arrays of words from satellites.
     * @return The reconstructed message.
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    private String getMessage(List<List<String>> messages) {
        log.debug("Iniciando reconstrucción de mensaje con {} arrays", messages.size());

        int maxLen = messages.stream().mapToInt(List::size).max().orElse(0);
        log.debug("Longitud máxima de mensaje detectada: {}", maxLen);

        String[] result = new String[maxLen];
        for (int i = 0; i < maxLen; i++) {
            for (List<String> msgArr : messages) {
                if (i < msgArr.size() && msgArr.get(i) != null && !msgArr.get(i).isBlank()) {
                    result[i] = msgArr.get(i);
                    log.trace("Posición {} completada con palabra: '{}'", i, result[i]);
                    break;
                }
            }
            if (result[i] == null) result[i] = "";
        }

        String finalMessage = String.join(" ", Arrays.stream(result).filter(s -> !s.isBlank()).toArray(String[]::new));
        log.debug("Mensaje reconstruido: '{}'", finalMessage);

        return finalMessage;
    }
}