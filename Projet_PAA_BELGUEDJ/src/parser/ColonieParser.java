package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import gestion.GestionAffectation;
import gestion.GestionRelations;
import model.Colon;
import model.Ressource;
import java.util.*;


public class ColonieParser {
    private static Map<String, Integer> mapRessources = new HashMap<>();
    private static int compteurRessource = 1;
    private static Map<String, Integer> ressourceMapping = new HashMap<>();
    private static int nextRessourceId = 1; // Compteur pour générer des IDs uniques (dans ressourceParser)

    public static GestionAffectation parser(String fichier) {
    	/**
    	 * @author NAGULESWARAN ALICIA
    	 * Parse un fichier de configuration pour créer une instance de GestionAffectation contenant
    	 * les colons, les ressources, les relations "détestation" et les préférences
    	 * 
    	 * @param fichier Le chemin du fichier à parser
    	 * @return Une instance de GestionAffectation contenant les données extraites du fichier
    	 * 
    	 * @throws IllegalArgumentException Si l'ordre des blocs dans le fichier est incorrect ou si une ligne est mal formée
    	 * @throws IOException              Si une erreur survient lors de la lecture du fichier
    	 */
        GestionAffectation colonieA = new GestionAffectation();
        GestionRelations colonieRelations = new GestionRelations(); // Instance pour les relations

        try (BufferedReader buff = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            int etatActuel = 0;

            while ((ligne = buff.readLine()) != null) {
                ligne = ligne.trim();

                if (ligne.startsWith("colon(")) {
                    if (etatActuel > 1) {
                        throw new IllegalArgumentException("Erreur : 'colon' doit apparaître avant 'ressource', 'deteste', ou 'preferences'. Ligne : " + ligne);
                    }
                    etatActuel = 1;

                    // Parser la ligne pour créer un objet Colon
                    Colon colon = colonParser(ligne);

                    // Ajouter le colon dans GestionAffectation
                    colonieA.ajouterColon(colon);

                    // Débogage : Afficher le colon ajouté
                    System.out.println("Colon ajouté : " + colon.getNom());
                

                } else if (ligne.startsWith("ressource(")) {
                    if (etatActuel > 2) {
                        throw new IllegalArgumentException("Erreur : 'ressource' doit apparaître avant 'deteste' ou 'preferences'. Ligne : " + ligne);
                    }
                    etatActuel = 2;
                    Ressource ressource = ressourceParser(ligne);
                    colonieA.ajouterRessource(ressource);
                    
                    
                } else if (ligne.startsWith("deteste(")) {
                    if (etatActuel > 3) {
                        throw new IllegalArgumentException("Erreur : 'deteste' doit apparaître avant 'preferences'. Ligne : " + ligne);
                    }
                    etatActuel = 3;
                    
                    relationParser(colonieA, colonieRelations, ligne); // Utilise GestionRelations ici
                    
                    
                } else if (ligne.startsWith("preferences(")) {
                	
                    etatActuel = 4;
                    preferenceParser(colonieA, ligne);
                } else {
                    throw new IllegalArgumentException("Erreur : Ligne non reconnue -> " + ligne);
                }
            }

            System.out.println("Fichier chargé avec succès.");

        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return colonieA; // Retourner l'instance de GestionAffectation
    }


    
    /**
     * @author BELGUEDJ NASSILYA
     * Parse une ligne décrivant une ressource pour extraire son nom et lui attribuer un identifiant unique
     * 
     * @param line La ligne à parser contenant la déclaration de la ressource
     * @return Une instance de la classe Ressource initialisée avec un identifiant et un nom
     * 
     * @throws IllegalArgumentException Si la ligne est mal formée ou si le nom de la ressource est invalide
     */
    public static Ressource ressourceParser(String line) {
        String[] ressources = line.split("[().,]");
        if (ressources.length != 2 || ressources[1].trim().isEmpty()) {
            throw new IllegalArgumentException("Erreur : Ligne 'ressource' mal formée ou nom invalide -> " + line);
        }

        String nomRessource = ressources[1].trim();
        if (!ressourceMapping.containsKey(nomRessource)) {
            ressourceMapping.put(nomRessource, nextRessourceId++);
        }

        int id = ressourceMapping.get(nomRessource); // Obtenir l'ID numérique
        Ressource ressource = new Ressource(id, nomRessource); // Constructeur avec ID et nom
        System.out.println("Ressource ajoutée avec succès ! ID : " + id + " (Nom : " + nomRessource + ")");
        return ressource;
    }



    /**
     * @author BELGUEDJ NASSILYA
     * Génère un identifiant unique pour une ressource donnée en utilisant son nom
     * 
     * @param nomRessource Le nom de la ressource pour laquelle l'identifiant doit être généré
     * @return L'identifiant unique associé à la ressource
     */
    private static int genererIdentifiantUnique(String nomRessource) {
        if (!mapRessources.containsKey(nomRessource)) {
            mapRessources.put(nomRessource, compteurRessource++);
        }
        return mapRessources.get(nomRessource);
    }
    /**
     * @author NAGULESWARAN ALICIA
     * Parse une ligne décrivant un colon pour extraire et valider son nom
     * 
     * @param line La ligne à parser contenant la déclaration du colon
     * @return Une instance de la classe Colon initialisée avec le nom extrait
     *
     * @throws IllegalArgumentException Si la ligne est mal formée ou si le nom du colon est invalide
     */
    public static Colon colonParser(String line) {
        String[] colons = line.split("[(),.]");
        if (colons.length != 2 || !isAlphanumeric(colons[1])) {
            throw new IllegalArgumentException("Erreur : Ligne 'colon' mal formée ou nom invalide -> " + line);
        }

        // Retourner un objet Colon avec le nom
        return new Colon(colons[1]);
    }
    /**
     * @author BELGUEDJ NASSILYA
     * Parse une ligne décrivant une relation de "détestation" entre deux colons et l'ajoute aux structures de gestion
     * 
     * @param gestionAffectation L'objet permettant de récupérer les colons existants
     * @param gestionRelations   L'objet permettant de gérer les relations entre les colons
     * @param line               La ligne contenant la relation à parser
     * 
     * @throws IllegalArgumentException Si la ligne est mal formée, contient des noms invalides ou si les colons n'existent pas
     * 
     * La méthode procède comme suit :
     * 1. Nettoie la ligne en supprimant les espaces
     * 2. Découpe la ligne en utilisant les délimiteurs `()`, `,` et `.` pour isoler les noms des colons
     * 3. Vérifie que la ligne est correctement formée avec deux noms alphanumériques
     * 4. Récupère les objets Colon correspondants à partir de `gestionAffectation`
     * 5. Vérifie que les deux colons existent, sinon lève une exception
     * 6. Ajoute la relation dans :
     *    - `gestionRelations` pour la gestion globale
     *    - Les objets Colon eux-mêmes pour garantir la symétrie
     * 7. Affiche un message confirmant l'ajout de la relation
     */

    public static void relationParser(GestionAffectation gestionAffectation, GestionRelations gestionRelations, String line) {
        line = line.trim().replaceAll("\\s+", ""); // Nettoyer la ligne

        String[] nomColons = line.split("[(),.]");
        if (nomColons.length != 3 || !isAlphanumeric(nomColons[1]) || !isAlphanumeric(nomColons[2])) {
            throw new IllegalArgumentException("Erreur : Ligne 'deteste' mal formée ou noms invalides -> " + line);
        }

        // Récupérer les colons existants à partir de GestionAffectation
        Colon colon1 = gestionAffectation.getColon(nomColons[1]);
        Colon colon2 = gestionAffectation.getColon(nomColons[2]);

        if (colon1 == null || colon2 == null) {
            throw new IllegalArgumentException("Erreur : Les colons spécifiés n'existent pas -> " + line);
        }

        // Ajouter la relation dans GestionRelations
        gestionRelations.ajouterRelation(colon1, colon2);

        // Ajouter la relation dans les colons eux-mêmes
        colon1.ajouterRelation(colon2);
        colon2.ajouterRelation(colon1);

        System.out.println("Relation ajoutée entre " + colon1.getNom() + " et " + colon2.getNom());
    }

    
    /**
     * @author NAGULESWARAN ALICIA
     * Parse une ligne décrivant les préférences d'un colon et assigne les ressources préférées correspondantes
     * 
     * @param gestion L'objet GestionAffectation utilisé pour récupérer les colons et les ressources existants
     * @param line    La ligne contenant les préférences à parser.
     * 
     * @throws IllegalArgumentException Si la ligne est mal formée, si le colon ou les ressources spécifiées n'existent pas,
     *                                  ou si aucune préférence valide n'est trouvée
     */
    public static void preferenceParser(GestionAffectation gestion, String line) {
        // Supprimer les espaces superflus et le point final avant le traitement
        line = line.trim().replaceAll("\\s+", ""); // Supprimer les espaces inutiles
        if (line.endsWith(").")) {
            line = line.substring(0, line.length() - 2) + ")"; // Supprimer ".)" à la fin
        }

        // Découper la ligne pour extraire les parties
        String[] parts = line.split("[(),]");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Erreur : Ligne 'preferences' mal formée -> " + line);
        }

        // Récupération du nom du colon
        String nomColon = parts[1].trim();
        if (nomColon.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Le nom du colon est vide dans la ligne -> " + line);
        }

        Colon colon = gestion.getColon(nomColon); // Recherche du colon par nom
        if (colon == null) {
            throw new IllegalArgumentException("Erreur : Le colon " + nomColon + " n'existe pas.");
        }

        // Récupération des ressources préférées
        List<Ressource> preferences = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            String nomRessource = parts[i].trim(); // Récupère le nom de la ressource
            if (nomRessource.isEmpty()) {
                System.err.println("Avertissement : Ressource vide ignorée dans la ligne -> " + line);
                continue;
            }

            Ressource ressource = gestion.getRessourceByName(nomRessource); // Recherche par nom
            if (ressource == null) {
                throw new IllegalArgumentException("Erreur : La ressource " + nomRessource + " n'existe pas dans la ligne -> " + line);
            }
            preferences.add(ressource);
        }

