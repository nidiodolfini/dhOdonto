package com.dh.dhOdonto.controller;

import com.dh.dhOdonto.entity.Consulta;
import com.dh.dhOdonto.entity.dto.ConsultaDTO;
import com.dh.dhOdonto.exceptions.CadastroInvalidoException;
import com.dh.dhOdonto.exceptions.ResourceNotFoundException;
import com.dh.dhOdonto.service.ConsultaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {
    @Autowired
    ConsultaService consultaService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody ConsultaDTO consultaDTO) throws CadastroInvalidoException {
        return consultaService.cadastrar(consultaDTO);
    }

    @DeleteMapping
    public void deletar(@RequestBody ConsultaDTO consultaDTO) throws ResourceNotFoundException {
        consultaService.deletar(consultaDTO.getDentista().getMatricula()
                ,consultaDTO.getDataHoraAgendamento());
    }

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "matricula", required = false)String matricula) throws ResourceNotFoundException {
        if(matricula != null) {
            return consultaService.buscaPorMatricula(matricula);
        } else {
            return consultaService.buscaTodos();
        }
    }
}

