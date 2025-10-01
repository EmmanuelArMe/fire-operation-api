package com.quasar.fireoperation.api.business;

import com.quasar.fireoperation.api.domain.general.TopSecretRequestDTO;
import com.quasar.fireoperation.api.domain.general.ResponseDTO;

/**
 * Port interface for business logic to resolve position and message.
 *
 * @version 1.0
 * @since 2025
 * @author Emmanuel Arenilla (emmanueldevtest01@gmail.com)
 */
public interface MessageBusiness {
    ResponseDTO processTopSecret(TopSecretRequestDTO request);
    ResponseDTO processTopSecretSplit();
    void saveSatelliteSplit(String name, float distance, java.util.List<String> message);
}
