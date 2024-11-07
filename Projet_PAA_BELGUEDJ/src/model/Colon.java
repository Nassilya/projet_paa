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
     * Ajoute une relation entre ce colon et un autre colon spécifique
     * Exemple d'utilisation : A.ajouterRelation(B)
     */
    public void ajouterRelation(Colon autreColon) {
        if (!relations.contains(autreColon)) {
            relations.add(autreColon);
        }
    }
    /*
     * @Auteur: BELGUEDJ NASSILYA
     * Retourne la liste des colons en relation avec ce colon
     */
    public List<Colon> getRelations() {
        return relations;
    }
    /*
     * @Auteur: BELGUEDJ NASSILYA
     * Retourne la ressource actuellement attribuée à ce colon
     */
    public Ressource getRessourceAttribuee() {
        return ressourceAttribuee;
    }
    
    
    /*
     * @Auteur: BELGUEDJ NASSILYA
     * Attribue une ressource spécifique à ce colon et affiche un message indiquant la ressource attribuée
     */
    public void setRessourceAttribuee(Ressource ressourceAttribuee) {
        this.ressourceAttribuee = ressourceAttribuee;
        System.out.println("Ressource " + (ressourceAttribuee != null ? ressourceAttribuee.getId() : "Aucune") 
                           + " attribuée à " + nom);
    }

    /*
     * @Auteur: BELGUEDJ NASSILYA
     * Définit la liste des préférences pour ce colon et affiche un message de confirmation
     */
    public void setPreferences(List<Ressource> preferences) {
        this.preferences = preferences;
        System.out.println("Les préférences pour " + nom + " ont été définies");
    }
   
    /*
     *  @Auteur: BELGUEDJ NASSILYA
     * Retourne les préférences sous forme de tableau d'entiers
     */
    public int[] getpreferences() {
    	return prf;
    }

    
    /*
     * @Auteur: BELGUEDJ NASSILYA
     * Ajoute une méthode toString pour afficher le nom du colon
     * Cela permet d'éviter de retourner un identifiant d'objet de type "[model.Colon@266474c2]"
     * lorsque la méthode afficherRelations est appelée
     */
    @Override
    public String toString() {
        return nom;
    }
    
    
    /* public void setPreferences(int []prf) {
	this.prf=prf;
	System.out.println("Les préférences pour " + nom + " ont été définies.");
}*/
    
}
