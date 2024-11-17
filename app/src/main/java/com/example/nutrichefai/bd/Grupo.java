package com.example.nutrichefai.bd;

public class Grupo {
    private int idGrupo;
    private String nombreGrupo;
    private String imagenGrupo;

    public Grupo(int idGrupo, String nombreGrupo, String imagenGrupo) {
        this.idGrupo = idGrupo;
        this.nombreGrupo = nombreGrupo;
        this.imagenGrupo = imagenGrupo;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public String getImagenGrupo() {
        return imagenGrupo;
    }
}