package com.medpro.medpro.infra.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioPasswordTest {

    @Test
    void hashDaMigrationDeveValidarSenhaDidaticaDaAula() {
        String hashDaMigration = "$2a$10$7qoiGo8MY204lRyCiRhg2uwaHGK10JU6XDP.UdYGAuxw.dZde8C.6";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        assertTrue(encoder.matches("123456", hashDaMigration));
        assertFalse(encoder.matches("senha-incorreta", hashDaMigration));
    }
}
