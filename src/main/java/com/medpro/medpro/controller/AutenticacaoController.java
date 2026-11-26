
package com.medpro.medpro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medpro.medpro.infra.security.DadosTokenJWT;
import com.medpro.medpro.infra.security.TokenService;
import com.medpro.medpro.model.dto.DadosAutenticacao;
import com.medpro.medpro.model.entity.Usuario;

import jakarta.validation.Valid;

/**
 * Endpoint público que autentica login/senha e devolve um token JWT.
 */
@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticacaoController(
            AuthenticationManager authenticationManager,
            TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(
            @RequestBody @Valid DadosAutenticacao dados) {

        var credenciais = new UsernamePasswordAuthenticationToken(
                dados.login(),
                dados.senha());

        var autenticacao = authenticationManager.authenticate(credenciais);
        var usuario = (Usuario) autenticacao.getPrincipal();
        var token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new DadosTokenJWT(token));
    }
}
