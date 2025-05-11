package com.fiap.challenge.quod.antifraude_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.fiap.challenge.quod.antifraude_backend.model.*;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}
