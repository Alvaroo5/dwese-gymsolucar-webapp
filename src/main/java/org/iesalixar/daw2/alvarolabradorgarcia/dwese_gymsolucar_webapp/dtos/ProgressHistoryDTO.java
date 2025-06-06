package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

public class ProgressHistoryDTO {
    private String fecha;
    private String actividad;
    private long puntosObtenidos;

    public ProgressHistoryDTO(String fecha, String actividad, long puntosObtenidos) {
        this.fecha = fecha;
        this.actividad = actividad;
        this.puntosObtenidos = puntosObtenidos;
    }

    // Getters y setters
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public long getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public void setPuntosObtenidos(long puntosObtenidos) {
        this.puntosObtenidos = puntosObtenidos;
    }
}