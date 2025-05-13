package com.fiap.challenge.quod.antifraude_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "biometrias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BiometriaRecord {

    @Id
    private String id;

    private String usuarioId;
    private TipoBiometria tipo;            // FACIAL ou DIGITAL
    private String templateBase64;         // imagem ou digital em Base64
    private Instant dataCaptura;
    private boolean valido;                // true → passou nos checks
    private String mensagem;               // “OK” ou motivo da recusa

    public enum TipoBiometria {
        FACIAL,
        DIGITAL
    }
}
