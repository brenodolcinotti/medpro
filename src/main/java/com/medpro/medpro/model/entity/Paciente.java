package com.medpro.medpro.model.entity;

import com.medpro.medpro.model.dto.DadosCadastroPaciente;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Entidade JPA que representa um paciente cadastrado na clínica. */
@Entity
@Table(name = "pacientes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Embedded
    private Endereco endereco;

    /** Constrói a entidade a partir do DTO recebido pelo controller. */
    public Paciente(DadosCadastroPaciente dados) {
        this.nome = dados.nome().trim();
        this.email = dados.email().trim();
        this.telefone = dados.telefone().trim();
        this.cpf = dados.cpf().trim();
        this.endereco = new Endereco(dados.endereco());
    }
}
