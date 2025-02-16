package com.ezequieldiaz.vacunatorioapp4.model;

import java.io.Serializable;
import java.time.LocalDate;

public class LoteProveedor implements Serializable {
    private int id;
    private int numeroDeLote;
    private int laboratorioId;
    private int tipoDeVacunaId;
    private int cantidadDeVacunas;
    private LocalDate fechaDeVencimiento;
    private boolean estado;

    public LoteProveedor() {}

    public LoteProveedor(int id, int numeroDeLote, int laboratorioId, int tipoDeVacunaId, int cantidadDeVacunas, LocalDate fechaDeVencimiento, boolean estado) {
        this.id = id;
        this.numeroDeLote = numeroDeLote;
        this.laboratorioId = laboratorioId;
        this.tipoDeVacunaId = tipoDeVacunaId;
        this.cantidadDeVacunas = cantidadDeVacunas;
        this.fechaDeVencimiento = fechaDeVencimiento;
        this.estado = estado;
    }

    public LoteProveedor(int numeroDeLote, int laboratorioId, int tipoDeVacunaId, int cantidadDeVacunas, LocalDate fechaDeVencimiento, boolean estado) {
        this.numeroDeLote = numeroDeLote;
        this.laboratorioId = laboratorioId;
        this.tipoDeVacunaId = tipoDeVacunaId;
        this.cantidadDeVacunas = cantidadDeVacunas;
        this.fechaDeVencimiento = fechaDeVencimiento;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroDeLote() {
        return numeroDeLote;
    }

    public void setNumeroDeLote(int numeroDeLote) {
        this.numeroDeLote = numeroDeLote;
    }

    public int getLaboratorioId() {
        return laboratorioId;
    }

    public void setLaboratorioId(int laboratorioId) {
        this.laboratorioId = laboratorioId;
    }

    public int getTipoDeVacunaId() {
        return tipoDeVacunaId;
    }

    public void setTipoDeVacunaId(int tipoDeVacunaId) {
        this.tipoDeVacunaId = tipoDeVacunaId;
    }

    public int getCantidadDeVacunas() {
        return cantidadDeVacunas;
    }

    public void setCantidadDeVacunas(int cantidadDeVacunas) {
        this.cantidadDeVacunas = cantidadDeVacunas;
    }

    public LocalDate getFechaDeVencimiento() {
        return fechaDeVencimiento;
    }

    public void setFechaDeVencimiento(LocalDate fechaDeVencimiento) {
        this.fechaDeVencimiento = fechaDeVencimiento;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
