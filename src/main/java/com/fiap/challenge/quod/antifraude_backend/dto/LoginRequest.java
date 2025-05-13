package com.fiap.challenge.quod.antifraude_backend.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Login
@Data
public class LoginRequest {
    @NotBlank private String email;
    @NotBlank private String pass;
}
