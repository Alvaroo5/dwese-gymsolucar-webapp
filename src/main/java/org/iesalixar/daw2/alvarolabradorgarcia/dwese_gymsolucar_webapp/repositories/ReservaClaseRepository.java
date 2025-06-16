package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.ReservaClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaClaseRepository extends JpaRepository<ReservaClase, Integer> {
    long countByIdClase(Integer idClase);
    List<ReservaClase> findByIdUsuario(Integer idUsuario);
    List<ReservaClase> findByIdUsuarioAndIdClase(Integer idUsuario, Integer idClase);
    void deleteByIdClase(Integer idClase);
}