package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.controllers;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Clase;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.ClaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ClaseRepository claseRepository;

    public PublicController(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    @GetMapping("/clases")
    public ResponseEntity<List<Clase>> getPublicClases() {
        List<Clase> clases = claseRepository.findAll();
        return ResponseEntity.ok(clases);
    }
}