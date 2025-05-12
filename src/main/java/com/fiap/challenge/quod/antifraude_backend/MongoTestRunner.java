package com.fiap.challenge.quod.antifraude_backend;

import com.fiap.challenge.quod.antifraude_backend.model.Usuario;
import com.fiap.challenge.quod.antifraude_backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MongoTestRunner implements CommandLineRunner {
    private final UsuarioRepository repo;
    private final MongoTemplate mongo;

    public MongoTestRunner(UsuarioRepository repo, MongoTemplate mongo) {
        this.repo = repo;
        this.mongo = mongo;
    }

    @Override
    public void run(String... args) {
        System.out.println(">>> Collections no DB:");
        mongo.getCollectionNames().forEach(System.out::println);

        long total = repo.count();
        System.out.println(">>> Total de usuários (repo.count()): " + total);

        System.out.println(">>> Usuários no Atlas:");
        repo.findAll()
                .forEach(u -> System.out.println(u.getName() + " – " + u.getEmail()));
    }
}

