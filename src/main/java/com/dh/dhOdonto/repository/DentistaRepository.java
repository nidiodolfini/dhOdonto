package com.dh.dhOdonto.repository;

import com.dh.dhOdonto.entity.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {
    Dentista findByMatricula(String matricula);
}
