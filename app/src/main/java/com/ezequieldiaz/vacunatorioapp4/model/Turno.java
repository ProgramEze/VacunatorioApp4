package com.ezequieldiaz.vacunatorioapp4.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Turno implements Serializable {
    private int id;
    private int pacienteId;
    private int tipoDeVacunaId;
    private int tutorId;
    private int agenteId;
    private int aplicacionId;
    private String cita;
    private String relacionTutor;
    private Paciente paciente;
    private TipoDeVacuna tipoDeVacuna;
    private Tutor tutor;
    private Agente agente;
    private Aplicacion aplicacion;

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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public TipoDeVacuna getTipoDeVacuna() {
        return tipoDeVacuna;
    }

    public void setTipoDeVacuna(TipoDeVacuna tipoDeVacuna) {
        this.tipoDeVacuna = tipoDeVacuna;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    @NonNull
    @Override
    public String toString() {
        return "Turno: " + id + " - Paciente: " + pacienteId + " - Tipo de Vacuna: " + tipoDeVacunaId + " - Tutor: " + tutorId + " - Agente: " + agenteId + " - Aplicación: " + aplicacionId + " - Cita: " + cita + " - Relación Tutor: " + relacionTutor;
    }
}
