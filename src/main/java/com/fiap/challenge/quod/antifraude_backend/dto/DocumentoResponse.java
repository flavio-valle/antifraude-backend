package com.fiap.challenge.quod.antifraude_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentoResponse {
    private boolean valid;
    private String message;
}
