package com.rlr.proyectoorganiceitor.modelo;

import java.io.Serializable;

public class Evento implements Serializable {
    private String userId;
    private String nombre;
    private String tipo;
    private String categoría;
    private String fecha;
    private String descripcion;
    private String imagen = "No hay";


    private boolean contratacion = false;
    private boolean invitacion = false;
    private boolean publicidad = false;

    public Evento() {
    }

    public Evento(String userId, String nombre, String tipo, String categoría, String fecha, String descripcion, String imagen, boolean contratacion, boolean publicidad) {
        this.userId = userId;
        this.nombre = nombre;
        this.tipo = tipo;
        this.categoría = categoría;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.contratacion = contratacion;
        this.publicidad = publicidad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoría() {
        return categoría;
    }

    public void setCategoría(String categoría) {
        this.categoría = categoría;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isContratacion() {
        return contratacion;
    }

    public void setContratacion(boolean contratacion) {
        this.contratacion = contratacion;
    }

    public boolean isInvitacion() {
        return invitacion;
    }

    public void setInvitacion(boolean invitacion) {
        this.invitacion = invitacion;
    }

    public boolean isPublicidad() {
        return publicidad;
    }

    public void setPublicidad(boolean publicidad) {
        this.publicidad = publicidad;
    }
}
