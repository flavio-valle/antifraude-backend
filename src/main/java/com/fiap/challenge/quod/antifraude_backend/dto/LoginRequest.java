package com.fiap.challenge.quod.antifraude_backend.dto;
import lombok.Data;

// Login
@Data
public class LoginRequest {
    private String email;
    private String pass;
}
