package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
