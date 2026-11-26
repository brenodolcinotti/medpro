package com.medpro.medpro.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** Dados de endereço recebidos pela API. Número e complemento são opcionais. */
public record DadosEndereco(
        @NotBlank(message = "O logradouro é obrigatório.")
        String logradouro,

        @NotBlank(message = "O bairro é obrigatório.")
        String bairro,

        @NotBlank(message = "O CEP é obrigatório.")
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 números, sem hífen.")
        String cep,

        @NotBlank(message = "A cidade é obrigatória.")
        String cidade,

        @NotBlank(message = "A UF é obrigatória.")
        String uf,

        String numero,
        String complemento) {
}
