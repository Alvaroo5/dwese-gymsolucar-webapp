package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaClaseDTO {
    private Integer id;
    private String usuario;
    private String clase;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;

    public ReservaClaseDTO(Integer id, String usuario, String clase, String diaSemana, String horaInicio, String horaFin) {
        this.id = id;
        this.usuario = usuario;
        this.clase = clase;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
}