package com.dh.dhOdonto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String logradouro;
    @Column(nullable = false)
    private String numero;
    
    private String complemento;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String municipio;
    @Enumerated(EnumType.STRING)

    @Column(nullable = false)
    private Estado estado;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String pais;
}
