package com.medpro.medpro.infra.security;

import com.medpro.medpro.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/** Configuração stateless: somente POST /login é público; as demais rotas exigem JWT. */
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   TokenService tokenService,
                                                   UsuarioRepository usuarioRepository) throws Exception {
        // O filtro é criado diretamente nesta cadeia para não ser registrado duas vezes
        // pelo servlet container e pelo Spring Security.
        SecurityFilter securityFilter = new SecurityFilter(tokenService, usuarioRepository);

        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                .requestCache(cache -> cache.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) ->
                                escreverErro(response, HttpServletResponse.SC_UNAUTHORIZED,
                                        "Autenticação obrigatória. Envie um token Bearer válido."))
                        .accessDeniedHandler((request, response, exception) ->
                                escreverErro(response, HttpServletResponse.SC_FORBIDDEN,
                                        "Acesso não permitido para este usuário.")))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static void escreverErro(HttpServletResponse response, int status, String mensagem)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        // Mensagens controladas pela aplicação, sem dados fornecidos pelo cliente.
        response.getWriter().write("{\"mensagem\":\"" + mensagem + "\"}");
    }
}
