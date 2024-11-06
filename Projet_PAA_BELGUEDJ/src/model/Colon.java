package model;
import java.util.ArrayList;
import java.util.List;

public class Colon {
    private String nom;
    private int [] prf = new int[0];
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
   
    /*
     * @Auteur: BELGUEDJ NASSILYA
     * ajout d'une relation pour un colon spécifique
     * A.ajouterRelation(B)
     */
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

    public void setRessourceAttribuee(Ressource ressourceAttribuee) {
        this.ressourceAttribuee = ressourceAttribuee;
        System.out.println("Ressource " + (ressourceAttribuee != null ? ressourceAttribuee.getId() : "Aucune") 
                           + " attribuée à " + nom);
    }


    public void setPreferences(List<Ressource> preferences) {
        this.preferences = preferences;
        System.out.println("Les préférences pour " + nom + " ont été définies.");
    }
   /* public void setPreferences(int []prf) {
    	this.prf=prf;
    	System.out.println("Les préférences pour " + nom + " ont été définies.");
    }*/
    public int[] getpreferences() {
    	return prf;
    }
    
    /*
     * @Auteur: BELGUEDJ NASSILYA
     * Ajout de la méthode toString pour afficher le nom du colon
     * pour ne pas retourner l'objet type"[model.Colon@266474c2, model.Colon@6f94fa3e]" quand j'appelle afficherRelations
     */
    @Override
    public String toString() {
        return nom;
    }
}