        // Vérification que les préférences ne sont pas vides
        if (preferences.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Aucune préférence valide trouvée pour le colon " + nomColon + " dans la ligne -> " + line);
        }

        // Assigner les préférences au colon
        colon.setPreferences(preferences);
        System.out.println("[INFO] Les préférences pour " + colon.getNom() + " ont été définies : " + preferences);
    }
    /**
     * @author BELGUEDJ NASSILYA
     * Vérifie si une chaîne de caractères est composée uniquement de caractères alphanumériques
     * 
     * @param str La chaîne à vérifier
     * @return true si la chaîne est non nulle et ne contient que des lettres et des chiffres, sinon false
     * 
     * La méthode procède comme suit :
     * 1. Vérifie que la chaîne n'est pas nulle
     * 2. Utilise une expression régulière pour valider que la chaîne ne contient que des caractères alphabétiques (a-z, A-Z)
     *    et numériques (0-9)
     */
     private static boolean isAlphanumeric(String str) {
        return str != null && str.matches("[a-zA-Z0-9]+");
    }
     
     /**
      * @author NAGULESWARAN ALICIA
      * Parse une ligne décrivant une ressource, génère un identifiant unique pour celle-ci, 
      * et l'ajoute à la gestion des ressources
      * 
      * @param line               La ligne contenant la déclaration de la ressource
      * @param gestionAffectation L'objet GestionAffectation utilisé pour gérer et ajouter les ressources
      * @return Une instance de la classe Ressource initialisée avec un identifiant unique
      * 
      * @throws IllegalArgumentException Si la ligne est mal formée ou si le nom de la ressource est invalide
      */
      public static Ressource ressourceParser(String line, GestionAffectation gestionAffectation) {
        String[] ressources = line.split("[().,]");
        if (ressources.length != 2 || !isAlphanumeric(ressources[1])) {
            throw new IllegalArgumentException("Erreur : Ligne 'ressource' mal formée ou nom invalide -> " + line);
        }

        int id = genererIdentifiantUnique(ressources[1]);
        Ressource ressource = new Ressource(id);

        // Ajout du mapping dans GestionAffectation
        gestionAffectation.ajouterRessource(ressource, ressources[1]);

        return ressource;
    }

}
