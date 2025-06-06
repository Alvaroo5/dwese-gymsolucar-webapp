package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotEmpty
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El email debe tener un formato válido (texto@email.com).")
    private String email;

    @NotEmpty
    @Size(max = 30)
    @Pattern(regexp = "^\\S*$", message = "El nombre de usuario no puede contener espacios.")
    private String username;

    @NotEmpty
    @Size(min = 8)
    @Pattern(regexp = "^\\S*$", message = "La contraseña no puede contener espacios.")
    private String password;

    @NotEmpty
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "El nombre debe contener solo letras y espacios.")
    private String nombre;

    @NotEmpty
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Los apellidos deben contener solo letras y espacios.")
    private String apellidos;

    @NotEmpty(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate fechaNacimiento;

    @Size(max = 20)
    @Pattern(regexp = "^[0-9]*$", message = "El teléfono debe contener solo números.")
    private String telefono;
}