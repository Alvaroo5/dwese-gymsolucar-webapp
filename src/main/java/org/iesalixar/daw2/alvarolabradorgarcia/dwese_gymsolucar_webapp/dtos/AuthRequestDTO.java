package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {
    @NotEmpty
    @Pattern(regexp = "^\\S*$", message = "El nombre de usuario no puede contener espacios.")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^\\S*$", message = "La contraseña no puede contener espacios.")
    private String password;
}