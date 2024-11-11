package com.example.nutrichefai.bd;

public class TipoAlimento {

    private int id;
    private String name;
    private String imageName;
    private int grupoId;

    public TipoAlimento(int id, String name, String imageName, int grupoId) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.grupoId = grupoId;
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

    public int getGrupoId() {
        return grupoId;
    }
}