package com.ezequieldiaz.vacunatorioapp4.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Laboratorio implements Serializable {
    private int id;
    private String nombre;
    private String pais;
    private String email;
    private String telefono;
    private String direccion;
    private boolean estado;

    public Laboratorio() {}

    public Laboratorio(int id, String nombre, String pais, String email, String telefono, String direccion, boolean estado){
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estado = estado;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @NonNull
    @Override
    public String toString() {
        return "Laboratorio: " + nombre + " - Pais: " + pais;
    }
}
