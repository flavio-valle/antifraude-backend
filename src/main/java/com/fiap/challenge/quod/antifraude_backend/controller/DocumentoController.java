package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fiap.challenge.quod.antifraude_backend.dto.DocumentoResponse;
import com.fiap.challenge.quod.antifraude_backend.orchestrator.FraudOrchestrator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios/{id}/documentos")
public class DocumentoController {

    private final FraudOrchestrator orchestrator;

    public DocumentoController(FraudOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DocumentoResponse> validar(
            @PathVariable("id") String id,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam("documento") MultipartFile docFoto,
            @RequestParam(value = "face", required = false) MultipartFile faceFoto
    ) {
        DocumentoResponse resp = orchestrator.processarDocumento(
                id, tipoDocumento, docFoto, faceFoto);
        return ResponseEntity
                .status(resp.isValid() ? 200 : 422)
                .body(resp);
    }
}
