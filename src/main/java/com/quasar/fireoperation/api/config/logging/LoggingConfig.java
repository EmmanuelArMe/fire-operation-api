package com.quasar.fireoperation.api.config.logging;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for application logging and startup information.
 * <p>
 * This class handles application lifecycle events and provides detailed
 * logging information about the application startup and configuration.
 * </p>
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingConfig {

    private final Environment environment;

    /**
     * Event listener that executes when the application is fully started and ready.
     * Logs important application information including active profiles, server port,
     * and available endpoints.
     *
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        String port = environment.getProperty("server.port", "8080");
        String[] activeProfiles = environment.getActiveProfiles();

        log.info("========== QUASAR FIRE OPERATION API - SISTEMA LISTO ==========");
        log.info("Servidor ejecutándose en: http://localhost:{}{}", port, contextPath);
        log.info("Perfiles activos: {}", activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default");
        log.info("Endpoints disponibles:");
        log.info("POST {}/top-secret - Procesamiento simultáneo de 3 satélites", contextPath);
        log.info("POST {}/top-secret-split/{{satellite}} - Procesamiento individual", contextPath);
        log.info("GET  {}/top-secret-split - Obtener resultado procesado", contextPath);
        log.info("Documentación Swagger: http://localhost:{}{}/swagger-ui.html", port, contextPath);
        log.info("Satélites configurados: Kenobi (-500,-200), Skywalker (100,-100), Sato (500,100)");
        log.info("¡Que la fuerza te acompañe en las operaciones de rescate!");
        log.info("================================================================");
    }
}
