package utilitaires;

import model.Colon;
import model.Ressource;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Verificateur {
	/**
     * @Auteur: BELGUEDJ NASSILYA
     * Vérifier si une liste de ressources contient des doublons
     * @param preferences         La liste des identifiants des ressources préférées
     * @return true si la liste ne contient pas de doublons, false sinon
     */
    public static boolean verifierDoublons(List<Integer> preferences) {
        Set<Integer> uniquePreferences = new HashSet<>(preferences);
        return uniquePreferences.size() != preferences.size();
    }
   /**
     * @Auteur: BELGUEDJ NASSILYA
     * Vérifie si toutes les ressources de la liste existent dans l'intervalle [1, nombreRessources] et si leur nombre correspond exactement au nombre de ressources disponibles
     * @param preferences         La liste des identifiants des ressources préférées
     * @param ressourcesDisponibles La liste des ressources disponibles
     * @return true si toutes les ressources existent et si leur taille correspond, false sinon
     */
    public static boolean verifierRessourcesExistent(List<Integer> preferences, List<Ressource> ressourcesDisponibles) {
        int nombreRessources = ressourcesDisponibles.size();
        boolean valide = true;

        // Vérifier si le nombre de préférences correspond au nombre de ressources disponibles
        if (preferences.size() != nombreRessources) {
            System.out.println("Erreur : Le nombre de préférences doit être exactement " + nombreRessources + ".");
            valide = false;
        }

        // Vérifier chaque ID de ressource
        for (int id : preferences) {
            if (id < 1 || id > nombreRessources) {
            	
                System.out.println("Erreur : La ressource avec l'ID " + id + " doit être comprise entre 1 et " + nombreRessources + ".");
                valide = false;
                continue; // Passe à l'ID suivant, inutile de vérifier son existence
            }

            boolean trouve = ressourcesDisponibles.stream().anyMatch(r -> r.getId() == id);
            if (!trouve) {
                System.out.println("Erreur : La ressource avec l'ID " + id + " n'existe pas parmi les ressources disponibles.");
                valide = false;
            }
        }

        return valide; // Retourne true uniquement si toutes les vérifications passent
    }
    
    /**
     * @Auteur: BELGUEDJ NASSILYA
     * Vérifier si la taille des préférences correspond au nombre de ressources disponibles
     * @param preferences         La liste des identifiants des ressources préférées
     * @param nombreRessources    Nombre de ressources disponibles
     * @return true si la taille des préférences correspond au nombre de ressources disponibles, false sinon
     */
    public static boolean verifierTaillePreferences(List<Integer> preferences, int nombreRessources) {
        return preferences.size() == nombreRessources;
    }


    /**
     * @Auteur: BELGUEDJ NASSILYA
     * Vérifier que tous les colons ont une liste complète de préférences
     * @param clons               La liste des colons
     * @param nombreRessources    Nombre de ressources disponibles
     * @return true si la loste est complète, false sinon
     */
    public static boolean verifierPreferencesCompletes(List<Colon> colons, int nombreRessources) {
        for (Colon colon : colons) {
            if (colon.getPreferences().size() != nombreRessources) {
                System.out.println("Le colon " + colon.getNom() + " n'a pas une liste complète de préférences");
                return false;
            }
        }
        return true;
    }
    /**
     * @Auteur: BELGUEDJ NASSILYA
     * Vérifie si l'entrée utilisateur est un seul caractère alphabétique
     * @param input L'entrée utilisateur
     * @return true si c'est un seul caractère alphabétique, false sinon
     */
    public static boolean validerNomColon(String input) {
        return input != null && input.matches("[A-Za-z]");
    }
    
    /**
     * @Auteur: BELGUEDJ NASSILYA
     * Vérifie si l'entrée utilisateur est une liste valide de préférences :
     * - Uniquement des chiffres
     * - Séparés par des espaces
     * - Pas de caractères spéciaux, lettres ou virgules
     *
     * @param input L'entrée utilisateur.
     * @return true si l'entrée est valide, false sinon
     */
    public static boolean validerPreferences(String input) {
        if (input == null || input.isEmpty()) {
            return false; // Entrée vide ou nulle
        }

        String[] tokens = input.split(" "); // Divise la chaîne par espaces
        for (String token : tokens) {
            try {
                Integer.parseInt(token); // Tente de convertir chaque élément en entier
            } catch (NumberFormatException e) {
                return false; // Si une exception survient, ce n'est pas un entier valide
            }
        }
        return true; // Tous les éléments sont valides
    }



}
