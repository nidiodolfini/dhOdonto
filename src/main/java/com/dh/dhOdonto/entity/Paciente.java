package com.dh.dhOdonto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Paciente {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String sobrenome;
    @Column(nullable = false, unique = true)
    private String matricula;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario",nullable = false)
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco",nullable = false)
    private Endereco endereco;


    private Date dataDeCadastro;

    public void setMatricula(){
        this.matricula = UUID.randomUUID().toString();
    }
    public void setDataCadastro(){
        this.dataDeCadastro = new Date();
    }
    public void encodePassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
    }
}
