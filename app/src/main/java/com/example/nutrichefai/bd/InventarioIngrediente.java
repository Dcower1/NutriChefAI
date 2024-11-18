package com.example.nutrichefai.bd;

public class InventarioIngrediente {
    private int id;
    private String nombre;
    private String descripcion;
    private String imageName;
    private String cantidad;

    public InventarioIngrediente(int id, String nombre, String descripcion, String imageName, String cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imageName = imageName;
        this.cantidad = cantidad;
    }
    // Getters
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

    public String getCantidad() {
        return cantidad;
    }

    // Setter para cantidad
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}