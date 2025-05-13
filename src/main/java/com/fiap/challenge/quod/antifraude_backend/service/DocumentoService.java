package com.fiap.challenge.quod.antifraude_backend.service;

import com.fiap.challenge.quod.antifraude_backend.model.DocumentoRecord;
import com.fiap.challenge.quod.antifraude_backend.dto.DocumentoResponse;
import com.fiap.challenge.quod.antifraude_backend.repository.DocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

@Service
public class DocumentoService {

    private final DocumentoRepository repo;
    private final NotificationService notifier;

    public DocumentoService(DocumentoRepository repo, NotificationService notifier) {
        this.repo     = repo;
        this.notifier = notifier;
    }

    public DocumentoResponse validarDocumento(String usuarioId,
                                              String tipoDocumento,
                                              MultipartFile docFoto,
                                              MultipartFile faceFoto) {

        if (!"RG".equalsIgnoreCase(tipoDocumento) && !"CNH".equalsIgnoreCase(tipoDocumento)) {
            return gravarENotificar(usuarioId, tipoDocumento, false, "Tipo de documento inválido");
        }

        // 1) validações básicas
        if (docFoto.isEmpty()) {
            return gravarENotificar(usuarioId, tipoDocumento, false, "Foto do documento vazia");
        }
        if (docFoto.getSize() > 10 * 1024 * 1024) {
            return gravarENotificar(usuarioId, tipoDocumento, false, "Documento maior que 10 MB");
        }
        // faceFoto pode ser opcional, se null skip

        // 2) stub de check de autenticidade (OCR, assinatura)
        boolean fraude = stubDocumentCheck(docFoto, faceFoto);

        // 3) conversão Base64
        String docBase64, faceBase64 = null;
        try {
            docBase64  = Base64.getEncoder().encodeToString(docFoto.getBytes());
            if (faceFoto != null && !faceFoto.isEmpty()) {
                faceBase64 = Base64.getEncoder().encodeToString(faceFoto.getBytes());
            }
        } catch (IOException e) {
            return gravarENotificar(usuarioId, tipoDocumento, false, "Erro ao ler arquivos");
        }

        // 4) persistência
        DocumentoRecord record = DocumentoRecord.builder()
                .usuarioId(usuarioId)
                .tipoDocumento(tipoDocumento)
                .documentoBase64(docBase64)
                .faceBase64(faceBase64)
                .dataCaptura(Instant.now())
                .valido(!fraude)
                .mensagem(fraude ? "Documento inválido" : "OK")
                .build();
        repo.save(record);

        // 5) notificação
        if (fraude) notifier.notificarFraude(usuarioId);
        else       notifier.notificarSucesso(usuarioId);

        return new DocumentoResponse(!fraude, record.getMensagem());
    }

    private boolean stubDocumentCheck(MultipartFile doc, MultipartFile face) {
        return false;
    }

    private DocumentoResponse gravarENotificar(String usuarioId,
                                               String tipoDocumento,
                                               boolean valido,
                                               String motivo) {
        DocumentoRecord rec = DocumentoRecord.builder()
                .usuarioId(usuarioId)
                .tipoDocumento(tipoDocumento)
                .documentoBase64(null)
                .faceBase64(null)
                .dataCaptura(Instant.now())
                .valido(valido)
                .mensagem(motivo)
                .build();
        repo.save(rec);

        if (valido) notifier.notificarSucesso(usuarioId);
        else       notifier.notificarFraude(usuarioId);

        return new DocumentoResponse(valido, motivo);
    }
}
