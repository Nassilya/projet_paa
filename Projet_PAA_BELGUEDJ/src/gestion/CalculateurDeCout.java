package gestion;
import model.Colon;
//import model.Ressource;

import java.util.List;

public class CalculateurDeCout {

	@SuppressWarnings("unlikely-arg-type")
	public void calculerNombreColonsJaloux(List<Colon> colons) {
	    int nombreJaloux = 0;
	    for (Colon colon : colons) {
	        for (Colon autreColon : colon.getRelations()) {  // Parcours des relations d’hostilité
	            if (autreColon.getRessourceAttribuee() != null &&
	                colon.getPreferences().indexOf(autreColon.getRessourceAttribuee().getId()) <
	                colon.getPreferences().indexOf(colon.getRessourceAttribuee().getId())) {
	                // Si la ressource de l'autre colon est mieux classée dans les préférences de ce colon
	                nombreJaloux++;
	                break; // Sortir dès qu’un colon est jaloux d’un autre
	            }
	        }
	    }
	    System.out.println("Nombre de colons jaloux : " + nombreJaloux);
	}

       
}
