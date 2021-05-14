package com.rlr.proyectoandroid.modelo;

import java.util.List;

public class Usuario {
    private String uid;
    private String nombre;
    private List<Integer> eventos;

    public Usuario() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Integer> getEventos() {
        return eventos;
    }

    public void setEventos(List<Integer> eventos) {
        this.eventos = eventos;
    }
}
