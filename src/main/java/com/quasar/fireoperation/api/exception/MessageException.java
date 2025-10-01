package com.quasar.fireoperation.api.exception;

/**
 * Exception thrown when message cannot be reconstructed.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
public class MessageException extends RuntimeException {
    public MessageException(String message) {
        super(message);
    }
}
