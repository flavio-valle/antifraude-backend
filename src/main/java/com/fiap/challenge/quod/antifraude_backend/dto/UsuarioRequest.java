package com.fiap.challenge.quod.antifraude_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UsuarioRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String pass;
}
