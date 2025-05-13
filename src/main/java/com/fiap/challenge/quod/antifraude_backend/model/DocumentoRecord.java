package com.fiap.challenge.quod.antifraude_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "documentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoRecord {

    @Id
    private String id;

    private String usuarioId;

    // tipo de documento (ex: RG, CNH, PASSAPORTE…)
    private String tipoDocumento;

    // foto do documento (template/base64)
    private String documentoBase64;

    // foto de face para conferência cruzada (se houver)
    private String faceBase64;

    private Instant dataCaptura;

    // true → passou nos checks
    private boolean valido;

    // “OK” ou razão da recusa (assinatura inválida, OCR falhou…)
    private String mensagem;
}
