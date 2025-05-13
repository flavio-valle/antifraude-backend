package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fiap.challenge.quod.antifraude_backend.dto.BiometriaResponse;
import com.fiap.challenge.quod.antifraude_backend.orchestrator.FraudOrchestrator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios/{id}/biometria")
public class BiometriaController {

    private final FraudOrchestrator orchestrator;

    public BiometriaController(FraudOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping(value = "/facial",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BiometriaResponse> facial(
            @PathVariable("id") String id,
            @RequestParam("imagem") MultipartFile foto) {

        BiometriaResponse resp = orchestrator.processarBiometriaFacial(id, foto);
        return ResponseEntity
                .status(resp.isValid() ? 200 : 422)
                .body(resp);
    }

    @PostMapping(value = "/digital",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BiometriaResponse> digital(
            @PathVariable("id") String id,
            @RequestParam("arquivo") MultipartFile digital) {

        BiometriaResponse resp = orchestrator.processarBiometriaDigital(id, digital);
        return ResponseEntity
                .status(resp.isValid() ? 200 : 422)
                .body(resp);
    }
}
