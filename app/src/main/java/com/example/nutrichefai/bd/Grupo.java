package com.example.nutrichefai.bd;

public class Grupo {
    private int id;
    private String name;
    private String imageName;

    public Grupo(int id, String name, String imageName) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }
}