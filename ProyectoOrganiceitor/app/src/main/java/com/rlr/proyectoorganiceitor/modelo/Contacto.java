package com.rlr.proyectoorganiceitor.modelo;

public class Contacto {
    private  String nombre;
    private  String numContac;


    public Contacto() {
    }

    public Contacto(String nombre, String numContac) {
        this.nombre = nombre;
        this.numContac = numContac;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumContac() {
        return numContac;
    }

    public void setNumContac(String numContac) {
        this.numContac = numContac;
    }
}
