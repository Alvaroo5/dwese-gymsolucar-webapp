package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.controllers;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos.ReservaClaseDTO;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Clase;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.ReservaClase;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Usuario;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.ClaseRepository;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.ReservaClaseRepository;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gestor")
@PreAuthorize("hasRole('ROLE_GESTOR')")
public class GestorController {

    private static final Logger logger = LoggerFactory.getLogger(GestorController.class);

    private final ClaseRepository claseRepository;
    private final ReservaClaseRepository reservaClaseRepository;
    private final UsuarioRepository usuarioRepository;

    public GestorController(ClaseRepository claseRepository, ReservaClaseRepository reservaClaseRepository, UsuarioRepository usuarioRepository) {
        this.claseRepository = claseRepository;
        this.reservaClaseRepository = reservaClaseRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/clases")
    public ResponseEntity<List<Clase>> getClases() {
        List<Clase> clases = claseRepository.findAll();
        return ResponseEntity.ok(clases);
    }

    @PostMapping("/clases")
    public ResponseEntity<Clase> createClase(@RequestBody Clase nuevaClase) {
        if (nuevaClase.getNombreClase() == null || nuevaClase.getDiaSemana() == null ||
                nuevaClase.getHoraInicio() == null || nuevaClase.getHoraFin() == null ||
                nuevaClase.getAforoMaximo() == null) {
            return ResponseEntity.badRequest().build();
        }

        Clase claseGuardada = claseRepository.save(nuevaClase);
        return ResponseEntity.ok(claseGuardada);
    }

    @PutMapping("/clases/{id}")
    public ResponseEntity<Clase> updateClase(@PathVariable Integer id, @RequestBody Clase claseActualizada) {
        Optional<Clase> existingClase = claseRepository.findById(id);
        if (!existingClase.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (claseActualizada.getNombreClase() == null || claseActualizada.getDiaSemana() == null ||
                claseActualizada.getHoraInicio() == null || claseActualizada.getHoraFin() == null ||
                claseActualizada.getAforoMaximo() == null) {
            return ResponseEntity.badRequest().build();
        }

        Clase clase = existingClase.get();
        clase.setNombreClase(claseActualizada.getNombreClase());
        clase.setDiaSemana(claseActualizada.getDiaSemana());
        clase.setHoraInicio(claseActualizada.getHoraInicio());
        clase.setHoraFin(claseActualizada.getHoraFin());
        clase.setAforoMaximo(claseActualizada.getAforoMaximo());

        Clase claseGuardada = claseRepository.save(clase);
        return ResponseEntity.ok(claseGuardada);
    }

    @GetMapping("/clases/{id}")
    public ResponseEntity<Clase> getClaseById(@PathVariable Integer id) {
        Optional<Clase> clase = claseRepository.findById(id);
        return clase.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/clases/{id}/reservas")
    public ResponseEntity<Long> countReservasByClaseId(@PathVariable Integer id) {
        long count = reservaClaseRepository.countByIdClase(id);
        logger.info("Número de reservas para la clase con ID {}: {}", id, count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaClaseDTO>> getReservas() {
        List<ReservaClase> reservas = reservaClaseRepository.findAll();
        List<ReservaClaseDTO> reservasDTO = reservas.stream().map(reserva -> {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(reserva.getIdUsuario());
            String nombreUsuario = usuarioOpt.isPresent() ? usuarioOpt.get().getUsername() : "Desconocido";

            Optional<Clase> claseOpt = claseRepository.findById(reserva.getIdClase());
            if (claseOpt.isPresent()) {
                Clase clase = claseOpt.get();
                return new ReservaClaseDTO(
                        reserva.getId(),
                        nombreUsuario,
                        clase.getNombreClase(),
                        clase.getDiaSemana(),
                        clase.getHoraInicio(),
                        clase.getHoraFin()
                );
            } else {
                return null;
            }
        }).filter(dto -> dto != null).collect(Collectors.toList());

        logger.info("Cargadas {} reservas", reservasDTO.size());
        return ResponseEntity.ok(reservasDTO);
    }

    @DeleteMapping("/clases/{id}")
    @Transactional
    public ResponseEntity<Void> deleteClase(@PathVariable Integer id) {
        Optional<Clase> existingClase = claseRepository.findById(id);
        if (!existingClase.isPresent()) {
            logger.warn("Clase con ID {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }

        try {
            logger.info("Eliminando reservas asociadas a la clase con ID {}", id);
            reservaClaseRepository.deleteByIdClase(id);
            logger.info("Reservas eliminadas con éxito para la clase con ID {}", id);

            logger.info("Eliminando clase con ID {}", id);
            claseRepository.deleteById(id);
            logger.info("Clase con ID {} eliminada con éxito", id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al eliminar la clase con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}