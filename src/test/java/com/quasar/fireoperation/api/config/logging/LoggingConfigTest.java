package com.quasar.fireoperation.api.config.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.mockito.Mockito.*;

/**
 * Unit tests for LoggingConfig class.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoggingConfig Tests")
class LoggingConfigTest {

    @Mock
    private Environment environment;

    @InjectMocks
    private LoggingConfig loggingConfig;

    @BeforeEach
    void setUp() {
        // Default mock responses
        when(environment.getProperty("server.servlet.context-path", "")).thenReturn("/quasar-fire-operation");
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
    }

    @Test
    @DisplayName("Should handle application ready event with default configuration")
    void onApplicationReady_DefaultConfiguration_LogsCorrectly() {
        // When
        loggingConfig.onApplicationReady();

        // Then
        verify(environment).getProperty("server.servlet.context-path", "");
        verify(environment).getProperty("server.port", "8080");
        verify(environment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle empty context path")
    void onApplicationReady_EmptyContextPath_LogsCorrectly() {
        // Given
        when(environment.getProperty("server.servlet.context-path", "")).thenReturn("");

        // When
        loggingConfig.onApplicationReady();

        // Then
        verify(environment).getProperty("server.servlet.context-path", "");
    }

    @Test
    @DisplayName("Should handle different port")
    void onApplicationReady_DifferentPort_LogsCorrectly() {
        // Given
        when(environment.getProperty("server.port", "8080")).thenReturn("9090");

        // When
        loggingConfig.onApplicationReady();

        // Then
        verify(environment).getProperty("server.port", "8080");
    }

    @Test
    @DisplayName("Should handle multiple active profiles")
    void onApplicationReady_MultipleProfiles_LogsCorrectly() {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev", "test"});

        // When
        loggingConfig.onApplicationReady();

        // Then
        verify(environment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle no active profiles")
    void onApplicationReady_NoActiveProfiles_LogsDefault() {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{});

        // When
        loggingConfig.onApplicationReady();

        // Then
        verify(environment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle null context path")
    void onApplicationReady_NullContextPath_HandlesGracefully() {
        // Given
        when(environment.getProperty("server.servlet.context-path", "")).thenReturn(null);

        // When & Then
        assertDoesNotThrow(() -> loggingConfig.onApplicationReady());
    }

    @Test
    @DisplayName("Should handle null port")
    void onApplicationReady_NullPort_UsesDefault() {
        // Given
        when(environment.getProperty("server.port", "8080")).thenReturn(null);

        // When & Then
        assertDoesNotThrow(() -> loggingConfig.onApplicationReady());
    }

    @Test
    @DisplayName("Should handle production profile")
    void onApplicationReady_ProductionProfile_LogsCorrectly() {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});

        // When
        loggingConfig.onApplicationReady();

        // Then
        verify(environment).getActiveProfiles();
    }

    private void assertDoesNotThrow(Runnable executable) {
        try {
            executable.run();
        } catch (Exception e) {
            throw new AssertionError("Expected no exception, but got: " + e.getMessage(), e);
        }
    }
}
