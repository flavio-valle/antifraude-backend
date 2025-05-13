package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.challenge.quod.antifraude_backend.dto.LoginRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.LoginResponse;
import com.fiap.challenge.quod.antifraude_backend.dto.RegisterRequest;
import com.fiap.challenge.quod.antifraude_backend.dto.RegisterResponse;
import com.fiap.challenge.quod.antifraude_backend.repository.UsuarioRepository;
import com.fiap.challenge.quod.antifraude_backend.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("POST /api/auth/register → registra usuário")
    void register_deveRetornarCreated() throws Exception {
        var req  = new RegisterRequest();
        req.setName("Zé");
        req.setEmail("ze@x.com");
        req.setPass("1234");

        var resp = new RegisterResponse("abc", "Zé", "ze@x.com");
        when(authService.register(req)).thenReturn(resp);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc"))
                .andExpect(jsonPath("$.email").value("ze@x.com"));

        verify(authService, times(1)).register(req);
    }

    @Test
    @DisplayName("POST /api/auth/login → autentica usuário")
    void login_deveRetornarOk() throws Exception {
        var req  = new LoginRequest();
        req.setEmail("ze@x.com");
        req.setPass("1234");

        var resp = new LoginResponse("tok-123");
        when(authService.login(req)).thenReturn(resp);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tok-123"));

        verify(authService, times(1)).login(req);
    }
}
