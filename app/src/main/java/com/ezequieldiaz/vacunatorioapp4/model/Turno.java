package com.ezequieldiaz.vacunatorioapp4.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Turno implements Serializable {
    private int id;
    private int pacienteId;
    private int tipoDeVacunaId;
    private int tutorId;
    private int agenteId;
    private int aplicacionId;
    private String cita;
    private String relacionTutor;

    public Turno() {}

    public Turno(int id, int pacienteId, int tipoDeVacunaId, int tutorId, int agenteId, int aplicacionId, String cita, String relacionTutor) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.tipoDeVacunaId = tipoDeVacunaId;
        this.tutorId = tutorId;
        this.agenteId = agenteId;
        this.aplicacionId = aplicacionId;
        this.cita = cita;
        this.relacionTutor = relacionTutor;
    }

    public Turno(int pacienteId, int tipoDeVacunaId, int tutorId, int agenteId, int aplicacionId, String cita, String relacionTutor) {
        this.pacienteId = pacienteId;
        this.tipoDeVacunaId = tipoDeVacunaId;
        this.tutorId = tutorId;
        this.agenteId = agenteId;
        this.aplicacionId = aplicacionId;
        this.cita = cita;
        this.relacionTutor = relacionTutor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public int getTipoDeVacunaId() {
        return tipoDeVacunaId;
    }

    public void setTipoDeVacunaId(int tipoDeVacunaId) {
        this.tipoDeVacunaId = tipoDeVacunaId;
    }

    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public int getAgenteId() {
        return agenteId;
    }

    public void setAgenteId(int agenteId) {
        this.agenteId = agenteId;
    }

    public int getAplicacionId() {
        return aplicacionId;
    }

    public void setAplicacionId(int aplicacionId) {
        this.aplicacionId = aplicacionId;
    }

    public String getCita() {
        return cita;
    }

    public void setCita(String cita) {
        this.cita = cita;
    }

    public String getRelacionTutor() {
        return relacionTutor;
    }

    public void setRelacionTutor(String relacionTutor) {
        this.relacionTutor = relacionTutor;
    }
}
