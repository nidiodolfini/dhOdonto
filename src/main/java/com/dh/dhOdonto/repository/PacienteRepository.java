package com.dh.dhOdonto.repository;

import com.dh.dhOdonto.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByMatricula(String matricula);

    void deleteByMatricula(String matricula);
}