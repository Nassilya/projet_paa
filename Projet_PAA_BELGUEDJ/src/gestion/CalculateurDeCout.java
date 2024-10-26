package gestion;
import model.Colon;
import java.util.List;

public class CalculateurDeCout {

    // Méthode pour calculer le nombre de colons jaloux
    public void calculerNombreColonsJaloux(List<Colon> colons) {
        int nombreJaloux = 0; // Compteur de colons jaloux

        // Parcours de tous les colons
        for (Colon colon : colons) {
            // Parcours des relations de chaque colon (ceux qu'il n'aime pas)
            for (Colon autreColon : colon.getRelations()) {
                // Si le colon préfère la ressource attribuée à un colon qu'il n'aime pas
                if (colon.getPreferences().indexOf(colon.getRessourceAttribuee()) > colon.getPreferences().indexOf(autreColon.getRessourceAttribuee())) {
                    nombreJaloux++; // Le colon est jaloux
                    break; // On sort de la boucle car un colon est compté comme jaloux une seule fois
                }
            }
        }

        // Affichage du nombre total de colons jaloux
        System.out.println("Nombre de colons jaloux : " + nombreJaloux);
    }
}
