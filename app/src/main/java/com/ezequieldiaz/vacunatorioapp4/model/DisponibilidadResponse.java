package com.ezequieldiaz.vacunatorioapp4.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DisponibilidadResponse {
    private String fecha;
    private List<Horario> horarios;

    public String getFecha() {
        return fecha;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }
}
