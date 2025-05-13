package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fiap.challenge.quod.antifraude_backend.dto.DocumentoResponse;
import com.fiap.challenge.quod.antifraude_backend.service.DocumentoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios/{id}/documentos")
@Validated
public class DocumentoController {

    private final DocumentoService service;

    public DocumentoController(DocumentoService service) {
        this.service = service;
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DocumentoResponse> validar(
            @PathVariable("id") String usuarioId,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam("documento") MultipartFile docFoto,
            @RequestParam(value = "face", required = false) MultipartFile faceFoto
    ) {
        DocumentoResponse resp = service.validarDocumento(usuarioId, tipoDocumento, docFoto, faceFoto);

        if (resp.isValid()) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.unprocessableEntity().body(resp);
        }
    }
}
