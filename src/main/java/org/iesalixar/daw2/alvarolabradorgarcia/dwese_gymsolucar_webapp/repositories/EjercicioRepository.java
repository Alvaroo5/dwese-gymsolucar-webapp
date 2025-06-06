package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {

    @Query("SELECT e FROM Ejercicio e JOIN e.grupoMuscular gm WHERE " +
            "(:nombre IS NULL OR e.nombre LIKE %:nombre%) AND " +
            "(:grupoMuscular IS NULL OR gm.nombre LIKE %:grupoMuscular%)")
    List<Ejercicio> findByNombreAndGrupoMuscular(
            @Param("nombre") String nombre,
            @Param("grupoMuscular") String grupoMuscular);
}