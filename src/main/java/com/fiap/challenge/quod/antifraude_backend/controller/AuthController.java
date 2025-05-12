package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fiap.challenge.quod.antifraude_backend.dto.LoginRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.LoginResponse;
import com.fiap.challenge.quod.antifraude_backend.dto.RegisterRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.RegisterResponse;
import com.fiap.challenge.quod.antifraude_backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest req) {
        return auth.register(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return auth.login(req);
    }
}
