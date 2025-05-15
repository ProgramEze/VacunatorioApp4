package com.ezequieldiaz.vacunatorioapp4.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class TipoDeVacuna implements Serializable {
    private int id;
    private String nombre;
    private String descripcion;

    public TipoDeVacuna() {}

    public TipoDeVacuna(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public TipoDeVacuna(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre;
    }
}
