package com.fiap.challenge.quod.antifraude_backend.service;

import com.fiap.challenge.quod.antifraude_backend.dto.BiometriaResponse;
import com.fiap.challenge.quod.antifraude_backend.model.BiometriaRecord;
import com.fiap.challenge.quod.antifraude_backend.model.BiometriaRecord.TipoBiometria;
import com.fiap.challenge.quod.antifraude_backend.repository.BiometriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BiometriaServiceTest {

    @Mock
    private BiometriaRepository repo;

    @Mock
    private NotificationService notifier;

    @InjectMocks
    private BiometriaService service;

    @Test
    @DisplayName("Facial: deve retornar inválido e notificar fraude quando arquivo vazio")
    void validarFacial_emptyFile_shouldNotifyFraude() {
        MultipartFile file = new MockMultipartFile(
                "imagem", "empty.jpg", "image/jpeg", new byte[0]
        );

        BiometriaResponse resp = service.validarBiometriaFacial("user1", file);

        assertFalse(resp.isValid());
        assertEquals("Arquivo vazio", resp.getMessage());
        verify(repo).save(argThat(r ->
                !r.isValido()
                        && r.getTipo() == TipoBiometria.FACIAL
                        && "Arquivo vazio".equals(r.getMensagem())
        ));
        verify(notifier).notificarFraude("user1");
    }

    @Test
    @DisplayName("Digital: deve retornar inválido e notificar fraude quando >5 MB")
    void validarDigital_largeFile_shouldNotifyFraude() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn(6L * 1024 * 1024);

        BiometriaResponse resp = service.validarBiometriaDigital("user2", file);

        assertFalse(resp.isValid());
        assertEquals("Arquivo maior que 5 MB", resp.getMessage());
        verify(repo).save(argThat(r ->
                !r.isValido()
                        && r.getTipo() == TipoBiometria.DIGITAL
                        && "Arquivo maior que 5 MB".equals(r.getMensagem())
        ));
        verify(notifier).notificarFraude("user2");
    }

    @Test
    @DisplayName("Facial: deve processar com sucesso quando stubDeepfake retorna false")
    void validarFacial_valid_shouldNotifySucessoAndSave() throws Exception {
        byte[] content = {0x01};
        MultipartFile file = new MockMultipartFile(
                "imagem", "face.jpg", "image/jpeg", content
        );
        // faz o save simplesmente retornar o record recebido
        when(repo.save(any(BiometriaRecord.class)))
                .thenAnswer(i -> i.getArgument(0));

        BiometriaResponse resp = service.validarBiometriaFacial("user3", file);

        assertTrue(resp.isValid());
        assertEquals("OK", resp.getMessage());
        verify(repo).save(argThat(r ->
                r.isValido()
                        && r.getTipo() == TipoBiometria.FACIAL
                        && "OK".equals(r.getMensagem())
        ));
        verify(notifier).notificarSucesso("user3");
    }
}
