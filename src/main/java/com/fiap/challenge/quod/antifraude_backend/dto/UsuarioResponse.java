package com.fiap.challenge.quod.antifraude_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponse {
    private String id;
    private String name;
    private String email;
    private String token;
}
