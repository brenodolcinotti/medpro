package com.medpro.medpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medpro.medpro.model.entity.Paciente;

/**
 * Repositório de persistência dos pacientes.
 * O Spring Data JPA fornece as operações CRUD, paginação e ordenação.
 */
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
