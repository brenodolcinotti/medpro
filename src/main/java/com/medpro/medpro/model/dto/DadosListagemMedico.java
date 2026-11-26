package com.medpro.medpro.model.dto;

import com.medpro.medpro.enums.Especialidade;
import com.medpro.medpro.model.entity.Medico;

/** DTO resumido usado na listagem paginada de médicos. */
public record DadosListagemMedico(
        Long id,
        String nome,
        String email,
        String telefone,
        String crm,
        Especialidade especialidade) {

    public DadosListagemMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getTelefone(),
                medico.getCrm(), medico.getEspecialidade());
    }
}
