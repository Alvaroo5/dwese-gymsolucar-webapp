package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos;

public class ProgressStatsDTO {
    private long puntosAcumulados;
    private long clasesAsistidas;
    private long ejerciciosRealizados;

    public ProgressStatsDTO(long puntosAcumulados, long clasesAsistidas, long ejerciciosRealizados) {
        this.puntosAcumulados = puntosAcumulados;
        this.clasesAsistidas = clasesAsistidas;
        this.ejerciciosRealizados = ejerciciosRealizados;
    }

    // Getters y setters
    public long getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(long puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public long getClasesAsistidas() {
        return clasesAsistidas;
    }

    public void setClasesAsistidas(long clasesAsistidas) {
        this.clasesAsistidas = clasesAsistidas;
    }

    public long getEjerciciosRealizados() {
        return ejerciciosRealizados;
    }

    public void setEjerciciosRealizados(long ejerciciosRealizados) {
        this.ejerciciosRealizados = ejerciciosRealizados;
    }
}