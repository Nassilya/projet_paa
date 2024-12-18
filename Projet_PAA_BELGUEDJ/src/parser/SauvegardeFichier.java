package parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import model.Colon;
import model.Ressource;


public class SauvegardeFichier {

	/**
	 * @author NAGULESWARAN ALICIA
	 * @author BELGUEDJ NASSILYA
	 * Sauvegarde l'affectation des colons aux ressources dans un fichier texte
	 * 
	 * @param cheminFichier Le chemin complet du fichier dans lequel sauvegarder
	 * @param affectations  La map contenant les colons et leurs ressources attribuées
	 * @param cout          Le coût (nombre de colons jaloux) à inclure en début de fichier
	 */
	public static void sauvegarderAffectation(String cheminFichier, Map<Colon, Ressource> affectations, int cout) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
	        // Écrire le coût en première ligne
	        writer.write("Nombre de colons jaloux : " + cout);
	        writer.newLine();
	        writer.newLine(); // Ligne vide pour séparer

	        // Écrire les affectations
	        for (Map.Entry<Colon, Ressource> entry : affectations.entrySet()) {
	            String ligne = entry.getKey().getNom() + " : " + 
	                           (entry.getValue() != null ? entry.getValue().getId() : "Aucune");
	            writer.write(ligne);
	            writer.newLine();
	        }

	        System.out.println("[INFO] Affectations sauvegardées dans le fichier : " + cheminFichier);
	    } catch (IOException e) {
	        System.err.println("[ERREUR] Problème lors de la sauvegarde : " + e.getMessage());
	    }
	}

	
	
}
