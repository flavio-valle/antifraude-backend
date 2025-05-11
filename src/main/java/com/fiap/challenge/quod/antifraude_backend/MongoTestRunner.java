package com.fiap.challenge.quod.antifraude_backend;

import com.fiap.challenge.quod.antifraude_backend.model.Usuario;
import com.fiap.challenge.quod.antifraude_backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class MongoTestRunner implements CommandLineRunner {

    private final UsuarioRepository repo;

    public MongoTestRunner(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        List<Usuario> todos = repo.findAll();
        System.out.println(">>> Usuários no Atlas:");
        todos.forEach(u -> System.out.println(u.getName() + " – " + u.getEmail()));
    }
}
