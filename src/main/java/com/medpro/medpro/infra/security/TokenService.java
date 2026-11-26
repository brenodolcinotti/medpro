package com.medpro.medpro.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.medpro.medpro.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/** Cria e valida tokens JWT assinados com HMAC256. */
@Service
public class TokenService {

    private static final String EMISSOR = "API MedPro";
    private static final long DURACAO_TOKEN_HORAS = 2;

    private final Algorithm algoritmo;
    private final JWTVerifier verificador;

    public TokenService(@Value("${api.security.token.secret}") String segredo) {
        if (segredo == null || segredo.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "A propriedade api.security.token.secret deve possuir pelo menos 32 bytes.");
        }

        this.algoritmo = Algorithm.HMAC256(segredo);
        this.verificador = JWT.require(algoritmo)
                .withIssuer(EMISSOR)
                .build();
    }

    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();

        return JWT.create()
                .withIssuer(EMISSOR)
                .withSubject(usuario.getLogin())
                .withIssuedAt(agora)
                .withExpiresAt(agora.plus(DURACAO_TOKEN_HORAS, ChronoUnit.HOURS))
                .sign(algoritmo);
    }

    /** Valida assinatura, emissor e validade temporal e devolve o login do usuário. */
    public String getSubject(String tokenJWT) throws JWTVerificationException {
        return verificador.verify(tokenJWT).getSubject();
    }
}
