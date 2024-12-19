package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Ressource {
	private int id;
    private String nom;
    private Map<Colon, List<Colon>> relations = new HashMap<>();
    private Colon colonAttribue;
   
    public Ressource(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Constructeur
     */
    public Ressource(int id) {
        this.id = id;
        }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Retourne le colon actuellement associé à cette ressource
     * 
     * @return Le colon auquel cette ressource est attribuée, ou null si elle est libre
     */
    public Colon getColonAttribue() {
        return colonAttribue;
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Définit si cette ressource est attribuée ou libre
     * 
     * @param attribuee true pour marquer la ressource comme attribuée, false sinon
     */
    public void setAttribuee(boolean attribuee) {
        this.attribuee = attribuee;
    }


    
    /**
     * @author BELGUEDJ NASSILYA
     * Retourne la liste des colons avec lesquels un colon donné entretient une relation
     * 
     * @param colon Le colon dont les relations doivent être récupérées
     * @return Une liste contenant les colons en relation avec le colon spécifié
     */
    public List<Colon> getRelationsDeColon(Colon colon) {
        return relations.getOrDefault(colon, new ArrayList<>());
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner l'id
     * @return l'id
     */
    public int getId() {
        return id;
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner le nom
     * @return le nom
     */
    public String getNom() {
        return nom; // Getter pour le nom
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * Ajoute une relation de "détestation" symétrique entre deux colons
     * 
     * @param colon1 Le premier colon
     * @param colon2 Le deuxième colon
     */
    public void ajouterRelation(Colon colon1, Colon colon2) {
        relations.computeIfAbsent(colon1, k -> new ArrayList<>()).add(colon2);
        relations.computeIfAbsent(colon2, k -> new ArrayList<>()).add(colon1); // Relation symétrique
    }
    
    /**
     * @author BELGUEDJ NASSILYA
     * @Override
     * Retourne une représentation sous forme de chaîne de l'objet Ressource
     * 
     * @return La valeur de l'identifiant de la ressource en tant que chaîne
     */
    @Override
    public String toString() {
        return String.valueOf(id);  
    }

    
    /**
     * @author BELGUEDJ NASSILYA
     * @Override
     * Vérifie l'égalité entre deux objets Ressource en comparant leur identifiant (id)
     * et leur nom (nom)
     * 
     * @param obj L'objet à comparer
     * @return true si les deux objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ressource ressource = (Ressource) obj;
        return id == ressource.id && Objects.equals(nom, ressource.nom);
    }


    /**
     * @author BELGUEDJ NASSILYA
     * @Override
     * Génère un code de hachage unique basé sur les propriétés pertinentes
     * (id et nom) de la ressource
     * 
     * @return Un entier représentant le code de hachage
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nom); 
    }

    

    /**
     * @author BELGUEDJ NASSILYA
     * Associe cette ressource à un colon spécifique
     * 
     * @param colon Le colon à qui attribuer cette ressource
     */
    public void attribuerA(Colon colon) {
        this.colonAttribue = colon;
    }


    /**
     * @author BELGUEDJ NASSILYA
     * Libère cette ressource, supprimant son association avec un colon
     */
    public void liberer() {
        this.colonAttribue = null; 
    }

   

    private boolean attribuee;

    /**
     * @author BELGUEDJ NASSILYA
     * Vérifie si cette ressource est actuellement attribuée
     * 
     * @return true si la ressource est attribuée, false sinon
     */
    public boolean estAttribuee() {
        return attribuee;
    }


    

}
