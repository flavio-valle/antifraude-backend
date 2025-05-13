package com.fiap.challenge.quod.antifraude_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Notifica o sistema interno sobre um processamento de biometria bem-sucedido.
     */
    public void notificarSucesso(String usuarioId) {
        log.info("✅ [MONITOR] Usuário {} validado com sucesso", usuarioId);
        // TODO: substituir pelo client HTTP (WebClient/RestTemplate) para chamar o endpoint de monitoramento
    }

    /**
     * Notifica o sistema interno sobre uma possível fraude detectada.
     */
    public void notificarFraude(String usuarioId) {
        log.warn("🚨 [MONITOR] Possível fraude detectada para usuário {}", usuarioId);
        // TODO: substituir pelo client HTTP (WebClient/RestTemplate) para chamar o endpoint de monitoramento
    }
}
