package gestion;
import model.Colon;
import java.util.ArrayList;
import java.util.List;

public class GestionRelations {
	private List<Colon> colons = new ArrayList<>();
	
	 /*
     * @Auteur: BELGUEDJ NASSILYA
     * Ajoute un colon à la liste des colons (s'il n'est pas déjà présent)
     */
	public void ajouterColon(Colon colon) {
        if (!colons.contains(colon)) {
            colons.add(colon);
          //TEST Affichage 
            /*System.out.println("Colon " + colon.getNom() + " ajouté à la colonie.");
        } else {
            System.out.println("Le colon " + colon.getNom() + " existe déjà.");
            */
        }
        
    }
    
	
	/*
     * @Auteur: BELGUEDJ NASSILYA
     * Ajoute une relation symétrique "ne s'aiment pas" entre deux colons
     * Cela signifie que chaque colon est ajouté à la liste des relations de l'autre
     */
    public void ajouterRelation(Colon colon1, Colon colon2) {
        if (colon1 != null && colon2 != null) { //vérifie si colon1 et colon2 existe
            colon1.ajouterRelation(colon2);
            colon2.ajouterRelation(colon1);
            System.out.println("Relation ajoutée entre " + colon1.getNom() + " et " + colon2.getNom() +" avec succés !");
        } else {
            System.out.println("Erreur : Un ou les deux colons n'existent pas ");
        }
    }
    /*
     * @Auteur: BELGUEDJ NASSILYA
     * Affiche les relations entre les colons
     * Pour chaque colon affiche la liste des colons avec lesquels il a une relation "ne s'aiment pas"
     */
    public void afficherRelations() {
        for (Colon colon : colons) {
            System.out.println("Le colon " + colon.getNom() + " n'aime pas : " + colon.getRelations());
        }
    }
}
