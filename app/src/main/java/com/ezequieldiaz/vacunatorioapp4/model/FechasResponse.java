// DisponibilidadResponse.java
package com.ezequieldiaz.vacunatorioapp4.model;

import java.io.Serializable;
import java.util.List;

public class FechasResponse implements Serializable {
    private String fecha;
    private List<HorariosResponse> horarios;

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public List<HorariosResponse> getHorarios() { return horarios; }
    public void setHorarios(List<HorariosResponse> horarios) { this.horarios = horarios; }
}
