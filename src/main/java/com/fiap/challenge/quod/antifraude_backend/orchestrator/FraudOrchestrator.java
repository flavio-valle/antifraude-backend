package com.fiap.challenge.quod.antifraude_backend.orchestrator;

import com.fiap.challenge.quod.antifraude_backend.dto.BiometriaResponse;
import com.fiap.challenge.quod.antifraude_backend.dto.DocumentoResponse;
import com.fiap.challenge.quod.antifraude_backend.dto.FraudEvent;
import com.fiap.challenge.quod.antifraude_backend.messaging.KafkaEventPublisher;
import com.fiap.challenge.quod.antifraude_backend.service.BiometriaService;
import com.fiap.challenge.quod.antifraude_backend.service.DocumentoService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Component
public class FraudOrchestrator {

    private final BiometriaService biometriaService;
    private final DocumentoService documentoService;
    private final KafkaEventPublisher publisher;

    public FraudOrchestrator(BiometriaService biometriaService,
                             DocumentoService documentoService,
                             KafkaEventPublisher publisher) {
        this.biometriaService = biometriaService;
        this.documentoService = documentoService;
        this.publisher = publisher;
    }

    public BiometriaResponse processarBiometriaFacial(String usuarioId, MultipartFile foto) {
        BiometriaResponse resp = biometriaService.validarBiometriaFacial(usuarioId, foto);
        FraudEvent evt = new FraudEvent(
                usuarioId,
                resp.isValid() ? "SUCESSO" : "FRAUDE",
                "FACIAL",
                Instant.now()
        );
        publisher.publish(evt);
        return resp;
    }

    public BiometriaResponse processarBiometriaDigital(String usuarioId, MultipartFile digital) {
        BiometriaResponse resp = biometriaService.validarBiometriaDigital(usuarioId, digital);
        FraudEvent evt = new FraudEvent(
                usuarioId,
                resp.isValid() ? "SUCESSO" : "FRAUDE",
                "FACIAL",
                Instant.now()
        );
        publisher.publish(evt);
        return resp;
    }

    public DocumentoResponse processarDocumento(String usuarioId,
                                                String tipoDocumento,
                                                MultipartFile docFoto,
                                                MultipartFile faceFoto) {
        DocumentoResponse resp = documentoService.validarDocumento(
                usuarioId, tipoDocumento, docFoto, faceFoto);
        FraudEvent evt = new FraudEvent(
                usuarioId,
                resp.isValid() ? "SUCESSO" : "FRAUDE",
                "FACIAL",
                Instant.now()
        );
        publisher.publish(evt);
        return resp;
    }
}
