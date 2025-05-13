package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.challenge.quod.antifraude_backend.dto.UsuarioRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.UsuarioResponse;
import com.fiap.challenge.quod.antifraude_backend.repository.UsuarioRepository;
import com.fiap.challenge.quod.antifraude_backend.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)

class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("GET /api/usuarios → lista todos")
    void listarTodos_deveRetornarLista() throws Exception {
        var u1 = new UsuarioResponse("1", "Alice", "alice@x.com", "");
        var u2 = new UsuarioResponse("2", "Bob",   "bob@y.com",   "");
        when(service.listarTodos()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].name").value("Bob"));

        verify(service, times(1)).listarTodos();
    }

    @Test
    @DisplayName("POST /api/usuarios → cria novo usuário")
    void criar_deveRetornarCreated() throws Exception {
        var req = new UsuarioRequest();
        req.setName("Carol");
        req.setEmail("carol@z.com");
        req.setPass("senha");

        var resp = new UsuarioResponse("42", "Carol", "carol@z.com", "");
        when(service.criar(req)).thenReturn(resp);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("42"))
                .andExpect(jsonPath("$.name").value("Carol"));

        verify(service, times(1)).criar(req);
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} → atualiza usuário")
    void atualizar_deveRetornarOk() throws Exception {
        var req = new UsuarioRequest();
        req.setName("Carol Jr");
        req.setEmail("caroljr@z.com");
        req.setPass("nova");

        var resp = new UsuarioResponse("42", "Carol Jr", "caroljr@z.com", "");
        when(service.atualizar(eq("42"), any())).thenReturn(resp);

        mockMvc.perform(put("/api/usuarios/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("caroljr@z.com"));

        verify(service, times(1)).atualizar(eq("42"), any());
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} → remove usuário")
    void remover_deveRetornarNoContent() throws Exception {
        doNothing().when(service).deletar("42");

        mockMvc.perform(delete("/api/usuarios/42"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deletar("42");
    }
}
