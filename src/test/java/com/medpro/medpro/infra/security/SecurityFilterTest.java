package com.medpro.medpro.infra.security;

import com.medpro.medpro.model.entity.Usuario;
import com.medpro.medpro.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityFilterTest {

    private static final String SEGREDO = "medpro-filter-test-secret-com-pelo-menos-32-bytes";
    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;
    private SecurityFilter securityFilter;

    @BeforeEach
    void configurar() {
        SecurityContextHolder.clearContext();
        tokenService = new TokenService(SEGREDO);
        usuarioRepository = mock(UsuarioRepository.class);
        securityFilter = new SecurityFilter(tokenService, usuarioRepository);
    }

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void semTokenDeveDeixarSpringSecurityDecidirAcesso() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/pacientes");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean proximoFiltroFoiChamado = new AtomicBoolean(false);

        securityFilter.doFilter(request, response,
                (req, res) -> proximoFiltroFoiChamado.set(true));

        assertTrue(proximoFiltroFoiChamado.get());
        assertFalse(SecurityContextHolder.getContext().getAuthentication() != null);
    }

    @Test
    void rotaLoginDeveIgnorarTokenAntigoOuInvalido() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setServletPath("/login");
        request.addHeader("Authorization", "Bearer token-invalido");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean proximoFiltroFoiChamado = new AtomicBoolean(false);

        securityFilter.doFilter(request, response,
                (req, res) -> proximoFiltroFoiChamado.set(true));

        assertTrue(proximoFiltroFoiChamado.get());
        assertEquals(200, response.getStatus());
    }

    @Test
    void tokenValidoDevePopularSecurityContext() throws Exception {
        Usuario usuario = new Usuario(1L, "teste@medpro.com", "hash-bcrypt-nao-usado-neste-teste");
        when(usuarioRepository.findByLogin(usuario.getLogin())).thenReturn(Optional.of(usuario));
        String token = tokenService.gerarToken(usuario);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/pacientes");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean proximoFiltroFoiChamado = new AtomicBoolean(false);

        securityFilter.doFilter(request, response,
                (req, res) -> proximoFiltroFoiChamado.set(true));

        assertTrue(proximoFiltroFoiChamado.get());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(usuario, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void tokenInvalidoDeveResponder401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/pacientes");
        request.addHeader("Authorization", "Bearer token-invalido");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean proximoFiltroFoiChamado = new AtomicBoolean(false);

        securityFilter.doFilter(request, response,
                (req, res) -> proximoFiltroFoiChamado.set(true));

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Token JWT inválido ou expirado!"));
        assertFalse(proximoFiltroFoiChamado.get());
    }

    @Test
    void tokenValidoDeUsuarioRemovidoDeveResponder401() throws Exception {
        Usuario usuario = new Usuario(1L, "removido@medpro.com", "hash-bcrypt");
        when(usuarioRepository.findByLogin(usuario.getLogin())).thenReturn(Optional.empty());
        String token = tokenService.gerarToken(usuario);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/pacientes");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean proximoFiltroFoiChamado = new AtomicBoolean(false);

        securityFilter.doFilter(request, response,
                (req, res) -> proximoFiltroFoiChamado.set(true));

        assertEquals(401, response.getStatus());
        assertFalse(proximoFiltroFoiChamado.get());
    }
}
