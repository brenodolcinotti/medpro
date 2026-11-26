package com.medpro.medpro.model.entity;

import com.medpro.medpro.model.dto.DadosEndereco;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Endereço incorporado às entidades Médico e Paciente. */
@Embeddable
public class Endereco {

    @Column(name = "logradouro", nullable = false, length = 100)
    private String logradouro;

    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    // A migration usa VARCHAR(9); o DTO recebe oito dígitos, sem hífen.
    @Column(name = "cep", nullable = false, length = 9)
    private String cep;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    /** Construtor exigido pelo JPA. */
    protected Endereco() {
    }

    public Endereco(DadosEndereco dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Os dados do endereço são obrigatórios.");
        }
        this.logradouro = normalizarObrigatorio(dados.logradouro());
        this.bairro = normalizarObrigatorio(dados.bairro());
        this.cep = normalizarObrigatorio(dados.cep());
        this.cidade = normalizarObrigatorio(dados.cidade());
        this.uf = normalizarObrigatorio(dados.uf());
        this.numero = normalizarOpcional(dados.numero());
        this.complemento = normalizarOpcional(dados.complemento());
    }

    private static String normalizarObrigatorio(String valor) {
        return valor == null ? null : valor.trim();
    }

    private static String normalizarOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }

    public String getLogradouro() { return logradouro; }
    public String getBairro() { return bairro; }
    public String getCep() { return cep; }
    public String getCidade() { return cidade; }
    public String getUf() { return uf; }
    public String getNumero() { return numero; }
    public String getComplemento() { return complemento; }
}
