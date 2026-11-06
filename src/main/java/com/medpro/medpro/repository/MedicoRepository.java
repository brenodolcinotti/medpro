package com.medpro.medpro.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.medpro.medpro.model.dto.DadosCadastroMedico;
import com.medpro.medpro.model.entity.Medico;

import jakarta.transaction.Transactional;

public interface MedicoRepository extends JpaRepository<Medico, Long>{

}


