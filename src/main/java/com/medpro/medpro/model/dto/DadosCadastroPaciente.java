package com.medpro.medpro.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO utilizado para receber os dados de cadastro de um paciente pela API.
 * O endereço é validado em cascata por meio de @Valid.
 */
public record DadosCadastroPaciente(

        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um endereço de e-mail válido.")
        String email,

        @NotBlank(message = "O telefone é obrigatório.")
        String telefone,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 números, sem pontos ou traços.")
        String cpf,

        @NotNull(message = "O endereço é obrigatório.")
        @Valid DadosEndereco endereco) {

}
