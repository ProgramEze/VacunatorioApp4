package com.ezequieldiaz.vacunatorioapp4.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Paciente implements Serializable {
    private int id;
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaDeNacimiento;
    private Genero genero;

    public Paciente(){
    }

    public Paciente(int id, String dni, String nombre, String apellido, LocalDate fechaDeNacimiento, Genero genero) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.genero = genero;
    }

    public Paciente(String dni, String nombre, String apellido, LocalDate fechaDeNacimiento, Genero genero) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }
}
