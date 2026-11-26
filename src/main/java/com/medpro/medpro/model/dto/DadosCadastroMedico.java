package com.medpro.medpro.model.dto;

import com.medpro.medpro.enums.Especialidade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroMedico(
        @NotBlank(message = "O nome é obrigatório.") String nome,
        @NotBlank(message = "O e-mail é obrigatório.") @Email(message = "Informe um e-mail válido.") String email,
        @NotBlank(message = "O telefone é obrigatório.") String telefone,
        @NotBlank(message = "O CRM é obrigatório.")
        @Pattern(regexp = "\\d{4,6}", message = "O CRM deve conter de 4 a 6 números.") String crm,
        @NotNull(message = "A especialidade é obrigatória.") Especialidade especialidade,
        @NotNull(message = "O endereço é obrigatório.") @Valid DadosEndereco endereco) {
}
