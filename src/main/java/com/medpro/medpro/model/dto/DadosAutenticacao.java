package com.medpro.medpro.model.dto;

import jakarta.validation.constraints.NotBlank;

/** Credenciais recebidas pelo endpoint POST /login. */
public record DadosAutenticacao(
        @NotBlank(message = "O login é obrigatório.") String login,
        @NotBlank(message = "A senha é obrigatória.") String senha) {
}
