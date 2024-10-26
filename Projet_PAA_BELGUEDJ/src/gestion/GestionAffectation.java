package gestion;
import model.Colon;
import model.Ressource;
import java.util.ArrayList;
import java.util.List;

public class GestionAffectation {
    private List<Colon> colons = new ArrayList<>();
    private List<Ressource> ressources = new ArrayList<>();

    // Méthode pour créer des colons et des ressources
    public void creerColonsEtRessources(int nombreColons) {
        for (int i = 0; i < nombreColons; i++) {
            char nom = (char) ('A' + i); // Nommer les colons A, B, C ... 'A' + 0 ->'A' 65 en ASCII
            colons.add(new Colon(String.valueOf(nom))); //nom s'attend a avoir un string du coup String pour convertir char en string
            ressources.add(new Ressource(i + 1, "Ressource " + (i + 1))); // Créer des ressources
        }
    }

    // Proposer une solution naïve d'affectation des ressources
    public void proposerSolutionNaive() {
        for (Colon colon : colons) {
            for (Ressource ressource : colon.getPreferences()) { //parcourir liste de préferece de chaque colon
                if (!ressourceEstDejaAttribuee(ressource)) {
                    colon.setRessourceAttribuee(ressource);
                    System.out.println(colon.getNom() + " reçoit " + ressource.getNom());
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
            System.out.println("Erreur : Un ou les deux colons n'existent pas.");
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

    // Ajouter listes préférences d'un colon        conserve ordre préférences  accés index  taille fixe
    public void ajouterPreferencesColon(Colon colon, int[] preferences) { //ex -> je prefere le 1,3,6 éme
        List<Ressource> listePreferences = new ArrayList<>(); //liste vide 
        for (int p : preferences) { //pour chaque elem du tableau
            listePreferences.add(ressources.get(p - 1));
        }                        //récupérer une ressource spécifique
        colon.ajouterPreferences(listePreferences); 
    }

    // Vérifier que tous les colons ont une liste complète de préférences
    // + liste préférences même taille liste des ressources disponibles
    public boolean verifierPreferencesCompletes() {
        for (Colon colon : colons) {
        	
            if (colon.getPreferences().size() != ressources.size()) {
            	//autre condition colon.getPreferences().isEmpty()
                System.out.println("Le colon " + colon.getNom() + " n'a pas une liste complète de préférences.");
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
}
