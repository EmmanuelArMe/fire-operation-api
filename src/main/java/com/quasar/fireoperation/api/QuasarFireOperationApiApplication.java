package com.quasar.fireoperation.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

/**
 * Main Spring Boot Application class for Quasar Fire Operation API.
 * This class serves as the entry point for the Spring Boot application.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
@SpringBootApplication
@Slf4j
public class QuasarFireOperationApiApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     * @since 2025
     * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
     */
    public static void main(String[] args) {
        log.info("Iniciando Quasar Fire Operation API...");
        log.info("Configurando red de satélites rebeldes (Kenobi, Skywalker, Sato)");

        try {
            SpringApplication.run(QuasarFireOperationApiApplication.class, args);
            log.info("Quasar Fire Operation API iniciada exitosamente");
            log.info("Sistema listo para operaciones de rescate espacial");
        } catch (Exception ex) {
            log.error("Error crítico al iniciar la aplicación: {}", ex.getMessage(), ex);
            System.exit(1);
        }
    }
}