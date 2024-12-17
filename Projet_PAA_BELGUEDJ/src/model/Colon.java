package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Colon {
    private String nom;
    private int [] prf = new int[0];
    private List<Ressource> preferences;
    private List<Colon> relations;
    private Ressource ressourceAttribuee;
    public int getPreferenceIndex(Ressource ressource) {
        if (ressource == null || preferences == null) return -1; // Si la ressource n'existe pas
        return preferences.indexOf(ressource); // Retourne l'indice ou -1 si non trouvé
    }
    /**
     * @author BELGUEDJ NASSILYA
     * Constructeur
     */
    public Colon(String nom) {
        this.nom = nom;
        this.preferences = new ArrayList<>();
        this.relations = new ArrayList<>();
    }
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner le nom du colon.
     * @return nom du colon.
     */
    public String getNom() {
        return nom;
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner la liste des préférences.
     * @return la liste des préférences.
     */
    public List<Ressource> getPreferences() {
        try {
            if (preferences == null) {
                throw new IllegalStateException("Les préférences ne sont pas initialisées pour ce colon.");
            }
            return preferences;
        } catch (IllegalStateException e) {
            System.err.println("Erreur : " + e.getMessage());
            return new ArrayList<>(); // Retourne une liste vide pour éviter une erreur
        }
    }

    
    public List<Integer> getPreferencesIds() {
        try {
            if (preferences == null) {
                throw new IllegalStateException("Les préférences ne sont pas initialisées pour ce colon.");
            }
            // Transforme la liste de Ressource en liste d'entiers (IDs)
            return preferences.stream()
                              .map(Ressource::getId)
                              .collect(Collectors.toList());
        } catch (IllegalStateException e) {
            System.err.println("Erreur : " + e.getMessage());
            return new ArrayList<>(); // Retourne une liste vide pour éviter une erreur
        }
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour ajouter les préférences.
     * @param preferences       Une liste de preferences.
     */
    public void ajouterPreferences(List<Ressource> preferences) {
        try {
            if (preferences == null || preferences.isEmpty()) {
                throw new IllegalArgumentException("Les préférences ne peuvent pas être nulles ou vides");
            }
            this.preferences = preferences;
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inattendue dans ajouterPreferences : " + e.getMessage());
        }
    }

   
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour ajouter une relation entre ce colon et un autre colon spécifique.
     * Exemple d'utilisation : A.ajouterRelation(B)
     * @param autreColon       Un colon.
     */
    public void ajouterRelation(Colon autreColon) {
        try {
            if (autreColon == null) {
                throw new IllegalArgumentException("Le colon ne peut pas être null.");
            }
            if (!relations.contains(autreColon)) {
                relations.add(autreColon);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inattendue dans ajouterRelation : " + e.getMessage());
        }
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner les relations entre les colons.
     * @return une liste       Une liste de colons.
     */
    public List<Colon> getRelations() {
        return relations;
    }
   
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner la ressource actuellement attribuée à ce colon.
     * @return La liste de ressources attribuée au colon.
     */
    public Ressource getRessourceAttribuee() {
        return ressourceAttribuee;
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour attribuer une ressource spécifique à ce colon et affiche un message indiquant la ressource attribuée.
     * @param ressourceAttribuee         La ressource attribuée au colon.
     */
    public void setRessourceAttribuee(Ressource ressourceAttribuee) {
        try {
            this.ressourceAttribuee = ressourceAttribuee;
            System.out.println("Ressource " + (ressourceAttribuee != null ? ressourceAttribuee.getId() : "Aucune") 
                               + " attribuée à " + nom);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'attribution de la ressource : " + e.getMessage());
        }
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour définir la liste des préférences pour ce colon et affiche un message de confirmation.
     * @param preferences         une liste de preferences.
     */
    public void setPreferences(List<Ressource> preferences) {
        this.preferences = preferences;
      //  System.out.println("Les préférences pour " + nom + " ont été définies");
    }
   
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner les préférences sous forme de tableau d'entiers.
     * @return tableau d'entier contenant les preferences.
     */
    public int[] getpreferences() {
    	return prf;
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode toString pour afficher le nom du colon.
     * Cela permet d'éviter de retourner un identifiant d'objet de type "[model.Colon@266474c2]".
     * lorsque la méthode afficherRelations est appelée
     * @return Nom du colon.
     */
    @Override
    public String toString() {
        return nom;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Colon colon = (Colon) obj;
        return Objects.equals(nom, colon.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
    public Ressource getProchaineRessourcePreferee() {
        // Parcourir les ressources préférées dans l'ordre
        for (Ressource ressource : this.getPreferences()) {
            // Si la ressource n'est pas encore attribuée à ce colon, retourne-la
            if (!this.getRessourceAttribuee().equals(ressource)) {
                return ressource;
            }
        }
        // Si toutes les ressources préférées ont déjà été vérifiées ou attribuées
        return null;
    }
    public boolean peutAccepterNimporteQuelleRessource() {
        // Vérifier si le colon a une liste de ressources qu'il "ne pas" aime
        if (getRessourcesQuIlNeAimePas() == null || getRessourcesQuIlNeAimePas().isEmpty()) {
            // Si la liste est vide ou nulle, le colon peut accepter n'importe quelle ressource
            return true;
        }
        return false;
    }
    private List<Ressource> ressourcesQuIlNeAimePas;

    public List<Ressource> getRessourcesQuIlNeAimePas() {
        return ressourcesQuIlNeAimePas;
    }

    public void setRessourcesQuIlNeAimePas(List<Ressource> ressources) {
        this.ressourcesQuIlNeAimePas = ressources;
    }


    
    
}
