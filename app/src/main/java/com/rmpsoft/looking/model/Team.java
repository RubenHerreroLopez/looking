package com.rmpsoft.looking.model;

public class Team {

    String uid;
    String correo;
    String password;
    String equipo;
    String municipio;
    String deporte;
    String horario;
    String image;
    String categoria;

    public Team() {
    }

    public Team(String uid, String correo, String password, String equipo, String municipio, String deporte,
                String horario, String image, String categoria) {
        this.uid = uid;
        this.correo = correo;
        this.password = password;
        this.equipo = equipo;
        this.municipio = municipio;
        this.deporte = deporte;
        this.horario = horario;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String imagen) {
        this.image = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
