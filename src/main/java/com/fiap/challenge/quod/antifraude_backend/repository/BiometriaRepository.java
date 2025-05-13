package com.fiap.challenge.quod.antifraude_backend.repository;

import com.fiap.challenge.quod.antifraude_backend.model.BiometriaRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BiometriaRepository extends MongoRepository<BiometriaRecord, String> {
    List<BiometriaRecord> findByUsuarioId(String usuarioId);
}
