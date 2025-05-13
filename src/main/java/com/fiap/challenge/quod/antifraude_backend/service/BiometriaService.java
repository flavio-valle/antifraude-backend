package com.fiap.challenge.quod.antifraude_backend.service;

import com.fiap.challenge.quod.antifraude_backend.model.BiometriaRecord;
import com.fiap.challenge.quod.antifraude_backend.model.BiometriaRecord.TipoBiometria;
import com.fiap.challenge.quod.antifraude_backend.dto.BiometriaResponse;
import com.fiap.challenge.quod.antifraude_backend.repository.BiometriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

@Service
public class BiometriaService {

    private final BiometriaRepository repo;
    private final NotificationService notifier;

    public BiometriaService(BiometriaRepository repo,
                            NotificationService notifier) {
        this.repo      = repo;
        this.notifier  = notifier;
    }

    public BiometriaResponse validarBiometriaFacial(String usuarioId, MultipartFile foto) {
        return processar(usuarioId, foto, TipoBiometria.FACIAL);
    }

    public BiometriaResponse validarBiometriaDigital(String usuarioId, MultipartFile digital) {
        return processar(usuarioId, digital, TipoBiometria.DIGITAL);
    }

    private BiometriaResponse processar(String usuarioId,
                                        MultipartFile arquivo,
                                        TipoBiometria tipo) {
        // 1) validações básicas
        if (arquivo.isEmpty()) {
            return gravarENotificar(usuarioId, tipo, false, "Arquivo vazio");
        }
        if (arquivo.getSize() > 5 * 1024 * 1024) {
            return gravarENotificar(usuarioId, tipo, false, "Arquivo maior que 5 MB");
        }

        // 2) stub de detecção de fraudes
        boolean fraude = stubDeepfakeCheck(arquivo);

        // 3) conversão Base64 e persistência
        String base64;
        try {
            base64 = Base64.getEncoder().encodeToString(arquivo.getBytes());
        } catch (IOException e) {
            return gravarENotificar(usuarioId, tipo, false, "Erro ao ler arquivo");
        }

        BiometriaRecord record = BiometriaRecord.builder()
                .usuarioId(usuarioId)
                .tipo(tipo)
                .templateBase64(base64)
                .dataCaptura(Instant.now())
                .valido(!fraude)
                .mensagem(fraude
                        ? "Fraude detectada (" + tipo + ")"
                        : "OK")
                .build();

        repo.save(record);

        // 4) notificação
        if (fraude) {
            notifier.notificarFraude(usuarioId);
        } else {
            notifier.notificarSucesso(usuarioId);
        }

        // 5) Response
        return new BiometriaResponse(!fraude, record.getMensagem());
    }

    private boolean stubDeepfakeCheck(MultipartFile arquivo) {
        return false;
    }

    private BiometriaResponse gravarENotificar(String usuarioId,
                                               TipoBiometria tipo,
                                               boolean valido,
                                               String motivo) {
        BiometriaRecord record = new BiometriaRecord();
        record.setUsuarioId(usuarioId);
        record.setTipo(tipo);
        record.setTemplateBase64(null);
        record.setDataCaptura(Instant.now());
        record.setValido(valido);
        record.setMensagem(motivo);
        repo.save(record);

        if (valido) {
            notifier.notificarSucesso(usuarioId);
        } else {
            notifier.notificarFraude(usuarioId);
        }
        return new BiometriaResponse(valido, motivo);
    }
}
