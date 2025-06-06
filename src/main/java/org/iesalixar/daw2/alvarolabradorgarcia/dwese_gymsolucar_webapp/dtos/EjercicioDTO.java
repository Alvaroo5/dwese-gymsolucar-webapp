package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

import jakarta.validation.constraints.NotBlank;

public class EjercicioDTO {
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El grupo muscular es obligatorio")
    private String grupoMuscular;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    public EjercicioDTO() {
    }

    public EjercicioDTO(Integer id, String nombre, String grupoMuscular, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}