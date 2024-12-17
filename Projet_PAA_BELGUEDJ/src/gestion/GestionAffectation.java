package gestion;
import model.Colon;
import utilitaires.GestionnaireExceptions;
import model.Ressource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
//import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;




public class GestionAffectation {
    private List<Colon> colons = new ArrayList<>();
    private List<Ressource> ressources = new ArrayList<>();
    private CalculateurDeCout calculateur;

    private List<Ressource> toutesLesRessources = new ArrayList<>();

    
    // Constructeur par défaut
    public GestionAffectation() {
        this.calculateur = new CalculateurDeCout(); // Initialisation automatique
        this.colons = new ArrayList<>();
    }

    // Constructeur avec paramètre
    public GestionAffectation(CalculateurDeCout calculateur) {
        this.calculateur = calculateur;
        this.colons = new ArrayList<>();
    }

    
    
   
    
    /**
     * 
     * Crée des colons et des ressources.
     * @author BELGUEDJ NASSILYA
     * Les 26 premiers colons reçoivent des noms basés sur les lettres de l'alphabet (A à Z)
     * Les colons supplémentaires (au-delà de 26) sont nommés avec un préfixe "C" suivi de leur index (par exemple, C27, C28, etc.)
     * Les ressources sont créées avec des IDs uniques correspondant à leur position.
     * 
     * @param nombreColons Le nombre de colons à créer. Doit être supérieur à 0.
     * @throws IllegalArgumentException si le nombre de colons est inférieur ou égal à 0.
     */

