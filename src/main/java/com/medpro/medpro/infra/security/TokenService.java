package com.medpro.medpro.infra.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.medpro.medpro.model.entity.Usuario;

@Service
public class TokenService {

    private static final String ISSUER = "API MedPro";
    private final Algorithm algoritmo;

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET deve possuir pelo menos 32 bytes.");
        }
        this.algoritmo = Algorithm.HMAC256(secret);
    }

    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(usuario.getLogin())
                .withIssuedAt(agora)
                .withExpiresAt(agora.plus(2, ChronoUnit.HOURS))
                .sign(algoritmo);
    }

    /** Valida assinatura, emissor e expiração antes de devolver o login do usuário. */
    public String getSubject(String tokenJWT) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algoritmo)
                .withIssuer(ISSUER)
                .build();
        return verifier.verify(tokenJWT).getSubject();
    }
}
