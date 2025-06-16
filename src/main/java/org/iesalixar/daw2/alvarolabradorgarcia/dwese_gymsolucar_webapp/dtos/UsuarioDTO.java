package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

public class UsuarioDTO {
    private Integer id;
    private String username;
    private boolean estadoCuenta;

    public UsuarioDTO(Integer id, String username, boolean estadoCuenta) {
        this.id = id;
        this.username = username;
        this.estadoCuenta = estadoCuenta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(boolean estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }
}