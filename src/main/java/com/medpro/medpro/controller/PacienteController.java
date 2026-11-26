package com.medpro.medpro.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.medpro.medpro.model.dto.DadosCadastroPaciente;
import com.medpro.medpro.model.dto.DadosDetalhamentoPaciente;
import com.medpro.medpro.model.dto.DadosListagemPaciente;
import com.medpro.medpro.model.entity.Paciente;
import com.medpro.medpro.repository.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

/** Endpoints REST de cadastro, listagem e detalhamento de pacientes. */
@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoPaciente> cadastrar(
            @RequestBody @Valid DadosCadastroPaciente dados,
            UriComponentsBuilder uriBuilder) {

        Paciente pacienteSalvo = pacienteRepository.save(new Paciente(dados));

        var uri = uriBuilder
                .path("/pacientes/{id}")
                .buildAndExpand(pacienteSalvo.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(new DadosDetalhamentoPaciente(pacienteSalvo));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {

        var pagina = pacienteRepository.findAll(paginacao)
                .map(DadosListagemPaciente::new);

        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPaciente> detalhar(
            @PathVariable Long id) {

        var paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado."));

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }
}
