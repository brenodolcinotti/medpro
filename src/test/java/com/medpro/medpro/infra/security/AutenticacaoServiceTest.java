package com.medpro.medpro.infra.security;

import com.medpro.medpro.model.entity.Usuario;
import com.medpro.medpro.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AutenticacaoServiceTest {

    @Test
    void deveLocalizarUsuarioPeloLogin() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        Usuario usuario = new Usuario(7L, "usuario@medpro.com", "hash-bcrypt");
        when(repository.findByLogin("usuario@medpro.com")).thenReturn(Optional.of(usuario));

        AutenticacaoService service = new AutenticacaoService(repository);

        assertEquals("usuario@medpro.com",
                service.loadUserByUsername("usuario@medpro.com").getUsername());
    }

    @Test
    void deveFalharComMensagemGenericaQuandoUsuarioNaoExiste() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        when(repository.findByLogin("desconhecido@medpro.com")).thenReturn(Optional.empty());

        AutenticacaoService service = new AutenticacaoService(repository);

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("desconhecido@medpro.com"));
    }
}
