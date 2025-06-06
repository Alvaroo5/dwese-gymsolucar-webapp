package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "usuarios")
@EqualsAndHashCode(exclude = "usuarios")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios;

    public Rol(String nombre) {
        this.nombre = nombre;
    }
}
