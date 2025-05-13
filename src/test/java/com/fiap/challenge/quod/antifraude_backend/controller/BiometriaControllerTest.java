package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fiap.challenge.quod.antifraude_backend.dto.BiometriaResponse;
import com.fiap.challenge.quod.antifraude_backend.filter.TokenAuthenticationFilter;
import com.fiap.challenge.quod.antifraude_backend.orchestrator.FraudOrchestrator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BiometriaController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class BiometriaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FraudOrchestrator orchestrator;

    @MockitoBean
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Test
    @DisplayName("POST /api/usuarios/{id}/biometria/facial → 200 OK")
    void facial_validShouldReturn200() throws Exception {
        when(orchestrator.processarBiometriaFacial(eq("1"), any(MultipartFile.class)))
                .thenReturn(new BiometriaResponse(true, "OK"));

        MockMultipartFile foto = new MockMultipartFile(
                "imagem", "face.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x01, 0x02}
        );

        mvc.perform(multipart("/api/usuarios/1/biometria/facial")
                        .file(foto))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("POST /api/usuarios/{id}/biometria/facial → 422 Unprocessable Entity")
    void facial_invalidShouldReturn422() throws Exception {
        when(orchestrator.processarBiometriaFacial(eq("1"), any(MultipartFile.class)))
                .thenReturn(new BiometriaResponse(false, "Arquivo vazio"));

        MockMultipartFile foto = new MockMultipartFile(
                "imagem", "empty.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        mvc.perform(multipart("/api/usuarios/1/biometria/facial")
                        .file(foto))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.message").value("Arquivo vazio"));
    }

    @Test
    @DisplayName("POST /api/usuarios/{id}/biometria/digital → 200 OK")
    void digital_validShouldReturn200() throws Exception {
        when(orchestrator.processarBiometriaDigital(eq("1"), any(MultipartFile.class)))
                .thenReturn(new BiometriaResponse(true, "OK"));

        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "scan.bin",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                new byte[]{0x0A}
        );

        mvc.perform(multipart("/api/usuarios/1/biometria/digital")
                        .file(arquivo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    @DisplayName("POST /api/usuarios/{id}/biometria/digital → 422 Unprocessable Entity")
    void digital_invalidShouldReturn422() throws Exception {
        when(orchestrator.processarBiometriaDigital(eq("1"), any(MultipartFile.class)))
                .thenReturn(new BiometriaResponse(false, "Arquivo vazio"));

        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "empty.bin",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                new byte[0]
        );

        mvc.perform(multipart("/api/usuarios/1/biometria/digital")
                        .file(arquivo))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.message").value("Arquivo vazio"));
    }
}
