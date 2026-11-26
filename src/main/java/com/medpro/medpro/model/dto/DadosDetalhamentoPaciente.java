package com.medpro.medpro.model.dto;

import com.medpro.medpro.model.entity.Endereco;
import com.medpro.medpro.model.entity.Paciente;

/** DTO de saída para detalhar paciente sem expor diretamente a entidade JPA. */
public record DadosDetalhamentoPaciente(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        DadosEndereco endereco) {

    public DadosDetalhamentoPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone(),
                paciente.getCpf(), converterEndereco(paciente.getEndereco()));
    }

    private static DadosEndereco converterEndereco(Endereco endereco) {
        return new DadosEndereco(endereco.getLogradouro(), endereco.getBairro(), endereco.getCep(),
                endereco.getCidade(), endereco.getUf(), endereco.getNumero(), endereco.getComplemento());
    }
}
