package com.medpro.medpro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.medpro.medpro.model.dto.DadosCadastroPaciente;
import com.medpro.medpro.model.dto.DadosEndereco;
import com.medpro.medpro.model.entity.Paciente;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/** Testes unitários do DTO e do mapeamento DTO -> entidade Paciente. */
class PacienteCadastroTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarPacienteValidoEMapearTodosOsDados() {
        DadosCadastroPaciente dados = dadosValidos(
                "Ana Silva", "ana.silva@medpro.com", "16999998888", "12345678901",
                new DadosEndereco("Rua das Flores", "Centro", "13574320", "São Carlos", "SP", "123", "Apto 2"));

        assertTrue(validator.validate(dados).isEmpty());

        Paciente paciente = new Paciente(dados);
        assertEquals("Ana Silva", paciente.getNome());
        assertEquals("ana.silva@medpro.com", paciente.getEmail());
        assertEquals("16999998888", paciente.getTelefone());
        assertEquals("12345678901", paciente.getCpf());
        assertEquals("Rua das Flores", paciente.getEndereco().getLogradouro());
        assertEquals("Centro", paciente.getEndereco().getBairro());
        assertEquals("13574320", paciente.getEndereco().getCep());
        assertEquals("São Carlos", paciente.getEndereco().getCidade());
        assertEquals("SP", paciente.getEndereco().getUf());
        assertEquals("123", paciente.getEndereco().getNumero());
        assertEquals("Apto 2", paciente.getEndereco().getComplemento());
    }

    @Test
    void devePermitirNumeroEComplementoOpcionais() {
        DadosCadastroPaciente dados = dadosValidos(
                "Ana Silva", "ana.silva@medpro.com", "16999998888", "12345678901",
                new DadosEndereco("Rua das Flores", "Centro", "13574320", "São Carlos", "SP", null, null));

        assertTrue(validator.validate(dados).isEmpty());
        Paciente paciente = new Paciente(dados);
        assertNull(paciente.getEndereco().getNumero());
        assertNull(paciente.getEndereco().getComplemento());
    }

    @Test
    void deveRejeitarCpfForaDoFormatoDeOnzeDigitos() {
        DadosCadastroPaciente dados = dadosValidos(
                "Ana Silva", "ana.silva@medpro.com", "16999998888", "1234567890",
                enderecoValido());

        Set<ConstraintViolation<DadosCadastroPaciente>> violacoes = validator.validate(dados);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
    }

    @Test
    void deveRejeitarEmailInvalido() {
        DadosCadastroPaciente dados = dadosValidos(
                "Ana Silva", "email-invalido", "16999998888", "12345678901", enderecoValido());

        Set<ConstraintViolation<DadosCadastroPaciente>> violacoes = validator.validate(dados);
        assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void deveRejeitarEnderecoComCampoObrigatorioEmBranco() {
        DadosCadastroPaciente dados = dadosValidos(
                "Ana Silva", "ana.silva@medpro.com", "16999998888", "12345678901",
                new DadosEndereco("", "Centro", "13574320", "São Carlos", "SP", null, null));

        Set<ConstraintViolation<DadosCadastroPaciente>> violacoes = validator.validate(dados);
        assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("endereco.logradouro")));
    }

    private static DadosCadastroPaciente dadosValidos(
            String nome, String email, String telefone, String cpf, DadosEndereco endereco) {
        return new DadosCadastroPaciente(nome, email, telefone, cpf, endereco);
    }

    private static DadosEndereco enderecoValido() {
        return new DadosEndereco("Rua das Flores", "Centro", "13574320", "São Carlos", "SP", "123", "Apto 2");
    }
}
