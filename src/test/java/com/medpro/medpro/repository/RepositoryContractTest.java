package com.medpro.medpro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import com.medpro.medpro.model.entity.Medico;
import com.medpro.medpro.model.entity.Paciente;
import com.medpro.medpro.model.entity.Usuario;

/** Testes de contrato das interfaces repository; não iniciam o Spring nem o MySQL. */
class RepositoryContractTest {

    @Test
    void medicoRepositoryDeveUsarJpaRepositoryComIdLong() {
        assertJpaRepository(MedicoRepository.class, Medico.class);
    }

    @Test
    void pacienteRepositoryDeveUsarJpaRepositoryComIdLong() {
        assertJpaRepository(PacienteRepository.class, Paciente.class);
    }

    @Test
    void usuarioRepositoryDeveUsarJpaRepositoryComIdLong() {
        assertJpaRepository(UsuarioRepository.class, Usuario.class);
    }

    @Test
    void usuarioRepositoryDeveDeclararBuscaPorLoginRetornandoOptionalDeUsuario() throws Exception {
        Method metodo = UsuarioRepository.class.getMethod("findByLogin", String.class);

        assertEquals(Optional.class, metodo.getReturnType());
        assertTrue(metodo.getGenericReturnType() instanceof ParameterizedType,
                "O retorno de findByLogin deve informar o tipo genérico.");

        ParameterizedType retorno = (ParameterizedType) metodo.getGenericReturnType();
        assertEquals(Usuario.class, retorno.getActualTypeArguments()[0]);
    }

    private static void assertJpaRepository(Class<?> repositoryType, Class<?> entityType) {
        boolean contratoEncontrado = false;

        for (Type tipo : repositoryType.getGenericInterfaces()) {
            if (!(tipo instanceof ParameterizedType parametrizado)) {
                continue;
            }

            if (parametrizado.getRawType().equals(JpaRepository.class)) {
                Type[] argumentos = parametrizado.getActualTypeArguments();
                contratoEncontrado = argumentos.length == 2
                        && argumentos[0].equals(entityType)
                        && argumentos[1].equals(Long.class);
                break;
            }
        }

        assertTrue(contratoEncontrado,
                repositoryType.getSimpleName() + " deve estender JpaRepository<"
                        + entityType.getSimpleName() + ", Long>.");
    }
}
