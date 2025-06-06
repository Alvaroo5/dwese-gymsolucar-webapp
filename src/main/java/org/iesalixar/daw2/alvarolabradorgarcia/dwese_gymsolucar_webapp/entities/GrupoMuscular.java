package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "grupos_musculares")
public class GrupoMuscular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    public GrupoMuscular() {
    }

    public GrupoMuscular(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
}