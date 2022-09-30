package com.dh.dhOdonto.entity.dto;

import com.dh.dhOdonto.entity.Endereco;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PacienteResponseDTO {
    @NotBlank
    private String nome;
    @NotBlank
    private String sobrenome;
    @NotBlank
    private String matricula;
    @NotBlank
    private UsuarioNonPasswordDTO usuario;

    private Endereco endereco;

    private Date dataDeCadastro;
}
