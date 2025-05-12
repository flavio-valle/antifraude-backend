package com.fiap.challenge.quod.antifraude_backend.dto;

import lombok.Data;

// Registro
@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String pass;
}
