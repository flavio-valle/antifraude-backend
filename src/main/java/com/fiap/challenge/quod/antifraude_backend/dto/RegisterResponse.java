package com.fiap.challenge.quod.antifraude_backend.dto;

import lombok.Data;

@Data
public class RegisterResponse {
    private String id;
    private String name;
    private String email;

    public RegisterResponse(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
