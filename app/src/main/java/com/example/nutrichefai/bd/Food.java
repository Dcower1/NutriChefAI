package com.example.nutrichefai.bd;

public class Food {
    private int id;
    private String name;
    private String info;
    private String imageName;
    private int tipoId; // Nueva variable para relacionar con TipoAlimento

    public Food(int id, String name, String info, String imageName, int tipoId) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.imageName = imageName;
        this.tipoId = tipoId;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getImageName() {
        return imageName;
    }

    public int getTipoId() {
        return tipoId;
    }
}