package com.fiap.challenge.quod.antifraude_backend.orchestrator;

import com.fiap.challenge.quod.antifraude_backend.dto.BiometriaResponse;
import com.fiap.challenge.quod.antifraude_backend.dto.DocumentoResponse;
import com.fiap.challenge.quod.antifraude_backend.service.BiometriaService;
import com.fiap.challenge.quod.antifraude_backend.service.DocumentoService;
import com.fiap.challenge.quod.antifraude_backend.service.NotificationService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FraudOrchestrator {

    private final BiometriaService biometriaService;
    private final DocumentoService documentoService;
    private final NotificationService notifier = new NotificationService();

    public FraudOrchestrator(BiometriaService biometriaService,
                             DocumentoService documentoService) {
        this.biometriaService = biometriaService;
        this.documentoService = documentoService;
    }

    public BiometriaResponse processarBiometriaFacial(String usuarioId, MultipartFile foto) {
        BiometriaResponse resp = biometriaService.validarBiometriaFacial(usuarioId, foto);
        notificar(resp.isValid(), usuarioId);
        return resp;
    }

    public BiometriaResponse processarBiometriaDigital(String usuarioId, MultipartFile digital) {
        BiometriaResponse resp = biometriaService.validarBiometriaDigital(usuarioId, digital);
        notificar(resp.isValid(), usuarioId);
        return resp;
    }

    public DocumentoResponse processarDocumento(String usuarioId,
                                                String tipoDocumento,
                                                MultipartFile docFoto,
                                                MultipartFile faceFoto) {
        DocumentoResponse resp = documentoService.validarDocumento(
                usuarioId, tipoDocumento, docFoto, faceFoto);
        notificar(resp.isValid(), usuarioId);
        return resp;
    }

    private void notificar(boolean sucesso, String usuarioId) {
        if (sucesso) notifier.notificarSucesso(usuarioId);
        else notifier.notificarFraude(usuarioId);
    }
}
