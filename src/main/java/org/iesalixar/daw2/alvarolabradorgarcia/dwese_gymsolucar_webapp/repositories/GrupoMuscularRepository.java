package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.GrupoMuscular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrupoMuscularRepository extends JpaRepository<GrupoMuscular, Integer> {
    @Query("SELECT gm FROM GrupoMuscular gm WHERE UPPER(gm.nombre) = UPPER(:nombre)")
    Optional<GrupoMuscular> findByNombre(@Param("nombre") String nombre);
}