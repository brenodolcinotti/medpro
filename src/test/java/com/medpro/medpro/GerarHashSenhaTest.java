package com.medpro.medpro;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;

class GerarHashSenhaTest {

    @Test
    void gerarHash() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hash = encoder.encode("hello");

        Files.writeString(Path.of("senha-bcrypt.txt"), hash);

        System.out.println("Hash BCrypt gerado: " + hash);

        if (!encoder.matches("hello", hash)) {
            throw new IllegalStateException("A validação da senha falhou.");
        }
    }
}