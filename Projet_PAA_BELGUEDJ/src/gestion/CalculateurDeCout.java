package gestion;
import model.Colon;
import model.Ressource;

import java.util.List;

public class CalculateurDeCout {
    /**
     * @Auteur: BELGUEDJ NASSILYA
     * Méthode pour calculer nombre de jaloux et afficher ce nombre
     * Un colon est jaloux uniquement si un voisin reçoit une ressource qu'il aurait préféré à la sienne
     * @param colons             une liste de colons.
     * @return le nombre de jaloux.
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

	public boolean estJaloux(Colon colon, List<Colon> colons) {
	    Ressource ressourceAttribuee = colon.getRessourceAttribuee();

	    // Vérifie si un colon qu'il déteste a une ressource qu'il préfère
	    for (Colon deteste : colon.getRelations()) {
	        Ressource ressourceDeteste = deteste.getRessourceAttribuee();
	        if (ressourceDeteste != null && estPreferee(colon, ressourceDeteste, ressourceAttribuee)) {
	            return true;
	        }
	    }

	    return false; // Aucun critère de jalousie trouvé
	}


	private boolean estPreferee(Colon colon, Ressource ressourceDeteste, Ressource ressourceAttribuee) {
	    List<Ressource> preferences = colon.getPreferences();

	    // Vérifie que les ressources et les préférences ne sont pas nulles
	    if (preferences == null || preferences.isEmpty() || ressourceDeteste == null || ressourceAttribuee == null) {
	        return false; // Aucun critère de préférence ne peut être vérifié
	    }

	    // Récupère les indices des ressources dans les préférences
	    int indexDeteste = preferences.indexOf(ressourceDeteste);
	    int indexAttribuee = preferences.indexOf(ressourceAttribuee);

	    // Vérifie si la ressource détestée est plus haut dans les préférences que la ressource attribuée
	    return indexDeteste != -1 && indexAttribuee != -1 && indexDeteste < indexAttribuee;
	}



}
















