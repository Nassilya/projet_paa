package gestion;
import model.Colon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionRelations {
	private List<Colon> colons = new ArrayList<>();
	private Map<Colon, List<Colon>> relations;

	// Constructeur
	public GestionRelations() {
	     relations = new HashMap<>();
	    }
	/**
	 * @author BELGUEDJ NASSILYA
	 * Retourne la liste des relations d'un colon donné.
	 * 
	 * @param colon Le colon dont on souhaite récupérer les relations.
	 * @return Une liste des colons en relation avec le colon spécifié.
	 */
	public List<Colon> getRelationsDeColon(Colon colon) {
        return relations.getOrDefault(colon, new ArrayList<>());
    }

	
    /**
     * @Auteur: BELGUEDJ NASSILYA
     * Ajoute un colon à la liste des colons (s'il n'est pas déjà présent).
     * @param colon      Un colon
     */
	
	public void ajouterColon(Colon colon) {
	    try {
	        if (colon == null) {
	            throw new IllegalArgumentException("Le colon ne peut pas être null");
	        }
	        if (!colons.contains(colon)) {
	            colons.add(colon);
	            System.out.println("Colon " + colon.getNom() + " ajouté à la colonie");
	        } else {
	            System.out.println("Le colon " + colon.getNom() + " existe déjà");
	        }
	    } catch (IllegalArgumentException e) {
	        System.err.println("Erreur : " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Erreur inattendue dans ajouterColon : " + e.getMessage());
	    }
	}

    
	/**
     * @Auteur: BELGUEDJ NASSILYA
     * Méthode pour ajouter une relation symétrique "ne s'aiment pas" entre deux colons.
     * Cela signifie que chaque colon est ajouté à la liste des relations de l'autre.
     * @param colon1            Nom du premier colon
     * @param colon2            Nom du deuxieme colon
     */
	public void ajouterRelation(Colon colon1, Colon colon2) {
	    try {
	        if (colon1 == null || colon2 == null) {
	            throw new IllegalArgumentException("Un ou les deux colons sont null.");
	        }
	        colon1.ajouterRelation(colon2);
	        colon2.ajouterRelation(colon1);
	        System.out.println("Relation ajoutée entre " + colon1.getNom() + " et " + colon2.getNom() + " avec succès !");
	    } catch (IllegalArgumentException e) {
	        System.err.println("Erreur : " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Erreur inattendue dans ajouterRelation : " + e.getMessage());
	    }
	}
	
	
    /**
     * @Auteur: BELGUEDJ NASSILYA
     * Méthode pour afficher les relations entre les colons.
     * Pour chaque colon affiche la liste des colons avec lesquels il a une relation "ne s'aiment pas".
     */
	public void afficherRelations() {
	    try {
	        for (Colon colon : colons) {
	            if (colon == null) {
	                throw new IllegalStateException("Un des colons dans la liste est null.");
	            }
	            System.out.println("Le colon " + colon.getNom() + " n'aime pas : " + colon.getRelations());
	        }
	    } catch (IllegalStateException e) {
	        System.err.println("Erreur : " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Erreur inattendue dans afficherRelations : " + e.getMessage());
	    }
	}

}
