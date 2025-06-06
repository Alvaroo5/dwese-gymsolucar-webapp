package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotEmpty
    @Size(max = 50)
    private String email;

    @NotEmpty
    @Size(max = 30)
    private String username;

    @NotEmpty
    @Size(min = 8)
    private String password;

    @NotEmpty
    @Size(max = 50)
    private String nombre;

    @NotEmpty
    @Size(max = 50)
    private String apellidos;

    private LocalDate fechaNacimiento;

    @Size(max = 20)
    private String telefono;
}