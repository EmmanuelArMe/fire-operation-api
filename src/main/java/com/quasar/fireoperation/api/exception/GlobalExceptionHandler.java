package com.quasar.fireoperation.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for API.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle LocationException and MessageException.
     *
     * @param ex The exception thrown
     * @return ResponseEntity with error message and NOT_FOUND status
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @ExceptionHandler({LocationException.class, MessageException.class})
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        log.warn("Error de negocio capturado: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        log.debug("Stack trace del error de negocio:", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handle IllegalArgumentException.
     *
     * @param ex The exception thrown
     * @return ResponseEntity with error message and BAD_REQUEST status
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Argumento inválido recibido: {}", ex.getMessage());
        log.debug("Stack trace del argumento inválido:", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handle malformed JSON requests.
     *
     * @param ex The exception thrown
     * @return ResponseEntity with error message and BAD_REQUEST status
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMalformedJson(HttpMessageNotReadableException ex) {
        log.warn("JSON malformado recibido: {}", ex.getMessage());
        log.debug("Stack trace del JSON malformado:", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JSON malformado en la solicitud");
    }

    /**
     * Handle unsupported media type requests.
     *
     * @param ex The exception thrown
     * @return ResponseEntity with error message and UNSUPPORTED_MEDIA_TYPE status
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        log.warn("Tipo de contenido no soportado: {}", ex.getMessage());
        log.debug("Stack trace del tipo de contenido no soportado:", ex);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body("Tipo de contenido no soportado. Se requiere application/json");
    }

    /**
     * Handle all other uncaught exceptions.
     *
     * @param ex The exception thrown
     * @return ResponseEntity with generic error message and INTERNAL_SERVER_ERROR status
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception ex) {
        log.error("Error interno no controlado: {} - {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}