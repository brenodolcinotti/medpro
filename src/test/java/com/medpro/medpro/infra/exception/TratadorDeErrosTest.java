
package com.medpro.medpro.infra.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class TratadorDeErrosTest {

    private final TratadorDeErros tratador =
            new TratadorDeErros();

    @Test
    void entidadeNaoEncontradaDeveRetornar404() {

        var resposta = tratador.tratarErro404();

        assertEquals(404, resposta.getStatusCode().value());
    }

    @Test
    void loginInvalidoDeveRetornar401SemRevelarQualCampoFalhou() {

        var resposta = tratador.tratarErro401();

        assertEquals(401, resposta.getStatusCode().value());

        DadosErro corpo = resposta.getBody();

        assertNotNull(corpo);

        assertEquals(
                "Login ou senha inválidos.",
                corpo.mensagem()
        );
    }
}
