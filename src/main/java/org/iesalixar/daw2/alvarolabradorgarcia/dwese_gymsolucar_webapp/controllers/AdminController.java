package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.controllers;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos.UsuarioDTO;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Usuario;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UsuarioRepository usuarioRepository;

    public AdminController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(u -> new UsuarioDTO(u.getId(), u.getUsername(), u.isEstadoCuenta()))
                .collect(Collectors.toList());
        logger.info("Cargados {} usuarios", usuariosDTO.size());
        return ResponseEntity.ok(usuariosDTO);
    }

    @PutMapping("/usuarios/{id}/estado")
    public ResponseEntity<Void> updateEstadoCuenta(@PathVariable Integer id, @RequestBody Map<String, Boolean> body) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Usuario con ID {} no encontrado", id);
                    return new RuntimeException("Usuario no encontrado");
                });
        boolean estadoCuenta = body.get("estadoCuenta");
        usuario.setEstadoCuenta(estadoCuenta);
        usuarioRepository.save(usuario);
        logger.info("Usuario con ID {} actualizado a estadoCuenta: {}", id, estadoCuenta);
        return ResponseEntity.ok().build();
    }
}