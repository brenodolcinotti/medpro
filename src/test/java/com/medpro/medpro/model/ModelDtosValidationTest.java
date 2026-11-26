package com.medpro.medpro.model;

import com.medpro.medpro.enums.Especialidade;
import com.medpro.medpro.model.dto.DadosAutenticacao;
import com.medpro.medpro.model.dto.DadosCadastroMedico;
import com.medpro.medpro.model.dto.DadosCadastroPaciente;
import com.medpro.medpro.model.dto.DadosEndereco;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelDtosValidationTest {

    private static Validator validator;
    private static AutoCloseable validatorFactory;

    @BeforeAll
    static void configurarValidador() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        validatorFactory = factory::close;
    }

    @AfterAll
    static void fecharValidador() throws Exception {
        validatorFactory.close();
    }

    private DadosEndereco enderecoValido() {
        return new DadosEndereco("Rua das Flores", "Centro", "13574320", "São Carlos", "SP", null, null);
    }

    @Test
    void deveAceitarCadastroMedicoComDadosValidos() {
        var dados = new DadosCadastroMedico(
                "Ana Silva", "ana@medpro.com", "16999998888", "123456",
                Especialidade.ORTOPEDIA, enderecoValido());

        assertTrue(validator.validate(dados).isEmpty());
    }

    @Test
    void deveRejeitarCadastroMedicoComCrmInvalido() {
        var dados = new DadosCadastroMedico(
                "Ana Silva", "ana@medpro.com", "16999998888", "ABC",
                Especialidade.ORTOPEDIA, enderecoValido());

        assertFalse(validator.validate(dados).isEmpty());
    }

    @Test
    void deveAceitarCadastroPacienteComCpfDeOnzeDigitos() {
        var dados = new DadosCadastroPaciente(
                "Bruno Souza", "bruno@medpro.com", "16988887777", "52998224725", enderecoValido());

        assertTrue(validator.validate(dados).isEmpty());
    }

    @Test
    void deveRejeitarCpfComQuantidadeDeDigitosIncorreta() {
        var dados = new DadosCadastroPaciente(
                "Bruno Souza", "bruno@medpro.com", "16988887777", "1234567890", enderecoValido());

        assertFalse(validator.validate(dados).isEmpty());
    }

    @Test
    void deveValidarOEnderecoAninhadoDoPaciente() {
        var enderecoInvalido = new DadosEndereco(
                "Rua das Flores", "Centro", "123", "São Carlos", "SP", null, null);
        var dados = new DadosCadastroPaciente(
                "Bruno Souza", "bruno@medpro.com", "16988887777", "52998224725", enderecoInvalido);

        assertFalse(validator.validate(dados).isEmpty());
    }

    @Test
    void deveRejeitarCredenciaisVazias() {
        var dados = new DadosAutenticacao(" ", "");

        assertFalse(validator.validate(dados).isEmpty());
    }
}
