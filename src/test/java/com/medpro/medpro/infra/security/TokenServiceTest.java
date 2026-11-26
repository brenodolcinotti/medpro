package com.medpro.medpro.infra.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.medpro.medpro.model.entity.Usuario;

class TokenServiceTest {

    private static final String SECRET = "medpro-test-secret-with-at-least-32-bytes-123456";
    private TokenService tokenService;

    @BeforeEach
    void configurar() {
        tokenService = new TokenService(SECRET);
    }

    @Test
    void deveGerarTokenComLoginComoSubject() {
        var usuario = new Usuario(1L, "teste@medpro.com", "hash-bcrypt-nao-usado-neste-teste");

        String token = tokenService.gerarToken(usuario);

        assertEquals("teste@medpro.com", tokenService.getSubject(token));
    }

    @Test
    void deveRejeitarTokenAdulterado() {
        var usuario = new Usuario(1L, "teste@medpro.com", "hash-bcrypt-nao-usado-neste-teste");
        String token = tokenService.gerarToken(usuario);
        String[] partes = token.split("\\.");
        String assinatura = partes[2];
        String assinaturaAdulterada = (assinatura.startsWith("A") ? "B" : "A")
                + assinatura.substring(1);
        String adulterado = partes[0] + "." + partes[1] + "." + assinaturaAdulterada;

        assertThrows(JWTVerificationException.class, () -> tokenService.getSubject(adulterado));
    }

    @Test
    void deveRejeitarSegredoMenorQue32Bytes() {
        assertThrows(IllegalStateException.class, () -> new TokenService("curto"));
    }
}
