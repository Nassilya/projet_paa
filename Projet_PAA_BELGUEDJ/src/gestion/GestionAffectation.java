package gestion;
import model.Colon;
import model.Ressource;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class GestionAffectation {
    private List<Colon> colons = new ArrayList<>();
    private List<Ressource> ressources = new ArrayList<>();

    // Méthode pour créer des colons et des ressources
    //les objets Ressource sont créés et ajoutés à une liste de ressources
    public void creerColonsEtRessources(int nombreColons) {
        for (int i = 0; i < nombreColons; i++) {
            char nom = (char) ('A' + i);
            colons.add(new Colon(String.valueOf(nom)));
            ressources.add(new Ressource(i + 1)); // Crée des ressources avec des IDs uniques (1, 2, 3, ...)
        }
    }

    // Proposer une solution naïve d'affectation des ressources
    //Assigner les objets Ressource aux colons en fonction de leurs préférences
    public void proposerSolutionNaive() {
        for (Colon colon : colons) {
            for (Ressource ressource : colon.getPreferences()) { //parcourir liste de préferece de chaque colon
                if (!ressourceEstDejaAttribuee(ressource)) {
                    colon.setRessourceAttribuee(ressource);
                    System.out.println(colon.getNom() + " reçoit " + ressource.getId());
                    break;//colon cherche pas a recevori d'autre ressource 
                          // juste 1ere dans liste parmi il prefere
                    //puis on passe au colon suivant 
                }
            }
        }
    }

    // Échanger les ressources attribuées entre deux colons
    public void echangerRessources(String nom1, String nom2) {
        Colon c1 = trouverColon(nom1);
        Colon c2 = trouverColon(nom2);

        if (c1 != null && c2 != null) { // les 2 colons existent 
            Ressource temp = c1.getRessourceAttribuee();
            c1.setRessourceAttribuee(c2.getRessourceAttribuee());
            c2.setRessourceAttribuee(temp);
            System.out.println("Échange effectué entre " + nom1 + " et " + nom2);
        } else {
            System.out.println("Erreur : Un ou les deux colons n'existent pas");
        }
    }

    // Trouver un colon par son nom
    public Colon trouverColon(String nom) {
        for (Colon colon : colons) {
            if (colon.getNom().equals(nom)) {
                return colon;
            }
        }
        return null;
    }
    public void ajouterPreferencesColon(Colon colon, int[] preferences) {
        List<Ressource> listePreferences = new ArrayList<>();
        List<Integer> uniquePreferences = new ArrayList<>();
        
        // Vérification des doublons
        for (int p : preferences) {
            if (uniquePreferences.contains(p)) {
                System.out.println("Erreur : La ressource " + p + " est en double dans les préférences de " + colon.getNom());
                return; // Arrêtez la vérification si un doublon est détecté
            }
            uniquePreferences.add(p);
        }
        for (int p : preferences) {
            Ressource ressource = obtenirRessourceParId(p);
            if (ressource != null) {
                listePreferences.add(ressource);
            } else {
                System.out.println("Erreur : La ressource " + p + " n'existe pas");
            }
        }
        
        // Vérification que le nombre de préférences est égal au nombre de ressources disponibles
        if (listePreferences.size() == ressources.size()) {
            colon.setPreferences(listePreferences);
            System.out.println("Les préférences pour " + colon.getNom() + " ont été définies");
        } else {
            System.out.println("Erreur : Les préférences pour " + colon.getNom() 
                               + " sont incomplètes. Veuillez inclure toutes les ressources disponibles");
        }
    }



    // Méthode pour obtenir une ressource par son ID
    private Ressource obtenirRessourceParId(int id) {
        for (Ressource ressource : ressources) {
            if (ressource.getId() == id) {
                return ressource;
            }
        }
        return null; // Retourne null si aucune ressource ne correspond à l'ID
    }


    public void verifierPreferences() {
        for (Colon colon : colons) {
            if (colon.getpreferences() == null || colon.getpreferences().length == 0) {
                System.out.println("Le colon " + colon.getNom() + " n'a pas de préférences");
            }
        }
    }
    // Vérifier que tous les colons ont une liste complète de préférences
    // + liste préférences même taille liste des ressources disponibles
    public boolean verifierPreferencesCompletes() {
        for (Colon colon : colons) {
        	
            if (colon.getPreferences().size() != ressources.size()) {
            	//autre condition colon.getPreferences().isEmpty()
                System.out.println("Le colon " + colon.getNom() + " n'a pas une liste complète de préférences");
                return false;
            }
        }
        return true;
    }

    // Vérifier si une ressource est déjà attribuée à un colon
    private boolean ressourceEstDejaAttribuee(Ressource ressource) { //un objet
        for (Colon colon : colons) {
            if (ressource.equals(colon.getRessourceAttribuee())) {
                return true;
            }
        }
        return false;
    }

    // Retourner la liste des colons
    public List<Colon> getColons() {
        return colons;
    }

    public void afficherAffectation() {
        System.out.println("Affectation actuelle des ressources :");
        for (Colon colon : colons) {
            Ressource ressourceAttribuee = colon.getRessourceAttribuee();
            System.out.println(colon.getNom() + " : " 
                + (ressourceAttribuee != null ? ressourceAttribuee.getId() : "Aucune"));
              //  + " | HashCode: " + colon.hashCode());
        }
    }

    public void ajouterColon(Colon colon) {
        if (!colons.contains(colon)) {
            colons.add(colon);
            System.out.println("Colon " + colon.getNom() + " ajouté à la colonie.");
        } else {
            System.out.println("Le colon " + colon.getNom() + " existe déjà.");
        }
    }
    //génère toutes les permutations des ressources et les assigne aux colons
    public void trouverSolutionsOptimales(CalculateurDeCout calculateur) {
        List<List<Ressource>> solutionsOptimales = new ArrayList<>();
        int minJalousie = Integer.MAX_VALUE;

        // Obtenez toutes les permutations des ressources
        List<List<Ressource>> permutations = permuter(ressources);

        // Testez chaque permutation
        for (List<Ressource> permutation : permutations) {
            // Appliquer l'affectation de la permutation courante
            for (int i = 0; i < colons.size(); i++) {
                colons.get(i).setRessourceAttribuee(permutation.get(i));
            }

            // Calculer le coût "nombre de colons jaloux" de cette affectation
            int jalousie = calculateur.calculerNombreColonsJaloux(colons);

            if (jalousie < minJalousie) {
                // Nouvelle meilleure solution trouvée-> MAJ score minimal et la liste des solutions
                minJalousie = jalousie;
                solutionsOptimales.clear();
                solutionsOptimales.add(new ArrayList<>(permutation));
            } else if (jalousie == minJalousie) {
                // Si le score est le même que le min-> ajouter cette permutation aux solutions optimales
                solutionsOptimales.add(new ArrayList<>(permutation));
            }
        }

        // Afficher le score minimal et toutes les solutions optimales trouvées
        System.out.println("Score minimal de jalousie : " + minJalousie);
        System.out.println("Solutions optimales (toutes les affectations possibles avec le score minimal) :");
        
        for (List<Ressource> solution : solutionsOptimales) {
            afficherAffectation(solution);
        }
    }

    // Méthode pour afficher une affectation spécifique
    private void afficherAffectation(List<Ressource> affectation) {
        StringBuilder affichage = new StringBuilder();
        for (int i = 0; i < colons.size(); i++) {
            affichage.append(colons.get(i).getNom()).append(":")
                     .append(affectation.get(i).getId()).append(" ");
        }
        System.out.println(affichage.toString().trim());
    }

    // Générer toutes les permutations des ressources
    private List<List<Ressource>> permuter(List<Ressource> ressources) {
        List<List<Ressource>> permutations = new ArrayList<>();
        permuter(ressources, 0, permutations);
        return permutations;
    }

    private void permuter(List<Ressource> ressources, int index, List<List<Ressource>> permutations) {
        if (index == ressources.size() - 1) {
            permutations.add(new ArrayList<>(ressources));
        } else {
            for (int i = index; i < ressources.size(); i++) {
                Collections.swap(ressources, i, index);
                permuter(ressources, index + 1, permutations);
                Collections.swap(ressources, i, index); // Revenir à l'état initial
            }
        }
    }
}

