package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author BELGUEDJ NASSILYA
 * Classe pour la gestion des ressources dans le programme
 */
public class Ressource {
	private int id;
    private String nom;
    private Map<Colon, List<Colon>> relations = new HashMap<>();
    private Colon colonAttribue;
   

    public void ajouterRelation(Colon colon1, Colon colon2) {
        relations.computeIfAbsent(colon1, k -> new ArrayList<>()).add(colon2);
        relations.computeIfAbsent(colon2, k -> new ArrayList<>()).add(colon1); // Relation symétrique
    }
    public List<Colon> getRelationsDeColon(Colon colon) {
        return relations.getOrDefault(colon, new ArrayList<>());
    }


    
    
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
     * Méthode pour retourner l'id.
     * @return l'id
     */
    public int getId() {
        return id;
    }
    

    public String getNom() {
        return nom; // Getter pour le nom
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Méthode pour retourner uniquement l'identifiant de la ressource.
     * @return l'identifiant de la ressource.
     */
    @Override
    public String toString() {
        return String.valueOf(id); // 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ressource ressource = (Ressource) obj;
        return id == ressource.id && Objects.equals(nom, ressource.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom); // Ajoute les propriétés pertinentes
    }
    

    public void attribuerA(Colon colon) {
        this.colonAttribue = colon;
    }

    public void liberer() {
        this.colonAttribue = null; // Libère la ressource
    }

    public Colon getColonAttribue() {
        return colonAttribue;
    }
    private boolean attribuee;

    public boolean estAttribuee() {
        return attribuee;
    }

    public void setAttribuee(boolean attribuee) {
        this.attribuee = attribuee;
    }


}
