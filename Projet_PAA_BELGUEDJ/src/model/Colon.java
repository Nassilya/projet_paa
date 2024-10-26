package model;
import java.util.ArrayList;
import java.util.List;

public class Colon {
    private String nom;
    private List<Ressource> preferences;
    private List<Colon> relations;
    private Ressource ressourceAttribuee;

    public Colon(String nom) {
        this.nom = nom;
        this.preferences = new ArrayList<>();
        this.relations = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public List<Ressource> getPreferences() {
        return preferences;
    }

    public void ajouterPreferences(List<Ressource> preferences) {
        this.preferences = preferences;
    }
    //ajout d'une relation pour un colon spécifique
    //A.ajouterRelation(B)
    public void ajouterRelation(Colon autreColon) {
        if (!relations.contains(autreColon)) {
            relations.add(autreColon);
        }
    }

    public List<Colon> getRelations() {
        return relations;
    }

    public Ressource getRessourceAttribuee() {
        return ressourceAttribuee;
    }

    public void setRessourceAttribuee(Ressource ressourceAttribuee) { //modifier you know 
        this.ressourceAttribuee = ressourceAttribuee;
    }
}
