package com.dh.dhOdonto.service;

import com.dh.dhOdonto.entity.Consulta;
import com.dh.dhOdonto.entity.Dentista;
import com.dh.dhOdonto.entity.Paciente;
import com.dh.dhOdonto.entity.dto.ConsultaDTO;
import com.dh.dhOdonto.exceptions.CadastroInvalidoException;
import com.dh.dhOdonto.exceptions.ResourceNotFoundException;
import com.dh.dhOdonto.repository.ConsultaRepository;
import com.dh.dhOdonto.repository.DentistaRepository;
import com.dh.dhOdonto.repository.PacienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaService {
    @Autowired
    ConsultaRepository consultaRepository;
    @Autowired
    DentistaRepository dentistaRepository;
    @Autowired
    PacienteRepository pacienteRepository;

    ObjectMapper mapper = new ObjectMapper();

    Logger logger = Logger.getLogger(ConsultaService.class);

    //Metodo para cadastrar consulta
    public ResponseEntity cadastrar(ConsultaDTO consultaDTO) throws CadastroInvalidoException {
        //Usando o ObjectMapper para converter um DTO em Entidade
        Consulta consulta = mapper.convertValue(consultaDTO, Consulta.class);

        //chamamos um metodo para validar se a consulta segue as regras para ser cadastrada
        this.validaConsulta(consultaDTO);
        //Caso esteja tudo certo, buscamos o dentista e o paciente, e salvamos a consulta
        consulta.setDentista(dentistaRepository.findByMatricula(consulta.getDentista().getMatricula()));
        consulta.setPaciente(pacienteRepository.findByMatricula(consulta.getPaciente().getMatricula()));
        Consulta consulta1 = consultaRepository.save(consulta);
          ConsultaDTO consultaSalva = mapper.convertValue(consulta1, ConsultaDTO.class);
        logger.info("Consulta cadastrada com sucesso!");
          return new ResponseEntity(consultaSalva, HttpStatus.OK);

    }


    //Metodo que busca uma consulta pela Matricula do Dentista
    public ResponseEntity buscaPorMatricula(String matricula) throws ResourceNotFoundException {
        //Trazemos do banco lista de consulta do tentista
        List<Consulta> consultaList = consultaRepository.findByDentistaMatricula(matricula);
        List<ConsultaDTO> consultaDTOList = new ArrayList<>();
        //validamos se de fato a lista do banco não vem vazia
        if(consultaList.isEmpty()){
            //se vier vazia retornamos um erro
            logger.error("Nenhuma consulta cadastrada para esse dentista");
            throw  new ResourceNotFoundException("Nenhuma consulta cadastrada para esse dentista");
        }
        //Caso tenha Consultas para o dentista, convertemos cada consulta para uma DTO e retornamos ao cliente
        for (Consulta consulta: consultaList){
            consultaDTOList.add(mapper.convertValue(consulta,ConsultaDTO.class));
        }
        return new ResponseEntity(consultaDTOList,HttpStatus.OK);
    }

    //Metodo responsavel por deletar uma consulta
    public void deletar( String matricula,Timestamp dataConsulta) throws ResourceNotFoundException {
        try{
            //Buscamos se a consulta existe, se não existe ela da erro, e vai cair no catch
            List<Consulta> consulta = consultaRepository.findByDentistaMatriculaAndDataHoraAgendamento(matricula,dataConsulta);

            //Pegamos a consulta retornada e deletamos
            consultaRepository.delete(consulta.get(0));
            logger.info("Consulta excluida");
        }catch (Exception ex){
            logger.error("Consulta não encontrada");
            throw new ResourceNotFoundException("COnsulta não encontrada");
        }

    }
    public ResponseEntity buscaTodos() throws ResourceNotFoundException {
        List<Consulta> consultaList = consultaRepository.findAll();
        List<ConsultaDTO> consultaDTOList = new ArrayList<>();
        if(consultaList.isEmpty()){
            logger.error("Nenhuma consulta cadastrada");
            throw  new ResourceNotFoundException("Nenhuma consulta cadastrada");
        }
        for (Consulta consulta: consultaList){
            consultaDTOList.add(mapper.convertValue(consulta,ConsultaDTO.class));
        }
        return new ResponseEntity(consultaDTOList,HttpStatus.OK);
    }

    //Metodo que retorna todas as consultas
    private Consulta compararConsulta(Consulta consultaUsuario, Consulta consultaBanco) {
        Paciente paciente = (consultaUsuario.getPaciente() != null) ? consultaUsuario.getPaciente() : consultaBanco.getPaciente();
        Dentista dentista = (consultaUsuario.getDentista() != null) ? consultaUsuario.getDentista() : consultaBanco.getDentista();
       // LocalDateTime dataHora = (consultaUsuario.getDataHora() != null) ? consultaUsuario.getDataHora() : consultaBanco.getDataHora();

        return new Consulta(consultaUsuario.getId(), paciente, dentista, null);
    }

    //Aqui validamos se a consulta segue todos os requisitos para ser salva
    private void validaConsulta(ConsultaDTO consultaDTO) throws CadastroInvalidoException {
        //verificamos se o dentista existe
        Dentista dentista = dentistaRepository.findByMatricula(consultaDTO.getDentista().getMatricula());
        if (dentista == null){
            logger.error("Dentista não cadastrado");
            throw  new CadastroInvalidoException("Dentista não cadastrado");
        }

        //verificamos se o paciente existe
        Paciente paciente =  pacienteRepository.findByMatricula(consultaDTO.getPaciente().getMatricula());
       if(paciente == null){
           logger.error("Paciente não cadastrado");
           throw  new CadastroInvalidoException("Paciente não cadastrado");
       }
       //verificamos se a data da consulta e anterior a data de hoje
        if (consultaDTO.getDataHoraAgendamento().before(Timestamp.valueOf(LocalDateTime.now()))) {
            logger.error("A data da consulta tem que ser maior que a de hoje");
            throw new CadastroInvalidoException("A data da consulta tem que ser maior que a de hoje");
        }
        //Validamos se não tem nenhuma consulta cadastrada para esse horario
        if (!consultaRepository.findByDentistaMatriculaAndDataHoraAgendamento(consultaDTO.getDentista().getMatricula(), consultaDTO.getDataHoraAgendamento()).isEmpty()) {
            logger.error("Horario indisponivel para agendamento");
            throw new CadastroInvalidoException("Horario indisponivel para agendamento");
        }

    }
}

