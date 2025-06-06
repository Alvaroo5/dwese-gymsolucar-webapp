package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservas_clase")
public class ReservaClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_clase")
    private Integer idClase;

    @Column(name = "fecha_reserva")
    private LocalDate fechaReserva;

    @Column(name = "asistencia_confirmada")
    private Boolean asistenciaConfirmada;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdClase() {
        return idClase;
    }

    public void setIdClase(Integer idClase) {
        this.idClase = idClase;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Boolean getAsistenciaConfirmada() {
        return asistenciaConfirmada;
    }

    public void setAsistenciaConfirmada(Boolean asistenciaConfirmada) {
        this.asistenciaConfirmada = asistenciaConfirmada;
    }
}