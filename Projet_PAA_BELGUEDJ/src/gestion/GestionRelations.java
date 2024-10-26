package gestion;
import model.Colon;
import java.util.ArrayList;
import java.util.List;

public class GestionRelations {
	private List<Colon> colons = new ArrayList<>();
    
	//pour la symétrie 
    public void ajouterRelation(Colon colon1, Colon colon2) {
        if (colon1 != null && colon2 != null) { //vérifie si colon1 et colon2 existe
            colon1.ajouterRelation(colon2);
            colon2.ajouterRelation(colon1);
            System.out.println("Relation ajoutée entre " + colon1.getNom() + " et " + colon2.getNom() +"avec succés !");
        } else {
            System.out.println("Erreur : Un ou les deux colons n'existent pas ");
        }
    }

    public void afficherRelations() {
        for (Colon colon : colons) {
            System.out.println("Le colon " + colon.getNom() + " n'aime pas : " + colon.getRelations());
        }
    }
}
