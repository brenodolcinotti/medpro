package com.medpro.medpro.infra.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import com.medpro.medpro.model.entity.Usuario;
import com.medpro.medpro.repository.UsuarioRepository;

class SecurityFilterTest {

    private static final String SECRET = "medpro-filter-test-secret-with-at-least-32-bytes";
    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;
    private SecurityFilter securityFilter;

    @BeforeEach
    void configurar() {
        SecurityContextHolder.clearContext();
        tokenService = new TokenService(SECRET);
        usuarioRepository = mock(UsuarioRepository.class);
        securityFilter = new SecurityFilter(tokenService, usuarioRepository);
    }

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void semTokenDeveDeixarFiltroProsseguirParaOEntryPointDoSpringSecurity() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var chamouProximoFiltro = new AtomicBoolean(false);

        securityFilter.doFilter(request, response,
                (req, res) -> chamouProximoFiltro.set(true));

        assertTrue(chamouProximoFiltro.get());
        assertFalse(SecurityContextHolder.getContext().getAuthentication() != null);
    }

    @Test
    void comTokenValidoDevePopularSecurityContext() throws Exception {
        var usuario = new Usuario(1L, "teste@medpro.com", "hash-bcrypt-nao-usado-neste-teste");
        when(usuarioRepository.findByLogin(usuario.getLogin())).thenReturn(Optional.of(usuario));
        String token = tokenService.gerarToken(usuario);

        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        var response = new MockHttpServletResponse();
        var chamouProximoFiltro = new AtomicBoolean(false);

        securityFilter.doFilter(request, response,
                (req, res) -> chamouProximoFiltro.set(true));

        assertTrue(chamouProximoFiltro.get());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(usuario, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void comTokenInvalidoDeveResponder401() throws Exception {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-invalido");
        var response = new MockHttpServletResponse();
        var chamouProximoFiltro = new AtomicBoolean(false);

        securityFilter.doFilter(request, response,
                (req, res) -> chamouProximoFiltro.set(true));

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Token JWT inválido ou expirado!"));
        assertFalse(chamouProximoFiltro.get());
    }
}
