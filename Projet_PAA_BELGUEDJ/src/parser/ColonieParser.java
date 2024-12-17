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

    public static GestionAffectation parser(String fichier) {
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


    private static Map<String, Integer> ressourceMapping = new HashMap<>();
    private static int nextRessourceId = 1; // Compteur pour générer des IDs uniques
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




    private static int genererIdentifiantUnique(String nomRessource) {
        if (!mapRessources.containsKey(nomRessource)) {
            mapRessources.put(nomRessource, compteurRessource++);
        }
        return mapRessources.get(nomRessource);
    }

    public static Colon colonParser(String line) {
        String[] colons = line.split("[(),.]");
        if (colons.length != 2 || !isAlphanumeric(colons[1])) {
            throw new IllegalArgumentException("Erreur : Ligne 'colon' mal formée ou nom invalide -> " + line);
        }

        // Retourner un objet Colon avec le nom
        return new Colon(colons[1]);
    }

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

/*
    public static void preferenceParser(GestionAffectation preference, String line) {
        line = line.replace(").", ")"); // Nettoyer le point final
        String[] info = line.replace(").", ")").split("[(),]");
        if (info.length < 3 || !isAlphanumeric(info[1])) {
            throw new IllegalArgumentException("Erreur : Ligne 'preferences' mal formée ou nom invalide -> " + line);
        }

        Colon colon = new Colon(info[1]);
        int[] preferences = new int[info.length - 2];
        for (int i = 2; i < info.length; i++) {
            preferences[i - 2] = genererIdentifiantUnique(info[i]); // Utilise le mapping pour les ressources
        }
        preference.ajouterPreferencesColon(colon, preferences);
    }

*/
    private static boolean isAlphanumeric(String str) {
        return str != null && str.matches("[a-zA-Z0-9]+");
    }
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
