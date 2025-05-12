package com.fiap.challenge.quod.antifraude_backend.service;

import com.fiap.challenge.quod.antifraude_backend.dto.UsuarioRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.UsuarioResponse;
import com.fiap.challenge.quod.antifraude_backend.model.Usuario;
import com.fiap.challenge.quod.antifraude_backend.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<UsuarioResponse> listarTodos() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(String id) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return toResponse(u);
    }

    public UsuarioResponse criar(UsuarioRequest req) {
        Usuario u = new Usuario();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPass(req.getPass());
        u.setToken(null);
        Usuario salvo = repo.save(u);
        return toResponse(salvo);
    }

    public UsuarioResponse atualizar(String id, UsuarioRequest req) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existente.setName(req.getName());
        existente.setEmail(req.getEmail());
        existente.setPass(req.getPass());
        Usuario saved = repo.save(existente);
        return toResponse(saved);
    }

    public void deletar(String id) {
        repo.delete(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getToken()
        );
    }
}
