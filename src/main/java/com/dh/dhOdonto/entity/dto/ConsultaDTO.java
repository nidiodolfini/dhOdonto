package com.dh.dhOdonto.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaDTO {
    @NotBlank
    private PacienteResponseDTO paciente;

    @NotBlank
    private DentistaResponseDTO dentista;

    @NotBlank
    private Timestamp dataHoraAgendamento;

}
