package com.dh.dhOdonto.controller;

import com.dh.dhOdonto.entity.Dentista;
import com.dh.dhOdonto.entity.dto.DentistaRequestDTO;
import com.dh.dhOdonto.entity.dto.DentistaResponseDTO;
import com.dh.dhOdonto.exceptions.CadastroInvalidoException;
import com.dh.dhOdonto.exceptions.ResourceNotFoundException;
import com.dh.dhOdonto.service.DentistaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/dentista")
public class DentistaController {
    @Autowired
    DentistaService dentistaService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody  DentistaRequestDTO dentista) throws CadastroInvalidoException {

        return dentistaService.cadastrar(dentista);

    }


    @GetMapping
    public ResponseEntity buscarTodos(@RequestParam(value = "matricula", required = false) String matricula) throws ResourceNotFoundException {
        if (matricula != null) {
           return dentistaService.buscarDentistaPorMatricula(matricula);
        } else {
            return dentistaService.buscarTodos();
        }
    }

    @DeleteMapping
    public void excluir(@RequestParam("matricula") String matricula) throws ResourceNotFoundException {
        dentistaService.excluir(matricula);}

    @PatchMapping
    public ResponseEntity alterar(@RequestBody  DentistaResponseDTO dentista) throws ResourceNotFoundException {
        return dentistaService.alterar(dentista);
    }
}

