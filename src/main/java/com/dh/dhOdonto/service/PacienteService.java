package com.dh.dhOdonto.service;

import com.dh.dhOdonto.entity.Perfil;
import com.dh.dhOdonto.entity.dto.PacienteRequestDTO;
import com.dh.dhOdonto.entity.dto.PacienteResponseDTO;
import com.dh.dhOdonto.exceptions.CadastroInvalidoException;
import com.dh.dhOdonto.exceptions.ResourceNotFoundException;
import com.dh.dhOdonto.entity.Paciente;
import com.dh.dhOdonto.repository.PacienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    PacienteRepository repository;
    ObjectMapper mapper = new ObjectMapper();
    Logger logger = Logger.getLogger(PacienteService.class);

    public ResponseEntity cadastrar(PacienteRequestDTO pacienteRequestDTO) throws CadastroInvalidoException {
        Paciente pacienteCadastrado = null;
        ObjectMapper mapper = new ObjectMapper();
        Paciente paciente = mapper.convertValue(pacienteRequestDTO, Paciente.class);
        try {
            paciente.setMatricula();
            paciente.encodePassword();
            paciente.setDataCadastro();
            paciente.getUsuario().setPerfis(Arrays.asList(new Perfil(1L)));
            pacienteCadastrado = repository.save(paciente);
        } catch (Exception e) {
            logger.error("Erro ao cadastrar paciente");
            logger.error(e.getMessage());
            throw new CadastroInvalidoException("Cadastro inválido");
        }
        logger.info("Paciente cadastrado com sucesso!");
        return new ResponseEntity(mapper.convertValue(pacienteCadastrado, PacienteResponseDTO.class), HttpStatus.OK);
    }

    public void excluir(String matricula) throws ResourceNotFoundException {
        try{
            Paciente paciente = repository.findByMatricula(matricula);
            repository.delete(paciente);

            logger.info("Paciente excluído com sucesso!");
        }catch (Exception exception){
            logger.error("Paciente não encontrado");
            throw  new ResourceNotFoundException("Paciente não encontrado");
        }
    }

    public ResponseEntity buscaPorMatricula (String matricula) throws ResourceNotFoundException {
        Paciente paciente = repository.findByMatricula(matricula);
        if(paciente == null){
            logger.error("Paciente não encontrado");
            throw  new ResourceNotFoundException("Paciente não encontrado");
        }
        return new ResponseEntity(
                mapper.convertValue(paciente,
                        PacienteResponseDTO.class), HttpStatus.OK);

    }

    public ResponseEntity buscarTodos () throws ResourceNotFoundException {

        List<Paciente> pacienteList = repository.findAll();
        if (pacienteList.isEmpty()){
            throw  new ResourceNotFoundException("Nenhum paciente cadastrado");
        }
        List<PacienteResponseDTO> pacienteResponseDTOList = new ArrayList<>();
        for (Paciente paciente : pacienteList){
            pacienteResponseDTOList.add(mapper.convertValue(paciente, PacienteResponseDTO.class));
        }

        return new ResponseEntity(pacienteResponseDTOList,HttpStatus.OK);

    }

    public ResponseEntity alterar(PacienteResponseDTO pacienteResponseDTO) throws ResourceNotFoundException {
        logger.info("Alterando paciente");
        try {
            Paciente pacienteAlterado = repository.findByMatricula(pacienteResponseDTO.getMatricula());
            if (pacienteResponseDTO.getNome() != null) {
                pacienteAlterado.setNome(pacienteResponseDTO.getNome());
            }
            if (pacienteResponseDTO.getSobrenome() != null) {
                pacienteAlterado.setSobrenome(pacienteResponseDTO.getSobrenome());
            }
            if (pacienteResponseDTO.getUsuario().getUsername() != null) {
                pacienteAlterado.getUsuario().setUsername(pacienteResponseDTO.getUsuario().getUsername());
            }
            return new ResponseEntity(mapper.convertValue(repository.save(pacienteAlterado),
                                        PacienteResponseDTO.class),
                                        HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Erro ao alterar paciente");
        }
    }

}

