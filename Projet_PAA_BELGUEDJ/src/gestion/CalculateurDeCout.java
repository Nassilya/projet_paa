package gestion;
import model.Colon;
import model.Ressource;

import java.util.List;

public class CalculateurDeCout {
	/*
     * @Auteur: BELGUEDJ NASSILYA
     * Méthode pour calculer nombre de jaloux et afficher ce nombre
     * Un colon est jaloux uniquement si un voisin reçoit une ressource qu'il aurait préféré à la sienne
     */
	public int calculerNombreColonsJaloux(List<Colon> colons) {
	    int nombreJaloux = 0;

	    for (Colon colon : colons) {
	        Ressource ressourceAttribuee = colon.getRessourceAttribuee();
	        if (ressourceAttribuee == null) continue;

	        boolean estJaloux = false;

	        // Parcourir chaque voisin (colon avec une relation "ne s'aime pas")
	        for (Colon autreColon : colon.getRelations()) {
	            Ressource ressourceAutreColon = autreColon.getRessourceAttribuee();
	            if (ressourceAutreColon == null) continue;

	            // Vérifier si le colon préfère la ressource du voisin à la sienne
	            if (colon.getPreferences().indexOf(ressourceAutreColon) < colon.getPreferences().indexOf(ressourceAttribuee)) {
	                estJaloux = true;
	                break; // Sortir dès qu'on trouve un voisin qui rend le colon jaloux
	            }
	        }

	        if (estJaloux) {
	            nombreJaloux++;
	        }
	    }

	    System.out.println("Nombre de colons jaloux : " + nombreJaloux);
	    return nombreJaloux;
	}

}
















