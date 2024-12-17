package parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import model.Colon;
import model.Ressource;
import gestion.GestionAffectation;

public class SauvegardeFichier {

    /**
     * Sauvegarde l'affectation des colons aux ressources dans un fichier texte.
     * 
     * @param cheminFichier Le chemin complet du fichier dans lequel sauvegarder.
     * @param gestionAffectation L'objet GestionAffectation contenant les données à sauvegarder.
     * @throws IOException En cas d'erreur lors de l'écriture du fichier.
     */
    
	
	public static void sauvegarderAffectation(String cheminFichier, List<Ressource> ressourcesAttribuees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            for (Ressource ressource : ressourcesAttribuees) {
                writer.write(ressource.toString()); // Assurez-vous que Ressource a une méthode toString() bien définie
                writer.newLine();
            }
            System.out.println("Affectations sauvegardées dans le fichier : " + cheminFichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

	
	
	
}
