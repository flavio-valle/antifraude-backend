package com.fiap.challenge.quod.antifraude_backend.integration;

import com.fiap.challenge.quod.antifraude_backend.model.Usuario;
import com.fiap.challenge.quod.antifraude_backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BiometriaIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String usuarioId;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        Usuario u = usuarioRepository.save(new Usuario(null,"Bob","bob@example.com","pass",""));
        usuarioId = u.getId();
    }

    @Test
    void facialIntegration_deveRetornar200() throws Exception {
        MockMultipartFile foto = new MockMultipartFile(
                "imagem", "face.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x01}
        );

        mvc.perform(multipart("/api/usuarios/" + usuarioId + "/biometria/facial")
                        .file(foto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void digitalIntegration_deveRetornar200() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "scan.bin",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                new byte[]{0x0A}
        );

        mvc.perform(multipart("/api/usuarios/" + usuarioId + "/biometria/digital")
                        .file(arquivo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
