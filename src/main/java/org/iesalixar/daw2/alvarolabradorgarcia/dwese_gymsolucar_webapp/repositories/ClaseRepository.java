package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Clase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaseRepository extends JpaRepository<Clase, Integer> {
    List<Clase> findByNombreClaseIgnoreCase(String nombreClase);
}