    public void creerColonsEtRessources(int nombreColons) {
        try {
            if (nombreColons <= 0) {
                throw new IllegalArgumentException("Le nombre de colons doit être supérieur à 0.");
            }
            for (int i = 0; i < nombreColons; i++) {
                String nom;

                if (i < 26) {
                    // Pour les 26 premiers colons, utiliser les lettres de A à Z
                    nom = String.valueOf((char) ('A' + i));
                } else {
                    // Au-delà de Z, utiliser le format C27, C28, etc.
                    nom = "C" + (i + 1);
                }

                colons.add(new Colon(nom)); // Ajouter le colon avec le nom généré
                ressources.add(new Ressource(i + 1)); // Ajouter une ressource correspondante
            }
        } catch (IllegalArgumentException e) {
            GestionnaireExceptions.gererException(e, "creerColonsEtRessources", "WARNING");
        } catch (Exception e) {
            GestionnaireExceptions.gererErreurCritique(e, "creerColonsEtRessources");
        }
    }


    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour ajouter ressource.
     * @param ressource      Liste de ressources     
     */
    public void ajouterRessource(Ressource ressource) {
        if (!ressources.contains(ressource)) {
            ressources.add(ressource);
            System.out.println("Ressource " + ressource.getId() + " ajoutée avec succès !");
        } else {
            System.out.println("La ressource " + ressource.getId() + " existe déjà.");
        }
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Propose une solution naïve d'affectation des ressources aux colons.
     * Attribue la première ressource préférée disponible pour chaque colon 
     */
    public void proposerSolutionNaive() {
        try {
            for (Colon colon : colons) {
                if (colon.getPreferences() == null || colon.getPreferences().isEmpty()) {
                    throw new IllegalStateException("Le colon " + colon.getNom() + " n'a pas de préférences définies.");
                }
                for (Ressource ressource : colon.getPreferences()) {
                    if (!ressourceEstDejaAttribuee(ressource)) {
                        colon.setRessourceAttribuee(ressource);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            GestionnaireExceptions.gererException(e, "proposerSolutionNaive", "CRITICAL");
        }
    }
    
    
    public Ressource getRessourceByName(String nomRessource) {
        for (Ressource ressource : ressources) { // Parcours de la liste des ressources
            if (ressource.getNom().equals(nomRessource)) { // Comparaison des noms
                return ressource;
            }
        }
        return null; // Retourne null si aucune ressource correspondante n'est trouvée
    }

   
    public Colon getColon(String nom) {
        for (Colon colon : colons) {
            if (colon.getNom().equals(nom)) { // Comparaison des noms
                return colon;
            }
        }
        return null; // Retourne null si aucun colon avec ce nom n'est trouvé
    }


    public Ressource getRessource(int id) {
        for (Ressource ressource : ressources) {
            if (ressource.getId() == id) { // Supposons que Ressource a une méthode getId()
                return ressource;
            }
        }
        return null; // Retourne null si aucune ressource avec cet ID n'est trouvée
    }

    public void affficherAffectations() {
        System.out.println("Affectation actuelle des ressources :");
        for (Colon colon : colons) {
            Ressource ressource = colon.getRessourceAttribuee();
            System.out.println(colon.getNom() + " : " + (ressource != null ? ressource.getId() : "Aucune"));
        }
    }

    
     /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour vérifier si une ressource est déjà attribuée à un colon.
     * @param ressource      Liste de ressources     
     */
   private boolean ressourceEstDejaAttribuee(Ressource ressource) { //un objet
       for (Colon colon : colons) {
           if (ressource.equals(colon.getRessourceAttribuee())) {
               return true;
           }
       }
       return false;
   }

   /**
    * @author BELGUEDJ NASSILYA
    * Méthode pour echanger les ressources attribuées entre deux colons spécifiés par leur nom.
    * @param nom1      Nom du premier colon.
    * @param nom2      Nom du deuxieme colon.
    */
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
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour trouver un colon par son nom.
     * @param nom      Nom du colon recherché. 
     * @return Colon si colon touver, null si non.    
     */
    public Colon trouverColon(String nom) {
        for (Colon colon : colons) {
            if (colon.getNom().equals(nom)) {
                return colon;
            }
        }
        return null;
    }
  
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour ajouter les préférences d'un colon.
     * @param colon           Nom du colon. 
     * @param prefereces      liste de preferences.     
     */
    public void ajouterPreferencesColon(Colon colon, int[] preferences) {
        try {
            if (colon == null || preferences == null) {
                throw new IllegalArgumentException("Le colon ou les préférences ne peuvent pas être null");
            }

            List<Ressource> listePreferences = new ArrayList<>();
            List<Integer> uniquePreferences = new ArrayList<>();

            for (int p : preferences) {
                if (uniquePreferences.contains(p)) {
                    throw new IllegalArgumentException("La ressource " + p + " est en double dans les préférences de " + colon.getNom());
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

            if (listePreferences.size() != ressources.size()) {
                throw new IllegalStateException("Les préférences pour " + colon.getNom() 
                    + " sont incomplètes. Veuillez inclure toutes les ressources disponibles.");
            }

            colon.setPreferences(listePreferences);
            System.out.println("Les préférences pour " + colon.getNom() + " ont été définies.");
        } catch (Exception e) {
            GestionnaireExceptions.gererException(e, "ajouterPreferencesColon", "WARNING");
        }
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour obtenir une ressource par son ID.
     * @param id           L'id de la ressource rechercher.
     * @return ressource si trouvé, null si non.     
     */
    private Ressource obtenirRessourceParId(int id) {
        for (Ressource ressource : ressources) {
            if (ressource.getId() == id) {
                return ressource;
            }
        }
        return null; // Retourne null si aucune ressource ne correspond à l'ID
    }

   
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour vérifier si un colon a bien une liste de préférence.
     */
    public void verifierPreferences() {
        for (Colon colon : colons) {
            if (colon.getpreferences() == null || colon.getpreferences().length == 0) {
                System.out.println("Le colon " + colon.getNom() + " n'a pas de préférences");
            }
        }
    }
    
 
    
    
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour Vérifier que tous les colons ont une liste complète de préférences.
     * liste préférences même taille liste des ressources disponibles
     * @return True si tout les colons ont des préférences compeletes et même taille des ressources disponibles, false si non.
     */
    public boolean verifierPreferencesComplete() {
        // Liste pour stocker les noms des colons sans préférences
        List<String> colonsSansPreferences = new ArrayList<>();
        
        // Parcourir les colons
        for (Colon colon : colons) {
            if (colon.getPreferences() == null || colon.getPreferences().isEmpty()) {
                colonsSansPreferences.add(colon.getNom());
            }
        }
        
        // Vérifier si la liste des colons sans préférences n'est pas vide
        if (!colonsSansPreferences.isEmpty()) {
            System.out.println("Les colons suivants n'ont pas une liste complète de préférences : " 
                               + String.join(", ", colonsSansPreferences));
            return false;
        }
        
        return true;
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner la liste des colons.
     * @return liste de colons
     */
    public List<Colon> getColons() {
        return colons;
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner la liste des ressources.s
     * @return liste de ressources
     */
    public List<Ressource> getRessources() {
        return ressources;
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour afficher l'affectation des ressources.
     */
    public void afficherAffectations() {
        System.out.println("Affectation actuelle des ressources :");
        for (Colon colon : colons) {
            Ressource ressourceAttribuee = colon.getRessourceAttribuee();
            System.out.println(colon.getNom() + " : " 
                + (ressourceAttribuee != null ? ressourceAttribuee.getId() : "Aucune"));
        }
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour ajouter un colon.
     * @param  colon        Un colon.
     */
    public void ajouterColon(Colon colon) {
        if (!colons.contains(colon)) {
            colons.add(colon);
            System.out.println("Colon " + colon.getNom() + " ajouté à la colonie.");
        } else {
            System.out.println("Le colon " + colon.getNom() + " existe déjà.");
        }
    }
// *******NEW*************************************************************//
    
 // *******NEW*************************************************************//
    
 // *******NEW*************************************************************//
    
 // *******NEW*************************************************************//
    
 // *******NEW*************************************************************//
    
 // *******NEW*************************************************************//
    
 // *******NEW*************************************************************//
   public void rechercheLocalee(int maxIt, CalculateurDeCout calculateur) {
        List<Colon> colons = getColons();
      //  List<Ressource> ressourcesDisponibles = new ArrayList<>(ressources);
     
        //Random random = new Random();

        // Initialisation avec une solution naïve
        proposerSolutionNaive();
        int coutActuel = calculateur.calculerNombreColonsJaloux(colons);
        System.out.println("[INFO] Coût initial : " + coutActuel);

        for (int iteration = 0; iteration < maxIt; iteration++) {
            boolean amelioration = false;

            // Parcourir chaque paire de colons pour trouver un échange bénéfique
            for (int i = 0; i < colons.size(); i++) {
                for (int j = i + 1; j < colons.size(); j++) {
                    Colon colon1 = colons.get(i);
                    Colon colon2 = colons.get(j);

                    Ressource ressourceColon1 = colon1.getRessourceAttribuee();
                    Ressource ressourceColon2 = colon2.getRessourceAttribuee();

                    // Échanger les ressources temporairement
                    colon1.setRessourceAttribuee(ressourceColon2);
                    colon2.setRessourceAttribuee(ressourceColon1);

                    if (validerAffectationUnique()) { // Vérifier que l'échange est valide
                        int nouveauCout = calculateur.calculerNombreColonsJaloux(colons);

                        if (nouveauCout < coutActuel) {
                            // Accepter l'échange si le coût diminue
                            coutActuel = nouveauCout;
                            amelioration = true;
                            System.out.println("[INFO] Échange accepté entre " + colon1.getNom() +
                                    " et " + colon2.getNom() + " | Nouveau coût : " + coutActuel);
                        } else {
                            // Annuler l'échange si aucune amélioration
                            colon1.setRessourceAttribuee(ressourceColon1);
                            colon2.setRessourceAttribuee(ressourceColon2);
                        }
                    } else {
                        // Annuler l'échange si invalide
                        colon1.setRessourceAttribuee(ressourceColon1);
                        colon2.setRessourceAttribuee(ressourceColon2);
                    }
                }
            }

            // Affichage intermédiaire toutes les 50 itérations
            if (iteration % 50 == 0 || amelioration) {
                System.out.println("[INFO] Itération " + iteration + ", coût actuel : " + coutActuel);
                afficherAffectations();
            }

            // Si aucune amélioration n'est trouvée, arrêter
            if (!amelioration) {
                System.out.println("[INFO] Aucune amélioration trouvée à l'itération " + iteration);
                break;
            }
        }

        // Résultat final
        System.out.println("[INFO] Coût final après optimisation locale : " + coutActuel);
        System.out.println("[INFO] Affectation finale des ressources :");
        afficherAffectations();
    }

    
 
    
   private boolean validerAffectationUnique() {
	    // Vérifier que chaque ressource est attribuée à un seul colon
	    Set<Ressource> ressourcesAttribuees = new HashSet<>();

	    for (Colon colon : colons) {
	        Ressource ressource = colon.getRessourceAttribuee();
	        if (ressource != null) {
	            // Si la ressource est déjà dans le set, l'affectation n'est pas valide
	            if (!ressourcesAttribuees.add(ressource)) {
	                System.out.println("[ERREUR] La ressource " + ressource + " est attribuée à plusieurs colons.");
	                return false;
	            }
	        }
	    }

	    // Si aucune duplication n'est trouvée, l'affectation est valide
	    return true;
	}


	public void afficherRessources() {
        if (ressources.isEmpty()) {
            System.out.println("[ERREUR] Aucune ressource n'est enregistrée !");
        } else {
            System.out.println("Ressources enregistrées :");
            for (Ressource ressource : ressources) {
                System.out.println(" - Ressource ID : " + ressource.getId());
            }
        }
    }
    private List<Colon> getColonsJaloux() {
        List<Colon> colonsJaloux = new ArrayList<>();
        for (Colon colon : colons) {
            if (calculateur.estJaloux(colon, colons)) {
                colonsJaloux.add(colon);
            }
        }
        // Trier par nombre de relations, décroissant
        colonsJaloux.sort((c1, c2) -> Integer.compare(c2.getRelations().size(), c1.getRelations().size()));
        return colonsJaloux;
    }



    public List<Ressource> rechercheLocale(int maxTentatives) {
        proposerSolutionNaive(); // Initialise avec une solution naïve
        int coutInitial = calculateur.calculerNombreColonsJaloux(colons);
        int coutActuel = coutInitial; // Nombre actuel de colons jaloux
        int tentative = 0; // Compteur de tentatives

        while (tentative < maxTentatives) {
            tentative++;
            boolean ameliorationTrouvee = false; // Réinitialisation pour chaque itération

            // Obtenir les colons jaloux
            List<Colon> colonsJaloux = getColonsJaloux();
            if (colonsJaloux.isEmpty()) {
                // Si aucun colon n'est jaloux, solution parfaite trouvée
                System.out.println("[SUCCÈS] Aucun colon jaloux !");
                return getRessourcesAttribuees();
            }

            // Parcourir chaque colon jaloux
            for (Colon colonP : colonsJaloux) {
                Ressource ressourcePreferee = colonP.getProchaineRessourcePreferee();
                Colon colonQ = null;

                if (ressourcePreferee != null) {
                    // Trouver un autre colon ayant cette ressource
                    colonQ = getColonParRessource(ressourcePreferee);
                }

                if (colonQ == null) {
                    // Si aucune ressource préférée n'est possible, choisir une ressource aléatoire
                    List<Ressource> ressourcesDisponibles = getRessourcesDisponibles();
                    if (!ressourcesDisponibles.isEmpty()) {
                        ressourcePreferee = ressourcesDisponibles.get(0); // Prendre la première ressource disponible
                        colonQ = getColonParRessource(ressourcePreferee);
                    }
                }

                if (ressourcePreferee == null || colonQ == null) {
                    // Si aucune ressource viable n'est trouvée, ignorer ce colon
                    continue;
                }

                // Vérifier si le colon peut accepter n'importe quelle ressource
                if (colonP.peutAccepterNimporteQuelleRessource()) {
                    // Attribuer une ressource libre directement
                    if (!getRessourcesDisponibles().isEmpty()) {
                        Ressource ressourceLibre = getRessourcesDisponibles().get(0); // Ressource libre
                        attribuerRessource(colonP, ressourceLibre);
                    }
                    continue;
                }

                // Échanger les ressources entre colonP et colonQ
                echange(colonP, colonQ);

                // Calculer le coût après l'échange
                int nouveauCout = calculateur.calculerNombreColonsJaloux(colons);

                if (nouveauCout < coutActuel) {
                    // Si le coût diminue, accepter l'échange
                    coutActuel = nouveauCout;
                    ameliorationTrouvee = true; // Une amélioration stricte a été trouvée
                    System.out.println("Amélioration trouvée : Nombre de colons jaloux = " + coutActuel);

                    // Sortir si le coût est strictement inférieur à l'état initial
                    if (coutActuel < coutInitial) {
                        System.out.println("[SUCCÈS] Nombre de colons jaloux réduit à : " + coutActuel);
                        return getRessourcesAttribuees();
                    }
                } else {
                    // Annuler l'échange si aucune amélioration
                    echange(colonP, colonQ);
                }
            }

            // Si aucune amélioration n'a été trouvée pour cette tentative
            if (!ameliorationTrouvee) {
                System.out.println("Aucune amélioration trouvée à la tentative #" + tentative);
            }
        }

        // Vérification finale
        if (coutActuel < coutInitial) {
            System.out.println("[SUCCÈS] Nombre de colons jaloux réduit à : " + coutActuel);
            return getRessourcesAttribuees();
        } else {
            System.out.println("[ÉCHEC] Nombre maximum de tentatives atteint sans amélioration stricte.");
            return null; // Vous pouvez aussi retourner une liste vide ou lever une exception
        }
    }


    private void attribuerRessource(Colon colonP, Ressource ressourceLibre) {
        if (colonP == null || ressourceLibre == null) {
            // Vérifier si les paramètres sont valides
            throw new IllegalArgumentException("Le colon ou la ressource ne peut pas être null.");
        }

        // Vérifier si la ressource est déjà attribuée
        if (ressourceLibre.estAttribuee()) {
            throw new IllegalStateException("La ressource " + ressourceLibre + " est déjà attribuée.");
        }

        // Retirer l'ancienne ressource du colon, si nécessaire
        Ressource ancienneRessource = colonP.getRessourceAttribuee();
        if (ancienneRessource != null) {
            ancienneRessource.setAttribuee(false); // Marquer l'ancienne ressource comme non attribuée
        }

        // Attribuer la nouvelle ressource au colon
        colonP.setRessourceAttribuee(ressourceLibre);
        ressourceLibre.setAttribuee(true); // Marquer la ressource comme attribuée

        System.out.println("La ressource " + ressourceLibre + " a été attribuée au colon " + colonP);
    }


	private List<Ressource> getRessourcesDisponibles() {
        List<Ressource> disponibles = new ArrayList<>();
        
        // Parcourir toutes les ressources
        for (Ressource ressource : toutesLesRessources) {
            // Vérifier si la ressource n'est pas attribuée
            if (!ressource.estAttribuee()) {
                disponibles.add(ressource);
            }
        }
        
        return disponibles;
    }

	private Colon getColonParRessource(Ressource ressourcePreferee) {
        for (Colon colon : colons) {
            if (colon.getRessourceAttribuee().equals(ressourcePreferee)) {
                return colon;
            }
        }
        return null; // Aucun colon trouvé avec cette ressource
    }

	private List<Ressource> getRessourcesAttribuees() {
        List<Ressource> ressourcesAttribuees = new ArrayList<>();
        for (Colon colon : colons) {
            ressourcesAttribuees.add(colon.getRessourceAttribuee());
        }
        return ressourcesAttribuees;
    }

    
	private void echange(Colon colonP, Colon colonQ) {
	    Ressource ressourceP = colonP.getRessourceAttribuee();
	    Ressource ressourceQ = colonQ.getRessourceAttribuee();

	    colonP.setRessourceAttribuee(ressourceQ);
	    colonQ.setRessourceAttribuee(ressourceP);

	    System.out.println("Échange effectué entre Colon " + colonP.getNom() + " et Colon " + colonQ.getNom());
	}

   
    
    
    
    /**
     * Calcule l'affectation optimale pour minimiser le nombre de colons jaloux.
     * Utilise un graphe biparti pondéré pour modéliser le problème.
     * @return Une carte représentant l'affectation optimale entre colons et ressources.
     */
    
    /*
    public Map<Colon, Ressource> calculerAffectationOptimale() {
        int n = colons.size();

        // Construire la matrice des poids
        int[][] weightMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                weightMatrix[i][j] = calculerPoids(colons.get(i), ressources.get(j));
            }
        }

        // Résoudre le problème avec l'algorithme Hongrois
        int[] resultat = algorithmeHongrois(weightMatrix);

        // Vérifier que le tableau résultat a la bonne taille
        if (resultat.length != n) {
            throw new IllegalStateException("La taille du tableau résultat ne correspond pas au nombre de colons !");
        }

        // Vérifier la validité des indices retournés
        for (int i = 0; i < resultat.length; i++) {
            if (resultat[i] < 0 || resultat[i] >= ressources.size()) {
                throw new ArrayIndexOutOfBoundsException("Indice invalide retourné par l'algorithme Hongrois : " + resultat[i]);
            }
        }

        // Construire l'affectation optimale
        Map<Colon, Ressource> affectations = new HashMap<>();
        for (int i = 0; i < n; i++) {
            affectations.put(colons.get(i), ressources.get(resultat[i]));
        }

        // Afficher les résultats
        afficherAffectation(affectations);
        return affectations;
    }
*/


   
    
    
    
    public void minimiserJalousiesOptimise() {
        System.out.println("[INFO] Initialisation avec l'affectation naïve...");
        proposerSolutionNaive();

        boolean amelioration = true;
        int iterations = 0;
        int maxIterations = 100;

        while (amelioration && iterations < maxIterations) {
            amelioration = false;
            int jalousiesInitiales = calculateur.calculerNombreColonsJaloux(colons);

            // Identifier les colons jaloux
            for (Colon colonJaloux : colons) {
                Ressource ressourceAttribuee = colonJaloux.getRessourceAttribuee();
                if (ressourceAttribuee == null) continue;

              //  List<Colon> voisins = colonJaloux.getRelations(); // Obtenez les voisins
                Ressource meilleureRessource = null;
                int meilleurGain = 0;

                // Chercher une meilleure affectation pour réduire la jalousie
                for (Ressource ressourceCandidate : ressources) {
                    if (ressourceCandidate.equals(ressourceAttribuee)) continue;
                    if (ressourceEstDejaAttribuee(ressourceCandidate)) continue;

                    colonJaloux.setRessourceAttribuee(ressourceCandidate);
                    int jalousiesApresAffectation = calculateur.calculerNombreColonsJaloux(colons);

                    int gain = jalousiesInitiales - jalousiesApresAffectation;
                    if (gain > meilleurGain) {
                        meilleurGain = gain;
                        meilleureRessource = ressourceCandidate;
                    }

                    // Revenir à l'affectation précédente pour évaluer d'autres options
                    colonJaloux.setRessourceAttribuee(ressourceAttribuee);
                }

                // Appliquer la meilleure réaffectation trouvée
                if (meilleureRessource != null && meilleurGain > 0) {
                    colonJaloux.setRessourceAttribuee(meilleureRessource);
                    amelioration = true;
                    System.out.println("[INFO] Réaffectation : " + colonJaloux.getNom() + " reçoit " + meilleureRessource.getId());
                }
            }

            iterations++;
            int jalousiesActuelles = calculateur.calculerNombreColonsJaloux(colons);
            System.out.println("[INFO] Nombre de colons jaloux après l'itération " + iterations + " : " + jalousiesActuelles);

            if (jalousiesActuelles == 0) {
                System.out.println("[INFO] Toutes les jalousies ont été éliminées !");
                break;
            }
        }

        int jalousiesFinales = calculateur.calculerNombreColonsJaloux(colons);
        System.out.println("[INFO] Optimisation terminée. Nombre final de colons jaloux : " + jalousiesFinales);
    }

    
   
    /**
     * Vérifie si un colon est jaloux.
     */
    
   
    
    private Map<Integer, String> idVersNomRessource = new HashMap<>();

 // Méthode pour ajouter une ressource avec mapping
 public void ajouterRessource(Ressource ressource, String nomOriginal) {
     ressources.add(ressource);
     idVersNomRessource.put(ressource.getId(), nomOriginal);
 }

 // Méthode pour obtenir le nom original d'une ressource
 public String getNomOriginalRessource(int id) {
     return idVersNomRessource.getOrDefault(id, "Ressource inconnue");
 }

 public CalculateurDeCout getCalculateur() {
	    return this.calculateur;
	}
 
 
 
 
 public class Etat {
	    Map<Colon, Ressource> affectation; // L'affectation actuelle des colons aux ressources
	    List<Ressource> ressourcesRestantes; // Ressources qui restent à attribuer
	    int scoreEstime; // Score estimé pour cet état

	    public Etat(Map<Colon, Ressource> affectation, List<Ressource> ressourcesRestantes, int scoreEstime) {
	        this.affectation = new HashMap<>(affectation);
	        this.ressourcesRestantes = new ArrayList<>(ressourcesRestantes);
	        this.scoreEstime = scoreEstime;
	    }

	    public int getScoreEstime() {
	        return scoreEstime;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null || getClass() != obj.getClass()) return false;
	        Etat other = (Etat) obj;
	        return affectation.equals(other.affectation) && ressourcesRestantes.equals(other.ressourcesRestantes);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(affectation, ressourcesRestantes);
	    }
	}

 
 
 public void branchAndBound(CalculateurDeCout calculateur) {
     // File de priorité pour explorer les états prometteurs en priorité
     PriorityQueue<Etat> queue = new PriorityQueue<>(Comparator.comparingInt(Etat::getScoreEstime));

     // Initialisation avec une solution de départ améliorée
     Map<Colon, Ressource> solutionInitiale = initialiserSolutionAmelioree();
     int meilleurScore = calculateur.calculerNombreColonsJaloux(new ArrayList<>(solutionInitiale.keySet()));

     queue.add(new Etat(solutionInitiale, new ArrayList<>(ressources), meilleurScore));

     // Mémoire pour éviter de traiter plusieurs fois les mêmes états
     Map<Map<Colon, Ressource>, Integer> memo = new HashMap<>();
     Map<Colon, Ressource> meilleureAffectation = null;

     while (!queue.isEmpty()) {
         Etat etatCourant = queue.poll();

         // Vérification de fin : toutes les ressources ont été attribuées
         if (etatCourant.affectation.size() == colons.size()) {
             int score = calculateur.calculerNombreColonsJaloux(new ArrayList<>(etatCourant.affectation.keySet()));
             if (score < meilleurScore) {
                 meilleurScore = score;
                 meilleureAffectation = etatCourant.affectation;
             }
             continue;
         }

         // Pruning : éviter les états déjà explorés
         if (memo.containsKey(etatCourant.affectation)) continue;
         memo.put(etatCourant.affectation, etatCourant.scoreEstime);

         // Choisir le prochain colon à traiter
         Colon colon = colons.get(etatCourant.affectation.size());

         // Trier les ressources restantes en fonction des préférences du colon
         List<Ressource> ressourcesTriees = etatCourant.ressourcesRestantes.stream()
                 .sorted(Comparator.comparingInt(r -> colon.getPreferences().indexOf(r)))
                 .collect(Collectors.toList());

         // Exploration des ressources disponibles
         for (Ressource ressource : ressourcesTriees) {
             Map<Colon, Ressource> nouvelleAffectation = new HashMap<>(etatCourant.affectation);
             nouvelleAffectation.put(colon, ressource);

             List<Ressource> nouvellesRessources = new ArrayList<>(etatCourant.ressourcesRestantes);
             nouvellesRessources.remove(ressource);

             int coutActuel = calculateur.calculerNombreColonsJaloux(new ArrayList<>(nouvelleAffectation.keySet()));
             int scoreEstime = coutActuel + estimerCoutRestant(nouvelleAffectation, nouvellesRessources);

             // Élagage : ajouter uniquement les états prometteurs
             if (scoreEstime < meilleurScore) {
                 queue.add(new Etat(nouvelleAffectation, nouvellesRessources, scoreEstime));
             }
         }
     }

     // Affichage du résultat final
     if (meilleureAffectation == null) {
         System.out.println("[ERREUR] Aucune solution trouvée.");
     } else {
         System.out.println("Meilleur score trouvé : " + meilleurScore);
         afficherAffectation(meilleureAffectation);
     }
 }
 private Map<Colon, Ressource> initialiserSolutionAmelioree() {
     Map<Colon, Ressource> solution = new HashMap<>();
     List<Ressource> ressourcesRestantes = new ArrayList<>(ressources);

     // Trier les colons par le nombre de préférences
     List<Colon> colonsTries = colons.stream()
             .sorted(Comparator.comparingInt(c -> c.getPreferences().size()))
             .collect(Collectors.toList());

     for (Colon colon : colonsTries) {
         for (Ressource ressource : colon.getPreferences()) {
             if (ressourcesRestantes.contains(ressource)) {
                 solution.put(colon, ressource);
                 ressourcesRestantes.remove(ressource);
                 break;
             }
         }
     }
     return solution;
 }

 private void afficherAffectation(Map<Colon, Ressource> affectation) {
     System.out.println("Affectation des ressources :");
     for (Map.Entry<Colon, Ressource> entry : affectation.entrySet()) {
         System.out.println(entry.getKey().getNom() + " -> Ressource " + entry.getValue().getId());
     }
 }



 private int estimerCoutRestant(Map<Colon, Ressource> affectation, List<Ressource> ressourcesRestantes) {
     int estimation = 0;
     for (Colon colon : colons) {
         if (!affectation.containsKey(colon)) { // Pour chaque colon non encore affecté
             int meilleurCout = Integer.MAX_VALUE;

             for (Ressource ressource : ressourcesRestantes) {
                 int rang = colon.getPreferences().indexOf(ressource);
                 if (rang != -1) {
                     meilleurCout = Math.min(meilleurCout, rang + 1);
                 } else {
                     meilleurCout = Math.min(meilleurCout, 100); // Coût élevé si ressource non préférée
                 }
             }
             estimation += meilleurCout;
         }
     }
     return estimation;
 }



}

