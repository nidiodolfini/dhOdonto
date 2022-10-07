package com.dh.dhOdonto.repository;

import com.dh.dhOdonto.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    List<Perfil> findByDescricao(String admin);
}
