package com.medpro.medpro.infra.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TratadorDeErrosTest {

    private final TratadorDeErros tratador = new TratadorDeErros();

    @Test
    void entidadeNaoEncontradaDeveRetornar404() {
        var resposta = tratador.tratarErro404();
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void loginInvalidoDeveRetornar401SemRevelarQualCampoFalhou() {
        var resposta = tratador.tratarErro401();
        assertEquals(HttpStatus.UNAUTHORIZED, resposta.getStatusCode());
        assertEquals("Login ou senha inválidos.", resposta.getBody().mensagem());
    }
}
