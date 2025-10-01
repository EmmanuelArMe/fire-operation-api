package com.quasar.fireoperation.api.exception;

/**
 * Exception thrown when location cannot be determined.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
public class LocationException extends RuntimeException {
    public LocationException(String message) {
        super(message);
    }
}
