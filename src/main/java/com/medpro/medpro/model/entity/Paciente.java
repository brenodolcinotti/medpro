package com.medpro.medpro.model.entity;

import com.medpro.medpro.model.dto.DadosCadastroPaciente;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidade JPA que representa um paciente cadastrado na clínica. */
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Embedded
    private Endereco endereco;

    /** Construtor exigido pelo JPA. */
    protected Paciente() {
    }

    /** Constrói a entidade a partir do DTO validado recebido pela API. */
    public Paciente(DadosCadastroPaciente dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Os dados do paciente são obrigatórios.");
        }
        this.nome = normalizar(dados.nome());
        this.email = normalizar(dados.email());
        this.telefone = normalizar(dados.telefone());
        this.cpf = normalizar(dados.cpf());
        this.endereco = new Endereco(dados.endereco());
    }

    private static String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getCpf() { return cpf; }
    public Endereco getEndereco() { return endereco; }
}
