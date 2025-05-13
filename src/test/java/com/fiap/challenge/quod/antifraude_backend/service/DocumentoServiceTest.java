package com.fiap.challenge.quod.antifraude_backend.service;

import com.fiap.challenge.quod.antifraude_backend.dto.DocumentoResponse;
import com.fiap.challenge.quod.antifraude_backend.model.DocumentoRecord;
import com.fiap.challenge.quod.antifraude_backend.repository.DocumentoRepository;
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
class DocumentoServiceTest {

    @Mock
    private DocumentoRepository repo;

    @Mock
    private NotificationService notifier;

    @InjectMocks
    private DocumentoService service;

    @Test
    @DisplayName("Deve retornar inválido e notificar fraude quando documento vazio")
    void validarDocumento_emptyDoc_shouldNotifyFraude() {
        MultipartFile doc = new MockMultipartFile(
                "documento", "doc.pdf", "application/pdf", new byte[0]
        );

        DocumentoResponse resp = service.validarDocumento("user1", "RG", doc, null);

        assertFalse(resp.isValid());
        assertEquals("Foto do documento vazia", resp.getMessage());
        verify(repo).save(argThat(r ->
                !r.isValido()
                        && "Foto do documento vazia".equals(r.getMensagem())
        ));
        verify(notifier).notificarFraude("user1");
    }

    @Test
    @DisplayName("Deve retornar inválido e notificar fraude quando documento >10 MB")
    void validarDocumento_largeDoc_shouldNotifyFraude() {
        MultipartFile doc = mock(MultipartFile.class);
        when(doc.isEmpty()).thenReturn(false);
        when(doc.getSize()).thenReturn(11L * 1024 * 1024);

        DocumentoResponse resp = service.validarDocumento("user2", "CNH", doc, null);

        assertFalse(resp.isValid());
        assertEquals("Documento maior que 10 MB", resp.getMessage());
        verify(repo).save(argThat(r ->
                !r.isValido()
                        && "Documento maior que 10 MB".equals(r.getMensagem())
        ));
        verify(notifier).notificarFraude("user2");
    }

    @Test
    @DisplayName("Deve processar com sucesso e notificar sucesso quando stubDocumentCheck retorna false")
    void validarDocumento_valid_shouldNotifySucessoAndSave() throws Exception {
        byte[] docBytes = {0x01};
        byte[] faceBytes = {0x02};
        MultipartFile doc = new MockMultipartFile(
                "documento", "doc.pdf", "application/pdf", docBytes
        );
        MultipartFile face = new MockMultipartFile(
                "face", "face.jpg", "image/jpeg", faceBytes
        );
        // faz o save simplesmente retornar o record recebido
        when(repo.save(any(DocumentoRecord.class)))
                .thenAnswer(i -> i.getArgument(0));

        DocumentoResponse resp = service.validarDocumento("user3", "RG", doc, face);

        assertTrue(resp.isValid());
        assertEquals("OK", resp.getMessage());
        verify(repo).save(argThat(r ->
                r.isValido()
                        && "OK".equals(r.getMensagem())
        ));
        verify(notifier).notificarSucesso("user3");
    }
}
