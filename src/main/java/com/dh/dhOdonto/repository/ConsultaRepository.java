package com.dh.dhOdonto.repository;

import com.dh.dhOdonto.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByDentistaMatriculaAndDataHoraAgendamento(String matricula, Timestamp dataHoraAgendamento);

    List<Consulta> findByDentistaMatricula(String matricula);
}
