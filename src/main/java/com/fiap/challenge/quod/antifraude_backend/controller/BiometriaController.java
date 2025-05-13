package com.fiap.challenge.quod.antifraude_backend.controller;

import com.fiap.challenge.quod.antifraude_backend.dto.BiometriaResponse;
import com.fiap.challenge.quod.antifraude_backend.service.BiometriaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios/{id}/biometria")
@Validated
public class BiometriaController {

    private final BiometriaService service;

    public BiometriaController(BiometriaService service) {
        this.service = service;
    }

    @PostMapping(value = "/facial", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BiometriaResponse> validarFacial(
            @PathVariable("id") String usuarioId,
            @RequestParam("imagem") MultipartFile imagem
    ) {
        BiometriaResponse resp = service.validarBiometriaFacial(usuarioId, imagem);
        return ResponseEntity
                .status(resp.isValid() ? 200 : 422)
                .body(resp);
    }

    @PostMapping(value = "/digital", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BiometriaResponse> validarDigital(
            @PathVariable("id") String usuarioId,
            @RequestParam("arquivo") MultipartFile arquivo
    ) {
        BiometriaResponse resp = service.validarBiometriaDigital(usuarioId, arquivo);
        return ResponseEntity
                .status(resp.isValid() ? 200 : 422)
                .body(resp);
    }
}
