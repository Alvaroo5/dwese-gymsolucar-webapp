package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
