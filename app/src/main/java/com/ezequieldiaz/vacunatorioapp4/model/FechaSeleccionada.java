package com.ezequieldiaz.vacunatorioapp4.model;

import java.io.Serializable;

public class FechaSeleccionada implements Serializable {
    private int mes;
    private int anio;

    public FechaSeleccionada(int mes, int anio) {
        this.mes = mes;
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }
}
