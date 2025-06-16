package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.controllers;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos.EjercicioDTO;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Ejercicio;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.GrupoMuscular;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.EjercicioRepository;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.GrupoMuscularRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gestor")
@PreAuthorize("hasRole('ROLE_GESTOR')")
public class EjercicioController {

    private static final Logger logger = LoggerFactory.getLogger(EjercicioController.class);

    private final EjercicioRepository ejercicioRepository;
    private final GrupoMuscularRepository grupoMuscularRepository;

    @Autowired
    public EjercicioController(EjercicioRepository ejercicioRepository, GrupoMuscularRepository grupoMuscularRepository) {
        this.ejercicioRepository = ejercicioRepository;
        this.grupoMuscularRepository = grupoMuscularRepository;
    }

    @GetMapping("/grupos-musculares")
    public ResponseEntity<List<String>> getGruposMusculares() {
        List<GrupoMuscular> grupos = grupoMuscularRepository.findAll();
        List<String> nombresGrupos = grupos.stream().map(GrupoMuscular::getNombre).collect(Collectors.toList());
        logger.info("Cargados {} grupos musculares", nombresGrupos.size());
        return ResponseEntity.ok(nombresGrupos);
    }

    @GetMapping("/ejercicios")
    public ResponseEntity<List<EjercicioDTO>> getEjercicios(@RequestParam(value = "nombre", required = false) String nombre, @RequestParam(value = "grupoMuscular", required = false) String grupoMuscular) {
        List<Ejercicio> ejercicios = ejercicioRepository.findByNombreAndGrupoMuscular(nombre, grupoMuscular);
        List<EjercicioDTO> ejerciciosDTO = ejercicios.stream().map(ejercicio -> new EjercicioDTO(
                ejercicio.getId(),
                ejercicio.getNombre(),
                ejercicio.getGrupoMuscular() != null ? ejercicio.getGrupoMuscular().getNombre() : "Sin grupo",
                ejercicio.getDescripcion()
        )).collect(Collectors.toList());

        logger.info("Cargados {} ejercicios", ejerciciosDTO.size());
        return ResponseEntity.ok(ejerciciosDTO);
    }

    @GetMapping("/ejercicios/{id}")
    public ResponseEntity<EjercicioDTO> getEjercicioById(@PathVariable Integer id) {
        Optional<Ejercicio> ejercicio = ejercicioRepository.findById(id);
        if (!ejercicio.isPresent()) {
            logger.warn("Ejercicio con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        EjercicioDTO dto = new EjercicioDTO(
                ejercicio.get().getId(),
                ejercicio.get().getNombre(),
                ejercicio.get().getGrupoMuscular() != null ? ejercicio.get().getGrupoMuscular().getNombre() : "Sin grupo",
                ejercicio.get().getDescripcion()
        );
        logger.info("Ejercicio con ID {} cargado correctamente", id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/ejercicios")
    public ResponseEntity<String> createEjercicio(@Valid @RequestBody EjercicioDTO ejercicioDTO) {
        Optional<GrupoMuscular> grupoMuscularOpt = grupoMuscularRepository.findByNombre(ejercicioDTO.getGrupoMuscular());
        if (grupoMuscularOpt.isEmpty()) {
            logger.warn("Grupo muscular '{}' no encontrado", ejercicioDTO.getGrupoMuscular());
            return ResponseEntity.badRequest().body("Grupo muscular no válido");
        }

        Ejercicio nuevoEjercicio = new Ejercicio();
        nuevoEjercicio.setNombre(ejercicioDTO.getNombre());
        nuevoEjercicio.setDescripcion(ejercicioDTO.getDescripcion());
        nuevoEjercicio.setGrupoMuscular(grupoMuscularOpt.get());
        nuevoEjercicio.setImagenUrl(null);

        Ejercicio ejercicioGuardado = ejercicioRepository.save(nuevoEjercicio);
        logger.info("Ejercicio creado con ID {}", ejercicioGuardado.getId());
        return ResponseEntity.status(201).body("Ejercicio creado exitosamente");
    }

    @PutMapping("/ejercicios/{id}")
    public ResponseEntity<String> updateEjercicio(@PathVariable Integer id, @Valid @RequestBody EjercicioDTO ejercicioDTO) {
        Optional<Ejercicio> existingEjercicio = ejercicioRepository.findById(id);
        if (!existingEjercicio.isPresent()) {
            logger.warn("Ejercicio con ID {} no encontrado", id);
            return ResponseEntity.status(404).body("Ejercicio no encontrado");
        }

        Optional<GrupoMuscular> grupoMuscularOpt = grupoMuscularRepository.findByNombre(ejercicioDTO.getGrupoMuscular());
        if (grupoMuscularOpt.isEmpty()) {
            logger.warn("Grupo muscular '{}' no encontrado", ejercicioDTO.getGrupoMuscular());
            return ResponseEntity.badRequest().body("Grupo muscular no válido");
        }

        Ejercicio ejercicio = existingEjercicio.get();
        ejercicio.setNombre(ejercicioDTO.getNombre());
        ejercicio.setGrupoMuscular(grupoMuscularOpt.get());
        ejercicio.setDescripcion(ejercicioDTO.getDescripcion());

        Ejercicio ejercicioGuardado = ejercicioRepository.save(ejercicio);
        logger.info("Ejercicio con ID {} actualizado", id);
        return ResponseEntity.ok("Ejercicio actualizado exitosamente");
    }

    @DeleteMapping("/ejercicios/{id}")
    public ResponseEntity<Void> deleteEjercicio(@PathVariable Integer id) {
        Optional<Ejercicio> existingEjercicio = ejercicioRepository.findById(id);
        if (!existingEjercicio.isPresent()) {
            logger.warn("Ejercicio con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }

        try {
            logger.info("Eliminando ejercicio con ID {}", id);
            ejercicioRepository.deleteById(id);
            logger.info("Ejercicio con ID {} eliminado con éxito", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al eliminar el ejercicio con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}