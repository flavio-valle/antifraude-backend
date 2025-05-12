package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fiap.challenge.quod.antifraude_backend.dto.UsuarioRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.UsuarioResponse;
import com.fiap.challenge.quod.antifraude_backend.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable String id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(@Valid @RequestBody UsuarioRequest req) {
        return service.criar(req);
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(
            @PathVariable String id,
            @Valid @RequestBody UsuarioRequest req
    ) {
        return service.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable String id) {
        service.deletar(id);
    }
}

