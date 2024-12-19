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
import java.util.stream.Collectors;
import java.util.HashMap;

public class GestionAffectation {
    private List<Colon> colons = new ArrayList<>();
    private List<Ressource> ressources = new ArrayList<>();
    private CalculateurDeCout calculateur;
    private Map<Integer, String> idVersNomRessource = new HashMap<>();
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

    
    
 // Méthode pour ajouter une ressource avec mapping
    public void ajouterRessource(Ressource ressource, String nomOriginal) {
        ressources.add(ressource);
        idVersNomRessource.put(ressource.getId(), nomOriginal);
    }

    // Méthode pour obtenir le nom original d'une ressource
    public String getNomOriginalRessource(int id) {
        return idVersNomRessource.getOrDefault(id, "Ressource inconnue");
        
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Retourne une carte (Map) représentant l'affectation complète des colons aux ressources
     * 
     * @return Une map où chaque clé est un colon et chaque valeur est la ressource attribuée à ce colon
     */
    public Map<Colon, Ressource> getAffectationComplete() {
        Map<Colon, Ressource> affectation = new HashMap<>();
        for (Colon colon : colons) {
            affectation.put(colon, colon.getRessourceAttribuee());
        }
        return affectation;
    }
    
    
    /**
     * @author BELGUEDJ NASSILYA
     * Retourne l'instance actuelle du calculateur de coût.
     * 
     * @return L'objet CalculateurDeCout associé.
     * 
     * Cette méthode permet d'accéder au calculateur utilisé pour évaluer 
     * les coûts dans l'affectation des ressources.
     */
     public CalculateurDeCout getCalculateur() {
   	    return this.calculateur;
   	}

    /**
     * @author BELGUEDJ NASSILYA
     * Retourne la liste des ressources disponibles, c'est-à-dire celles qui ne sont pas encore attribuées.
     * 
     * @return Une liste contenant toutes les ressources disponibles.
     */
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
    /**
     * @author BELGUEDJ NASSILYA
     * Recherche et retourne le colon auquel une ressource spécifique est attribuée
     * 
     * @param ressourcePreferee La ressource dont on souhaite trouver le détenteur
     * @return Le colon possédant la ressource spécifiée, ou null si aucun colon ne la possède
     */
    private Colon getColonParRessource(Ressource ressourcePreferee) {
    for (Colon colon : colons) {
        if (colon.getRessourceAttribuee().equals(ressourcePreferee)) {
            return colon;
        }
    }
    return null; // Aucun colon trouvé avec cette ressource
}
    /**
     * @author BELGUEDJ NASSILYA
     * Retourne la liste des ressources actuellement attribuées aux colons
     * 
     * @return Une liste contenant toutes les ressources attribuées
     */
    private List<Ressource> getRessourcesAttribuees() {
        List<Ressource> ressourcesAttribuees = new ArrayList<>();
        for (Colon colon : colons) {
        ressourcesAttribuees.add(colon.getRessourceAttribuee());
         }
    return ressourcesAttribuees;
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
    
    
    /**
     * @author BELGUEDJ NASSILYA
     * Recherche une ressource par son nom dans la liste des ressources disponibles
     * Parcourt toutes les ressources et retourne celle dont le nom correspond au nom spécifié
     * 
     * @param nomRessource Le nom de la ressource à rechercher
     * @return La ressource correspondant au nom spécifié si elle existe, sinon null
     */
    public Ressource getRessourceByName(String nomRessource) {
        for (Ressource ressource : ressources) { // Parcours de la liste des ressources
            if (ressource.getNom().equals(nomRessource)) { // Comparaison des noms
                return ressource;
            }
        }
        return null; // Retourne null si aucune ressource correspondante n'est trouvée
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Recherche un colon par son nom dans la liste des colons disponibles
     * Parcourt la liste des colons et retourne celui dont le nom correspond au nom spécifié
     * 
     * @param nom Le nom du colon à rechercher
     * @return Le colon correspondant au nom spécifié s'il existe, sinon null
     */
    public Colon getColon(String nom) {
        for (Colon colon : colons) {
            if (colon.getNom().equals(nom)) { 
                return colon;
            }
        }
        return null;
    }

    
    /**
     * @author BELGUEDJ NASSILYA
     * Recherche une ressource par son identifiant unique dans la liste des ressources disponibles
     * Parcourt toutes les ressources et retourne celle dont l'identifiant correspond à l'ID spécifié
     * 
     * @param id L'identifiant unique de la ressource à rechercher
     * @return La ressource correspondant à l'ID spécifié si elle existe, sinon null
     */
    public Ressource getRessource(int id) {
        for (Ressource ressource : ressources) {
            if (ressource.getId() == id) { 
                return ressource;
            }
        }
        return null; 
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Affiche l'affectation actuelle des ressources aux colons
     * Pour chaque colon, affiche son nom suivi de l'ID de la ressource qui lui est attribuée, 
     * ou "Aucune" si aucune ressource n'est attribuée
     */
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

   
   /**
    * @author BELGUEDJ NASSILYA
    * Affiche la liste des ressources enregistrées
    * Si aucune ressource n'est disponible, affiche un message d'erreur
    * 
    * La méthode procède comme suit :
    * 1. Vérifie si la liste des ressources est vide
    * 2. Affiche un message d'erreur si aucune ressource n'est enregistrée
    * 3. Sinon, affiche l'ID de chaque ressource présente dans la liste
    */
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
    /**
     * @author BELGUEDJ NASSILYA
     * Retourne la liste des colons jaloux, triée par nombre de relations en ordre décroissant
     * Un colon est considéré jaloux si le calculateur détermine qu'il est insatisfait 
     * par rapport à l'affectation actuelle des ressources.
     * 
     * @return Une liste des colons jaloux triée selon le nombre de relations de chacun
     */
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

     /**
      * @author BELGUEDJ NASSILYA
      * Réalise une optimisation par recherche locale pour réduire le nombre de colons jaloux
      * L'algorithme explore les échanges de ressources entre colons afin d'améliorer progressivement la solution actuelle
      * 
      * @param maxTentatives Le nombre maximum de tentatives d'amélioration
      * @return La liste des ressources attribuées si une solution améliorée est trouvée, sinon null
      * 
      */
      public List<Ressource> rechercheLocale(int maxTentatives) {
        proposerSolutionNaive(); // Initialise avec une solution naïve
        int coutInitial = calculateur.calculerNombreColonsJaloux(colons);
        int coutActuel = coutInitial; // Nombre actuel de colons jaloux
        int tentative = 0; // Compteur de tentatives
        boolean ameliorationTrouveeGlobal = false;
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

            if (!ameliorationTrouvee && !ameliorationTrouveeGlobal) {
                ameliorationTrouveeGlobal = true; // Évite d'afficher plusieurs fois
                System.out.println("Aucune amélioration trouvée après plusieurs tentatives.");
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

      /**
       * @author BELGUEDJ NASSILYA
       * Attribue une ressource libre à un colon en s'assurant que les contraintes d'attribution sont respectées
       * Remplace l'ancienne ressource du colon, si elle existe, par la nouvelle ressource spécifiée
       * 
       * @param colonP         Le colon à qui attribuer la ressource
       * @param ressourceLibre La ressource à attribuer au colon
       * @throws IllegalArgumentException Si le colon ou la ressource est null
       * @throws IllegalStateException    Si la ressource spécifiée est déjà attribuée à un autre colon
       */
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

   /**
    * @author BELGUEDJ NASSILYA
    * Échange les ressources attribuées entre deux colons
    * 
    * @param colonP Le premier colon impliqué dans l'échange
    * @param colonQ Le deuxième colon impliqué dans l'échange
    */
   private void echange(Colon colonP, Colon colonQ) {
	    Ressource ressourceP = colonP.getRessourceAttribuee();
	    Ressource ressourceQ = colonQ.getRessourceAttribuee();

	    colonP.setRessourceAttribuee(ressourceQ);
	    colonQ.setRessourceAttribuee(ressourceP);

	    System.out.println("Échange effectué entre Colon " + colonP.getNom() + " et Colon " + colonQ.getNom());
	}

  

 
 public class Etat {
	    Map<Colon, Ressource> affectation; // L'affectation actuelle des colons aux ressources
	    List<Ressource> ressourcesRestantes; // Ressources qui restent à attribuer
	    int scoreEstime; // Score estimé pour cet état

	    /**
	     * @author BELGUEDJ NASSILYA
	     * Constructeur pour initialiser un état
	     * 
	     * @param affectation         La carte représentant l'affectation actuelle des colons aux ressources
	     * @param ressourcesRestantes Liste des ressources non encore attribuées
	     * @param scoreEstime         Le score estimé pour cet état
	     */
	    public Etat(Map<Colon, Ressource> affectation, List<Ressource> ressourcesRestantes, int scoreEstime) {
	        this.affectation = new HashMap<>(affectation);
	        this.ressourcesRestantes = new ArrayList<>(ressourcesRestantes);
	        this.scoreEstime = scoreEstime;
	    }

	    public int getScoreEstime() {
	        return scoreEstime;
	    }
	    /**
	     * @author BELGUEDJ NASSILYA
	     * Vérifie l'égalité entre deux objets Etat.
	     * Deux états sont considérés égaux si leur affectation et leurs ressources restantes sont identiques.
	     * 
	     * @param obj L'objet à comparer avec cet état.
	     * @return true si les états sont égaux, sinon false.
	     */
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null || getClass() != obj.getClass()) return false;
	        Etat other = (Etat) obj;
	        return affectation.equals(other.affectation) && ressourcesRestantes.equals(other.ressourcesRestantes);
	    }
	    /**
	     * @author BELGUEDJ NASSILYA
	     * Calcule le hashCode de cet état en se basant sur l'affectation et les ressources restantes.
	     * 
	     * @return La valeur de hachage pour cet état.
	     */
	    @Override
	    public int hashCode() {
	        return Objects.hash(affectation, ressourcesRestantes);
	    }
	}

 
/**
 * @author BELGUEDJ NASSILYA
 * Résout le problème d'affectation des ressources aux colons en utilisant l'algorithme Branch and Bound
 * L'objectif est de minimiser le nombre de colons jaloux en explorant efficacement l'espace des solutions
 *
 * @param calculateur Un objet permettant de calculer le coût actuel (nombre de colons jaloux)
 * 
 */
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
 
 /**
  * @author BELGUEDJ NASSILYA
  * Initialise une solution améliorée en attribuant les ressources aux colons en fonction de leurs préférences
  * Les colons sont traités dans l'ordre croissant du nombre de leurs préférences pour maximiser l'efficacité
  * 
  * @return Une carte (Map) représentant une première affectation des ressources aux colons
  */
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
 /**
  * @author BELGUEDJ NASSILYA
  * Affiche l'affectation actuelle des ressources aux colons
  * 
  * @param affectation La carte (Map) contenant l'affectation des colons aux ressources
  */
 private void afficherAffectation(Map<Colon, Ressource> affectation) {
     System.out.println("Affectation des ressources :");
     for (Map.Entry<Colon, Ressource> entry : affectation.entrySet()) {
         System.out.println(entry.getKey().getNom() + " -> Ressource " + entry.getValue().getId());
     }
 }

 /**
  * @author BELGUEDJ NASSILYA
  * Estime le coût restant pour l'affectation en cours en se basant sur les préférences des colons
  * et les ressources encore disponibles.
  * 
  * @param affectation         La carte actuelle des affectations des colons aux ressources
  * @param ressourcesRestantes La liste des ressources qui n'ont pas encore été attribuées
  * @return Une estimation du coût total restant pour affecter les ressources aux colons non encore satisfaits
  */
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

