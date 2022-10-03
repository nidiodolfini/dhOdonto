package com.dh.dhOdonto.service;

import com.dh.dhOdonto.entity.Dentista;
import com.dh.dhOdonto.entity.dto.DentistaRequestDTO;
import com.dh.dhOdonto.entity.dto.DentistaResponseDTO;
import com.dh.dhOdonto.exceptions.CadastroInvalidoException;
import com.dh.dhOdonto.exceptions.ResourceNotFoundException;
import com.dh.dhOdonto.repository.DentistaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DentistaService {
    @Autowired
    DentistaRepository repository;

    ObjectMapper mapper = new ObjectMapper();
    Logger logger = Logger.getLogger(DentistaService.class);

    public ResponseEntity cadastrar(DentistaRequestDTO dentistaRequestDTO) throws CadastroInvalidoException {
        Dentista dentistaCadastrado = null;
        Dentista dentista = mapper.convertValue(dentistaRequestDTO, Dentista.class);
        try {
            dentista.setMatricula();
            dentista.encodePassword();
            dentistaCadastrado = repository.save(dentista);
        } catch (Exception e) {
            logger.error("Erro ao cadastrar dentista");
            logger.error(e.getMessage());
            throw new CadastroInvalidoException("Cadastro inválido");
        }
        logger.info("Dentista cadastrado com sucesso!");
        return new ResponseEntity(mapper.convertValue(dentistaCadastrado, DentistaResponseDTO.class), HttpStatus.OK);
    }

    public ResponseEntity buscarDentistaPorMatricula(String matricula) throws ResourceNotFoundException {
        Dentista dentista = repository.findByMatricula(matricula);
        if(dentista == null) {
            logger.error("Dentista não encontrado");
            throw new ResourceNotFoundException("Dentista não encontrado");
        }

            return new ResponseEntity(
                    mapper.convertValue(dentista,
                            DentistaResponseDTO.class), HttpStatus.OK);


    }

    public ResponseEntity buscarTodos() throws ResourceNotFoundException {
        List<Dentista> dentistaList = repository.findAll();
        if (dentistaList.isEmpty()){
            throw  new ResourceNotFoundException("Nenhum dentista cadastrado");
        }
        List<DentistaResponseDTO> dentistaResponseDTOList = new ArrayList<>();
        for (Dentista dentista : dentistaList){
            dentistaResponseDTOList.add(mapper.convertValue(dentista, DentistaResponseDTO.class));
        }

        return new ResponseEntity(dentistaResponseDTOList,HttpStatus.OK);
    }

    public void excluir(String matricula) throws ResourceNotFoundException {
        try{
            Dentista dentista = repository.findByMatricula(matricula);
            repository.delete(dentista);

            logger.info("Dentista excluído com sucesso!");
        }catch (Exception exception){
            logger.error("Dentista não encontrado");
            throw  new ResourceNotFoundException("Dentista não encontrado");
        }
    }

    public ResponseEntity alterar(DentistaResponseDTO dentistaResponseDTO) throws ResourceNotFoundException {
        logger.info("Alterando dentista");
        try {
            Dentista dentistaAlterarado = repository.findByMatricula(dentistaResponseDTO.getMatricula());
            if (dentistaResponseDTO.getNome() != null) {
                dentistaAlterarado.setNome(dentistaResponseDTO.getNome());
            }
            if (dentistaResponseDTO.getSobrenome() != null) {
                dentistaAlterarado.setSobrenome(dentistaResponseDTO.getSobrenome());
            }
            if (dentistaResponseDTO.getUsuario().getUsername() != null) {
                dentistaAlterarado.getUsuario().setUsername(dentistaResponseDTO.getUsuario().getUsername());
            }
            return new ResponseEntity(mapper.convertValue(
                                                        repository.save(dentistaAlterarado),
                                                        DentistaResponseDTO.class)
                                        ,HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Erro ao alterar dentista: "+ex.getMessage());
        }
    }


}

