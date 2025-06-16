package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

public class RankingDTO {
    private Integer idUsuario;
    private String username;
    private Long puntosTotales;

    public RankingDTO(Integer idUsuario, String username, Long puntosTotales) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.puntosTotales = puntosTotales;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPuntosTotales() {
        return puntosTotales;
    }

    public void setPuntosTotales(Long puntosTotales) {
        this.puntosTotales = puntosTotales;
    }
}