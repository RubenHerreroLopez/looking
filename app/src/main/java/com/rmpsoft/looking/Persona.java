package com.rmpsoft.looking;

public class Persona {
    String nombre;
    String apellido;
    String id;

    public Persona(String id, String nombre, String apellido) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
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

    public String getId () {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

