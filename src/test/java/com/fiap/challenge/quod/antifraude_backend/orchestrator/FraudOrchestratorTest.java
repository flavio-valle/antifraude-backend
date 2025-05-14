package com.fiap.challenge.quod.antifraude_backend.orchestrator;

import com.fiap.challenge.quod.antifraude_backend.dto.BiometriaResponse;
import com.fiap.challenge.quod.antifraude_backend.dto.DocumentoResponse;
import com.fiap.challenge.quod.antifraude_backend.messaging.KafkaEventPublisher;
import com.fiap.challenge.quod.antifraude_backend.service.BiometriaService;
import com.fiap.challenge.quod.antifraude_backend.service.DocumentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FraudOrchestratorTest {

    @Mock
    private BiometriaService biometriaService;

    @Mock
    private DocumentoService documentoService;

    @Mock               // ➜ adiciona isto
    private KafkaEventPublisher publisher;

    @InjectMocks
    private FraudOrchestrator orchestrator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("processarBiometriaFacial deve chamar BiometriaService e devolver resposta")
    void processarBiometriaFacial_delegatesToService() {
        MultipartFile foto = mock(MultipartFile.class);
        BiometriaResponse esperado = new BiometriaResponse(true, "OK facial");
        when(biometriaService.validarBiometriaFacial("user1", foto)).thenReturn(esperado);

        BiometriaResponse resultado = orchestrator.processarBiometriaFacial("user1", foto);

        assertSame(esperado, resultado);
        verify(biometriaService, times(1)).validarBiometriaFacial("user1", foto);
    }

    @Test
    @DisplayName("processarBiometriaDigital deve chamar BiometriaService e devolver resposta")
    void processarBiometriaDigital_delegatesToService() {
        MultipartFile digital = mock(MultipartFile.class);
        BiometriaResponse esperado = new BiometriaResponse(false, "Arquivo vazio");
        when(biometriaService.validarBiometriaDigital("userX", digital)).thenReturn(esperado);

        BiometriaResponse resultado = orchestrator.processarBiometriaDigital("userX", digital);

        assertSame(esperado, resultado);
        verify(biometriaService, times(1)).validarBiometriaDigital("userX", digital);
    }

    @Test
    @DisplayName("processarDocumento deve chamar DocumentoService e devolver resposta")
    void processarDocumento_delegatesToService() {
        MultipartFile doc = mock(MultipartFile.class);
        MultipartFile face = mock(MultipartFile.class);
        DocumentoResponse esperado = new DocumentoResponse(true, "OK doc");
        when(documentoService.validarDocumento("u42", "RG", doc, face)).thenReturn(esperado);

        DocumentoResponse resultado = orchestrator.processarDocumento("u42", "RG", doc, face);

        assertSame(esperado, resultado);
        verify(documentoService, times(1))
                .validarDocumento("u42", "RG", doc, face);
    }
}
