package com.ezequieldiaz.vacunatorioapp4.model;

public class Disponibilidad {
    private String fecha;
    private int disponibles;

    public Disponibilidad(String fecha, int disponibles) {
        this.fecha = fecha;
        this.disponibles = disponibles;
    }

    public String getFecha() { return fecha; }
    public int getDisponibles() { return disponibles; }
}

