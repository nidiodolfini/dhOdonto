package com.dh.dhOdonto.controller;

import com.dh.dhOdonto.entity.dto.PacienteRequestDTO;
import com.dh.dhOdonto.entity.dto.PacienteResponseDTO;
import com.dh.dhOdonto.exceptions.CadastroInvalidoException;
import com.dh.dhOdonto.exceptions.ResourceNotFoundException;
import com.dh.dhOdonto.entity.Paciente;
import com.dh.dhOdonto.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paciente")
public class PacienteController {
    @Autowired
    PacienteService pacienteService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody PacienteRequestDTO pacienteRequestDTO) throws CadastroInvalidoException {
        return pacienteService.cadastrar(pacienteRequestDTO);
    }

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "matricula", required = false)String matricula) throws ResourceNotFoundException {
        if(matricula != null) {
            return new ResponseEntity(pacienteService.buscaPorMatricula(matricula),HttpStatus.OK);
        } else {
            return new ResponseEntity(pacienteService.buscarTodos(), HttpStatus.OK);
        }
    }


    @DeleteMapping
    public void excluir(@RequestParam("matricula") String matricula) throws ResourceNotFoundException {
        pacienteService.excluir(matricula);}


    @PatchMapping
    public ResponseEntity alterar(@RequestBody PacienteResponseDTO pacienteResponseDTO) throws ResourceNotFoundException {
        return pacienteService.alterar(pacienteResponseDTO);
    }
}

