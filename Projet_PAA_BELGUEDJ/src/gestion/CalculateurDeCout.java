package gestion;
import model.Colon;
import model.Ressource;

import java.util.List;

public class CalculateurDeCout {

    public void calculerNombreColonsJaloux(List<Colon> colons) {
        int nombreJaloux = 0;
        
        for (Colon colon : colons) {
            Ressource ressourceAttribuee = colon.getRessourceAttribuee();
            if (ressourceAttribuee == null) continue;

            // Parcourir chaque relation (voisin) du colon
            for (Colon autreColon : colon.getRelations()) {
                Ressource ressourceAutreColon = autreColon.getRessourceAttribuee();
                if (ressourceAutreColon == null) continue;

                // Vérifier si la ressource de l'autre colon est mieux classée que celle du colon actuel
                if (colon.getPreferences().indexOf(ressourceAutreColon) < colon.getPreferences().indexOf(ressourceAttribuee)) {
                    nombreJaloux++;
                    break; // Un colon est jaloux si un seul voisin possède une meilleure ressource
                }
            }
        }

        System.out.println("Nombre de colons jaloux : " + nombreJaloux);
    }
}
















