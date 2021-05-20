package com.rmpsoft.looking.model;

public class Anuncio {

    private String uid;
    private String estado;
    private String contacto;
    private String descripcion;
    private String equipo;
    private String deporte;
    private String posicion;
    private String municipio;

    public Anuncio () {

    }

    public Anuncio (String uid, String estado, String contacto, String descripcion, String equipo, String deporte, String posicion, String municipio) {
        this.uid = uid;
        this.estado = estado;
        this.contacto = contacto;
        this.descripcion = descripcion;
        this.equipo = equipo;
        this.deporte = deporte;
        this.posicion = posicion;
        this.municipio = municipio;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
