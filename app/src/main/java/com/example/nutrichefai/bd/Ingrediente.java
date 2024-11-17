package com.example.nutrichefai.bd;

public class Ingrediente {
    private int id;
    private String nombre;
    private String descripcion;
    private String imageName;

    public Ingrediente(int id, String nombre, String descripcion, String imageName) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImageName() {
        return imageName;
    }
}