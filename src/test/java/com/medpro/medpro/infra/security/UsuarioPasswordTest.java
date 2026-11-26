package com.medpro.medpro.infra.security;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UsuarioPasswordTest {

    @Test
    void hashDaMigrationDeveValidarSenhaDidaticaDoMaterial() {
        String hashDaMigration = "$2a$10$7qoiGo8MY204lRyCiRhg2uwaHGK10JU6XDP.UdYGAuxw.dZde8C.6";

        assertTrue(new BCryptPasswordEncoder().matches("123456", hashDaMigration));
    }
}
