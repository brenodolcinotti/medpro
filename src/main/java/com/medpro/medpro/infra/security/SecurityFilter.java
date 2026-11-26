package com.medpro.medpro.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.medpro.medpro.model.entity.Usuario;
import com.medpro.medpro.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/** Lê e valida Authorization: Bearer <token> nas rotas protegidas. */
public class SecurityFilter extends OncePerRequestFilter {

    private static final String MENSAGEM_TOKEN_INVALIDO = "Token JWT inválido ou expirado!";

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    /** O login é público e não deve ser bloqueado por um Bearer antigo enviado pelo cliente. */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/login".equals(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenJWT = recuperarToken(request);
        if (tokenJWT == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String login;
        try {
            login = tokenService.getSubject(tokenJWT);
        } catch (JWTVerificationException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
            responderNaoAutorizado(response);
            return;
        }

        if (login == null || login.isBlank()) {
            SecurityContextHolder.clearContext();
            responderNaoAutorizado(response);
            return;
        }

        Usuario usuario = usuarioRepository.findByLogin(login).orElse(null);
        if (usuario == null || !usuario.isEnabled()) {
            SecurityContextHolder.clearContext();
            responderNaoAutorizado(response);
            return;
        }

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(autenticacao);

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null
                || !authorizationHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return null;
        }

        String token = authorizationHeader.substring(7).trim();
        return token.isEmpty() ? null : token;
    }

    private void responderNaoAutorizado(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        // Mensagem fixa da aplicação; não contém conteúdo fornecido pelo cliente.
        response.getWriter().write("{\"mensagem\":\"" + MENSAGEM_TOKEN_INVALIDO + "\"}");
    }
}
