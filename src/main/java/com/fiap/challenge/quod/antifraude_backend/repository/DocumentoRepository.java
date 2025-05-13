package com.fiap.challenge.quod.antifraude_backend.repository;

import com.fiap.challenge.quod.antifraude_backend.model.DocumentoRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentoRepository extends MongoRepository<DocumentoRecord, String> {
    List<DocumentoRecord> findByUsuarioId(String usuarioId);
}
