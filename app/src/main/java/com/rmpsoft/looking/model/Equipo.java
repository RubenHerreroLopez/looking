package com.rmpsoft.looking.model;

public class Equipo {

    String uid;
    String correo;
    String password;
    String equipo;
    String municipio;
    String deporte;
    String horario;
    String imagen;
    String categoria;

    public Equipo() {
    }

    public Equipo(String uid, String correo, String password, String equipo, String municipio, String deporte,
                  String horario, String imagen, String categoria) {
        this.uid = uid;
        this.correo = correo;
        this.password = password;
        this.equipo = equipo;
        this.municipio = municipio;
        this.deporte = deporte;
        this.horario = horario;
        this.imagen = imagen;
        this.categoria = categoria;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
