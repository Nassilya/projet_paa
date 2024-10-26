package model;

public class Ressource {
    private int id;
    private String nom;

    public Ressource(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
}
