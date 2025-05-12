package com.fiap.challenge.quod.antifraude_backend.dto;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}