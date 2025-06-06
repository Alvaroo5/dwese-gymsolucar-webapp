package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String message;
}
