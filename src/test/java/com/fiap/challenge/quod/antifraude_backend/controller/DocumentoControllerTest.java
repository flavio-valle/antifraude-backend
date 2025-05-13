package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fiap.challenge.quod.antifraude_backend.dto.DocumentoResponse;
import com.fiap.challenge.quod.antifraude_backend.filter.TokenAuthenticationFilter;
import com.fiap.challenge.quod.antifraude_backend.service.DocumentoService;
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
        controllers = DocumentoController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class DocumentoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private DocumentoService service;

    @MockitoBean
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Test
    @DisplayName("POST /api/usuarios/{id}/documentos → 200 OK")
    void documento_validShouldReturn200() throws Exception {
        // Stub passa a usar any(MultipartFile.class)
        when(service.validarDocumento(
                eq("42"),
                eq("RG"),
                any(MultipartFile.class),
                any(MultipartFile.class))
        ).thenReturn(new DocumentoResponse(true, "OK"));

        MockMultipartFile doc = new MockMultipartFile(
                "documento", "doc.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                new byte[]{0x01}
        );
        MockMultipartFile face = new MockMultipartFile(
                "face", "face.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x02}
        );

        mvc.perform(multipart("/api/usuarios/42/documentos")
                        .file(doc)
                        .file(face)
                        .param("tipoDocumento", "RG")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("POST /api/usuarios/{id}/documentos → 422 Unprocessable Entity")
    void documento_invalidShouldReturn422() throws Exception {
        when(service.validarDocumento(
                eq("42"),
                eq("CNH"),
                any(MultipartFile.class),
                any(MultipartFile.class))
        ).thenReturn(new DocumentoResponse(false, "OCR falhou"));

        MockMultipartFile doc = new MockMultipartFile(
                "documento", "doc.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                new byte[]{0x01}
        );
        MockMultipartFile face = new MockMultipartFile(
                "face", "face.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x02}
        );

        mvc.perform(multipart("/api/usuarios/42/documentos")
                        .file(doc)
                        .file(face)
                        .param("tipoDocumento", "CNH")
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.message").value("OCR falhou"));
    }
}
