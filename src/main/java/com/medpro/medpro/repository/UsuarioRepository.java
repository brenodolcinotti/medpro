package com.medpro.medpro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medpro.medpro.model.entity.Usuario;

/** Repositório dos usuários utilizados na autenticação. */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca o usuário pelo login.
     * O Spring Data JPA deriva a consulta a partir do nome do método.
     *
     * @param login login informado na autenticação
     * @return usuário encontrado ou Optional.empty() quando não existir
     */
    Optional<Usuario> findByLogin(String login);
}
