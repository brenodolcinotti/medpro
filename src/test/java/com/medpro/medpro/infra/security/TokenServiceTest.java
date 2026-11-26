package com.medpro.medpro.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.medpro.medpro.model.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenServiceTest {

    private static final String SEGREDO = "medpro-test-secret-com-pelo-menos-32-bytes-123";
    private TokenService tokenService;

    @BeforeEach
    void configurar() {
        tokenService = new TokenService(SEGREDO);
    }

    @Test
    void deveGerarTokenComLoginComoSubject() {
        Usuario usuario = new Usuario(1L, "teste@medpro.com", "hash-bcrypt-nao-usado-neste-teste");

        String token = tokenService.gerarToken(usuario);

        assertEquals("teste@medpro.com", tokenService.getSubject(token));
    }

    @Test
    void deveRejeitarTokenAdulterado() {
        Usuario usuario = new Usuario(1L, "teste@medpro.com", "hash-bcrypt-nao-usado-neste-teste");
        String token = tokenService.gerarToken(usuario);
        String[] partes = token.split("\\.");
        String assinatura = partes[2];
        String assinaturaAlterada = (assinatura.startsWith("A") ? "B" : "A")
                + assinatura.substring(1);
        String tokenAlterado = partes[0] + "." + partes[1] + "." + assinaturaAlterada;

        assertThrows(JWTVerificationException.class,
                () -> tokenService.getSubject(tokenAlterado));
    }

    @Test
    void deveRejeitarTokenExpirado() {
        String tokenExpirado = JWT.create()
                .withIssuer("API MedPro")
                .withSubject("teste@medpro.com")
                .withExpiresAt(Instant.now().minusSeconds(60))
                .sign(Algorithm.HMAC256(SEGREDO));

        assertThrows(JWTVerificationException.class,
                () -> tokenService.getSubject(tokenExpirado));
    }

    @Test
    void deveRejeitarSegredoMenorQue32Bytes() {
        assertThrows(IllegalStateException.class, () -> new TokenService("segredo-curto"));
    }
}
