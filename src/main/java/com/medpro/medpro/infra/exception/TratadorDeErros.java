
package com.medpro.medpro.infra.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErro400(
            MethodArgumentNotValidException exception) {

        var erros = exception.getFieldErrors();

        var dados = erros.stream()
                .map(DadosErroValidacao::new)
                .toList();

        return ResponseEntity.badRequest().body(dados);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DadosErro> tratarErro401() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new DadosErro("Login ou senha inválidos."));
    }

    public record DadosErroValidacao(
            String campo,
            String mensagem) {

        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
