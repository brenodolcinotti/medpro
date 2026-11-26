package com.medpro.medpro.model;

import com.medpro.medpro.enums.Especialidade;
import com.medpro.medpro.model.dto.DadosCadastroMedico;
import com.medpro.medpro.model.dto.DadosCadastroPaciente;
import com.medpro.medpro.model.dto.DadosDetalhamentoMedico;
import com.medpro.medpro.model.dto.DadosDetalhamentoPaciente;
import com.medpro.medpro.model.dto.DadosEndereco;
import com.medpro.medpro.model.dto.DadosListagemMedico;
import com.medpro.medpro.model.dto.DadosListagemPaciente;
import com.medpro.medpro.model.entity.Endereco;
import com.medpro.medpro.model.entity.Medico;
import com.medpro.medpro.model.entity.Paciente;
import com.medpro.medpro.model.entity.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelEntitiesTest {

    private DadosEndereco enderecoValido() {
        return new DadosEndereco(
                " Rua das Flores ", " Centro ", "13574320",
                " São Carlos ", " SP ", " 123 ", " Apto 2 ");
    }

    @Test
    void deveCriarMedicoEMapearDtosDeDetalhamentoEListagem() {
        var dados = new DadosCadastroMedico(
                " Ana Silva ", " ana@medpro.com ", " 16999998888 ",
                "123456", Especialidade.ORTOPEDIA, enderecoValido());

        Medico medico = new Medico(dados);
        DadosDetalhamentoMedico detalhamento = new DadosDetalhamentoMedico(medico);
        DadosListagemMedico listagem = new DadosListagemMedico(medico);

        assertEquals("Ana Silva", medico.getNome());
        assertEquals("ana@medpro.com", medico.getEmail());
        assertEquals("16999998888", medico.getTelefone());
        assertEquals("123456", medico.getCrm());
        assertEquals(Especialidade.ORTOPEDIA, medico.getEspecialidade());
        assertEquals("Rua das Flores", medico.getEndereco().getLogradouro());
        assertEquals("123", medico.getEndereco().getNumero());
        assertEquals("Apto 2", medico.getEndereco().getComplemento());
        assertEquals(medico.getNome(), detalhamento.nome());
        assertEquals("13574320", detalhamento.endereco().cep());
        assertEquals(medico.getEmail(), listagem.email());
    }

    @Test
    void deveCriarPacienteEMapearDtosDeDetalhamentoEListagem() {
        var dados = new DadosCadastroPaciente(
                " Bruno Souza ", " bruno@medpro.com ", " 16988887777 ",
                "52998224725", enderecoValido());

        Paciente paciente = new Paciente(dados);
        DadosDetalhamentoPaciente detalhamento = new DadosDetalhamentoPaciente(paciente);
        DadosListagemPaciente listagem = new DadosListagemPaciente(paciente);

        assertEquals("Bruno Souza", paciente.getNome());
        assertEquals("bruno@medpro.com", paciente.getEmail());
        assertEquals("16988887777", paciente.getTelefone());
        assertEquals("52998224725", paciente.getCpf());
        assertEquals("Rua das Flores", paciente.getEndereco().getLogradouro());
        assertEquals(paciente.getCpf(), detalhamento.cpf());
        assertEquals("SP", detalhamento.endereco().uf());
        assertEquals(paciente.getEmail(), listagem.email());
        assertEquals(paciente.getCpf(), listagem.cpf());
    }

    @Test
    void enderecoDeveAceitarNumeroEComplementoOpcionaisAusentes() {
        Endereco endereco = new Endereco(
                new DadosEndereco("Rua A", "Centro", "12345678", "São Carlos", "SP", null, null));

        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals(null, endereco.getNumero());
        assertEquals(null, endereco.getComplemento());
    }

    @Test
    void usuarioDeveImplementarUserDetailsComRoleUser() {
        Usuario usuario = new Usuario(7L, "usuario@medpro.com", "hash-bcrypt");

        assertEquals("usuario@medpro.com", usuario.getUsername());
        assertEquals("hash-bcrypt", usuario.getPassword());
        assertEquals(1, usuario.getAuthorities().size());
        assertEquals("ROLE_USER", usuario.getAuthorities().iterator().next().getAuthority());
        assertTrue(usuario.isEnabled());
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
    }
}
