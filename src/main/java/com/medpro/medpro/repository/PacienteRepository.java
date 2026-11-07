package com.medpro.medpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medpro.medpro.model.entity.Paciente;

/** Repositório Spring Data JPA para persistência de pacientes. */
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}
