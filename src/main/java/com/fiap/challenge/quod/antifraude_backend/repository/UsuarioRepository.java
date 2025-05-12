package com.fiap.challenge.quod.antifraude_backend.repository;

import com.fiap.challenge.quod.antifraude_backend.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByToken(String token);
}
