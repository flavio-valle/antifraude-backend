package com.fiap.challenge.quod.antifraude_backend.service;

import com.fiap.challenge.quod.antifraude_backend.dto.LoginRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.LoginResponse;
import com.fiap.challenge.quod.antifraude_backend.dto.RegisterRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.RegisterResponse;
import com.fiap.challenge.quod.antifraude_backend.model.*;
import com.fiap.challenge.quod.antifraude_backend.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public RegisterResponse register(RegisterRequest req) {
        repo.findByEmail(req.getEmail()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        });

        var user = new Usuario();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPass(encoder.encode(req.getPass()));
        // token vazio até login
        user.setToken(null);

        var saved = repo.save(user);
        return new RegisterResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    public LoginResponse login(LoginRequest req) {
        var user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (!encoder.matches(req.getPass(), user.getPass())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        repo.save(user);

        return new LoginResponse(token);
    }
}
