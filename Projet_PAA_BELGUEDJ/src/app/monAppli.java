package app;
import java.util.Scanner;
import gestion.GestionRelations;
import gestion.GestionAffectation;
import gestion.CalculateurDeCout;
import model.Colon;
//import model.Ressource;

import java.util.ArrayList;
import java.util.List;

//This is url du dépot
//https://github.com/Nassilya/projet_paa.git

public class monAppli {

	public static void main(String[] args) {
	    GestionRelations gr = new GestionRelations();
	    GestionAffectation gf = new GestionAffectation();
	    CalculateurDeCout calculateur = new CalculateurDeCout();
	    System.out.println("Bienvenue cher commandant !");
	    System.out.println("Combien de colons souhaitez vous avoir dans votre colonie ?");
	    Scanner sc = new Scanner(System.in);
	    int n = sc.nextInt();
	    sc.nextLine(); // Consomme la nouvelle ligne

	    // Crée les colons et les ressources en même temps
	    gf.creerColonsEtRessources(n);
	    List<Colon> colons = gf.getColons();
	    
	    for (Colon colon : colons) {
	        gr.ajouterColon(colon); // Ajoute chaque colon à GestionRelations
	    }

	    
	    boolean continuer = true;
	    while (continuer) {
	        System.out.println("Choisissez une option parmi les 3 :");
	        System.out.println("1. Ajouter une relation entre deux colons");
	        System.out.println("2. Ajouter les préférences d'un colon");
	        System.out.println("3. Fin");
	        int n1 = sc.nextInt();
	        sc.nextLine(); // Consomme la nouvelle ligne
	        if (n1 < 1 || n1 > 3) {
	            System.out.println("Option invalide. Veuillez choisir 1, 2 ou 3.");
	            continue; // Revenir au début de la boucle
	        }
	        switch (n1) {
	            case 1:
	                System.out.print("Nom du premier colon : ");
	                String nom1 = sc.nextLine().toUpperCase();
	                System.out.print("Nom du deuxième colon : ");
	                String nom2 = sc.nextLine().toUpperCase();

	                Colon c1 = gf.trouverColon(nom1);
	                Colon c2 = gf.trouverColon(nom2);

	                if (c1 != null && c2 != null) {
	                    gr.ajouterRelation(c1, c2);
	                } else {
	                    System.out.println("Erreur : Un ou les deux colons n'existent pas.");
	                }
	                break;

	            case 2:
	                System.out.println("Vous avez choisi d'ajouter les préférences d'un colon");
	                System.out.print("Veuillez entrer le nom du colon : ");
	                String nomColon = sc.nextLine().toUpperCase();
	                Colon colon = gf.trouverColon(nomColon);
	                if (colon != null) {
	                    System.out.println("Veuillez entrer les préférences du colon ex : 1,2,3...");
	                    String preferencesLine = sc.nextLine();
	                    Scanner lineScanner = new Scanner(preferencesLine);
	                    List<Integer> preferencesList = new ArrayList<>();
	                    while (lineScanner.hasNextInt()) {
	                        preferencesList.add(lineScanner.nextInt());
	                    }
	                    lineScanner.close();
	                    int[] preferences = preferencesList.stream().mapToInt(Integer::intValue).toArray();
	                    gf.ajouterPreferencesColon(colon, preferences);
	                } else {
	                    System.out.println("Erreur : Colon non trouvé.");
	                }
	                break;

	            case 3:
	                continuer = false;
	                System.out.println("Vous avez choisi de nous quitter :'( ");
	                gf.verifierPreferencesCompletes();
	                break;

	            default:
	                System.out.println("Option non reconnue, veuillez réessayer.");
	                break;
	        }
	    }

	    System.out.println("Proposition d'une solution naïve d'affectation...");
	    gf.proposerSolutionNaive();
	    gf.afficherAffectation();

	    // Menu d'interaction
	    boolean programmeEnCours = true;
	    while (programmeEnCours) {
	        System.out.println("\nMenu 2 :");
	        System.out.println("1. Échanger les ressources de deux colons");
	        System.out.println("2. Afficher le nombre de colons jaloux");
	        System.out.println("3. Quitter le programme");

	        int choixMenu = sc.nextInt();
	        sc.nextLine(); // Consomme la nouvelle ligne
	        
	        if (choixMenu < 1 || choixMenu > 3) {
	            System.out.println("Option invalide. Veuillez choisir 1, 2 ou 3.");
	            continue; // Revenir au début de la boucle
	        }

	        switch (choixMenu) {
	            case 1:
	                System.out.print("Nom du premier colon : ");
	                String nomEchange1 = sc.nextLine().toUpperCase();
	                System.out.print("Nom du deuxième colon : ");
	                String nomEchange2 = sc.nextLine().toUpperCase();
	                gf.echangerRessources(nomEchange1, nomEchange2);
	                gf.afficherAffectation();
	                break;

	            case 2:
	                System.out.println("Affichage des ressources avant le calcul des jaloux :");
	                gf.afficherAffectation();
	                calculateur.calculerNombreColonsJaloux(gf.getColons());
	                System.out.println("Affichage des ressources après le calcul des jaloux :");
	                gf.afficherAffectation();
	                gr.afficherRelations();
	                break;

	            case 3:
	                programmeEnCours = false;
	                System.out.println("Vous avez choisi de nous quitter. À très bientôt !");
	             // Calculer et afficher la solution optimale avant de quitter
	                gf.trouverSolutionOptimale(calculateur); //Dans GestionAffectation
	                break;
	                

	            default:
	                System.out.println("Option invalide. Veuillez choisir 1, 2 ou 3.");
	                break;
	        }
	    }

	    sc.close();
	}

}
