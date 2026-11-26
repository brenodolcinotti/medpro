package com.medpro.medpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medpro.medpro.model.entity.Medico;

/**
 * Repositório de persistência dos médicos.
 * O Spring Data JPA fornece as operações CRUD, paginação e ordenação.
 */
public interface MedicoRepository extends JpaRepository<Medico, Long> {
}
