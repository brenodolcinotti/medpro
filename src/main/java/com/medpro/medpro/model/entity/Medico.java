package com.medpro.medpro.model.entity;

import com.medpro.medpro.enums.Especialidade;
import com.medpro.medpro.model.dto.DadosCadastroMedico;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidade JPA que representa um médico da clínica. */
@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 6)
    private String crm;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    /** Construtor exigido pelo JPA. */
    protected Medico() {
    }

    /** Constrói a entidade a partir do DTO validado recebido pela API. */
    public Medico(DadosCadastroMedico dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Os dados do médico são obrigatórios.");
        }
        this.nome = normalizar(dados.nome());
        this.email = normalizar(dados.email());
        this.telefone = normalizar(dados.telefone());
        this.crm = normalizar(dados.crm());
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }

    private static String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCrm() { return crm; }
    public String getTelefone() { return telefone; }
    public Especialidade getEspecialidade() { return especialidade; }
    public Endereco getEndereco() { return endereco; }
}
