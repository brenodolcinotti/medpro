package com.medpro.medpro.infra.security;

/** Resposta enviada ao cliente após autenticação bem-sucedida. */
public record DadosTokenJWT(String token) {
}
