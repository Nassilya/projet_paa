package app;
import java.util.Scanner;
import java.util.Set;

import gestion.GestionRelations;
import gestion.GestionAffectation;
import gestion.CalculateurDeCout;
import model.Colon;
import model.Ressource;
//import model.Ressource;
import parser.ColonieParser;
import parser.SauvegardeFichier;
import parser.VerifieFichier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;




import utilitaires.GestionnaireExceptions;
import utilitaires.Verificateur;

public class monAppli {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		 System.out.printf("\n");
		    System.out.printf("=================================================\n");
		    System.out.printf("             BIENVENUE DANS LA COLONIE           \n");
		    System.out.printf("=================================================\n");
		    System.out.printf("               /\\        /\\                      \n");
		    System.out.printf("              /  \\      /  \\                     \n");
		    System.out.printf("             /    \\____/    \\                    \n");
		    System.out.printf("            /      COLONS     \\                   \n");
		    System.out.printf("           /____________________\\                 \n");
		    System.out.printf("          ||                    ||                \n");
		    System.out.printf("          ||      Ressources    ||                \n");
		    System.out.printf("          ||      Relations     ||                \n");
		    System.out.printf("          ||____________________||                \n");
		    System.out.printf("         //______________________\\               \n");
		    System.out.printf("=================================================\n");
		    System.out.printf("             PRÊT À COMMANDER ?                  \n");
		    System.out.printf("=================================================\n\n");
		// Déclarer le Set en dehors de la structure switch pour qu'il persiste
       // Set<String> colonsAvecPreferences = new HashSet<>();
	    GestionRelations gr = new GestionRelations();
	    GestionAffectation gf = new GestionAffectation();
	 
	    
	    
	    
	   // GestionAffectation gf1 = new GestionAffectation(calculateur);

	    
	    Scanner sc = new Scanner(System.in);
	   
	    
	   // String cheminFichier = "testLecture.txt";
	    
	    
	    if (args.length > 0) {
            String cheminFichier = args[0]; // Chemin passé en argument
            File fichier = new File(cheminFichier);

            if (!fichier.exists()) {
                System.err.println("[ERREUR] Le fichier spécifié n'existe pas : " + cheminFichier);
                return;
            }

            // Vérification du fichier
            System.out.println("Vérification du fichier...");
            boolean fichierValide = VerifieFichier.verifie(cheminFichier);

            if (fichierValide) {
                System.out.println("[INFO] Fichier valide. Chargement des données...");
                ColonieParser.parser(cheminFichier);

                // Menu des options
                boolean continuer = true;
                while (continuer) {
                    System.out.println("\n=== MENU LAST ONE ===");
                    System.out.println("1. Résolution automatique");
                    System.out.println("2. Sauvegarder la solution actuelle");
                    System.out.println("3. Fin");
                    System.out.print("Votre choix : ");
                
                    int choix = -1;
                    try {
                        choix = Integer.parseInt(sc.nextLine().trim());

                        switch (choix) {
                        case 1:
                            System.out.println("[INFO] Résolution automatique...");

                            // Charger les données du fichier
                            gf = ColonieParser.parser(cheminFichier);

                            // Vérifier les ressources et colons enregistrés
                            // System.out.println("Nombre de colons dans gf : " + gf.getColons().size());
                            // gf.afficherRessources();

                            // Exécuter la solution naïve avant recherche locale
                            gf.proposerSolutionNaive();

                            // Vérifier les affectations initiales
                            System.out.println("[////////////////////////////////////// AVANT]");
                            gf.afficherAffectations();

                            
                            
                            // Calculer et afficher le nombre de jaloux avant la recherche locale
                            int nombreJalouxAvant = gf.getCalculateur().calculerNombreColonsJaloux(gf.getColons());
                            System.out.println("[INFO] Nombre de colons jaloux avant : " + nombreJalouxAvant);

                            // Lancer la recherche locale
                           gf.rechercheLocale(150);
                            
                          //  gf.branchAndBound(calculateur);
                            System.out.println("[INFO] Solution optimale trouvée.");

                            // Afficher les affectations finales
                            System.out.println("[//////////////////////////////////////// APRES]");
                            gf.affficherAffectations();

                            // Calculer et afficher le nombre de jaloux après la recherche locale
                            int nombreJalouxApres = gf.getCalculateur().calculerNombreColonsJaloux(gf.getColons());
                            System.out.println("[INFO] Nombre de colons jaloux après : " + nombreJalouxApres);

                            break;


                            case 2:
                                System.out.print("Entrez le nom du fichier de sauvegarde : ");
                                String cheminSauvegarde = sc.nextLine().trim();

                                System.out.print("Entrez le nombre d'itérations (k) pour la recherche locale : ");
                                int k = sc.nextInt(); // Lire le nombre d'itérations
                                sc.nextLine(); // Consommer la nouvelle ligne

                                // Appeler la méthode rechercheLocale
                               
                                List<Ressource> ressourcesAttribuees = gf.rechercheLocale(k);

                                // Sauvegarder la liste obtenue
                                SauvegardeFichier.sauvegarderAffectation(cheminSauvegarde, ressourcesAttribuees);
                                break;


                            case 3:
                                System.out.println("[INFO] Fin du programme. À bientôt !");
                                continuer = false;
                                break;

                            default:
                                System.out.println("[ERREUR] Option invalide. Veuillez choisir entre 1, 2 ou 3.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERREUR] Entrez un nombre valide (1, 2 ou 3).");
                   
                    }
                }
            } else {
                System.err.println("[ERREUR] Fichier invalide. Veuillez corriger les erreurs et réessayer.");
            }
        } else {
            System.out.println("=================================================");
            System.out.println("Aucun fichier de configuration fourni.");
            System.out.println("Passage à la construction et résolution manuelles.");
            System.out.println("=================================================");

            // Partie 1 : Construction et résolution manuelles
           constructionManuelle(gr, gf, sc);
        }

        sc.close();
}

	  
   //****************************************************************************************************
private static void constructionManuelle(GestionRelations gr, GestionAffectation gf, Scanner sc) {
	
	    try (Scanner sc1 = new Scanner(System.in)) {
			Set<String> colonsAvecPreferences = new HashSet<>();
			CalculateurDeCout calculateur = new CalculateurDeCout();
 
			 System.out.println("Combien de colons souhaitez-vous avoir dans votre colonie ?");
			int n = 0;

			// Boucle jusqu'à ce que l'utilisateur entre un entier valide
			while (true) {
			    try {
			        System.out.print("Veuillez entrer un nombre entier : ");
			        n = sc1.nextInt();
			        sc1.nextLine(); // Consomme la nouvelle ligne

			        // Vérifier que le nombre est positif
			        if (n <= 0) {
			            System.out.println("Erreur : Le nombre de colons doit être un entier positif.");
			            continue; // Redemander la saisie
			        }

			        // Si l'entrée est correcte, sortir de la boucle
			        break;
			    } catch (InputMismatchException e) {
			        // Gère les cas où l'entrée n'est pas un entier
			        System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
			        sc.nextLine(); // Consomme l'entrée incorrecte pour éviter une boucle infinie
			    }
			}


			// Crée les colons et les ressources en même temps
			gf.creerColonsEtRessources(n);
			List<Colon> colons = gf.getColons();
			
			for (Colon colon : colons) {
			    gr.ajouterColon(colon); // Ajoute chaque colon à GestionRelations
			}

			//int n1=-1;
			boolean continuer = true;
			while (continuer) {
				System.out.println("\n=== MENU PRINCIPAL ===");
				System.out.println("  1. Ajouter une relation entre deux colons");
				System.out.println("  2. Ajouter les préférences d'un colon");
				System.out.println("  3. Fin");
				System.out.print("Votre choix : ");

			    int n1 = -1; // Initialiser avec une valeur invalide
			    try {
			        n1 = Integer.parseInt(sc1.nextLine().trim()); // Lecture sécurisée de l'entrée

			        if (n1 < 1 || n1 > 3) {
			            System.out.println("Option invalide. Veuillez choisir 1, 2 ou 3.");
			            continue; // Revenir au début de la boucle
			        }
			    } catch (NumberFormatException e) {
			        System.out.println("Erreur : Veuillez entrer un entier valide (1, 2 ou 3).");
			        continue;
			    }

			    switch (n1) {
			    case 1:
			    	System.out.println("\n-----------------------------------------");
			        System.out.println("        AJOUT DE RALATION ENTRE DEUX COLONS");
			        System.out.println("-----------------------------------------\n");
			        try {
			            String nom1, nom2;

			            // Validation pour le premier colon
			            while (true) {
			            	
			                System.out.print("Nom du premier colon (un seul caractère alphabétique) : ");
			                nom1 = sc.nextLine().toUpperCase();
			                if (Verificateur.validerNomColon(nom1)) {
			                    Colon c1 = gf.trouverColon(nom1);
			                    if (c1 != null) {
			                        break; // Sort de la boucle si le colon existe
			                    } else {
			                    	System.out.println("\n-----------------------------------------");
			                    	System.out.println("ERREUR : Le colon" +nom1 + "n'existe pas !");
			                    	System.out.println("-----------------------------------------\n");

			                    }
			                } else {
			                    System.out.println("Erreur : Veuillez entrer un seul caractère alphabétique (A-Z).");
			                }
			            }

			            // Validation pour le deuxième colon
			            while (true) {
			                System.out.print("Nom du deuxième colon (un seul caractère alphabétique) : ");
			                nom2 = sc1.nextLine().toUpperCase();
			                if (Verificateur.validerNomColon(nom2)) {
			                    Colon c2 = gf.trouverColon(nom2);
			                    if (c2 != null) {
			                        gr.ajouterRelation(gf.trouverColon(nom1), c2);
			                        break; // Sort de la boucle si la relation est ajoutée avec succès
			                    } else {
			                    	System.out.println("\n-----------------------------------------");
			                    	System.out.println("ERREUR : Le colon" +nom2 + "n'existe pas !");
			                    	System.out.println("-----------------------------------------\n");

			                    }
			                } else {
			                    System.out.println("Erreur : Veuillez entrer un seul caractère alphabétique (A-Z).");
			                }
			            }
			        } catch (Exception e) {
			            GestionnaireExceptions.gererException(e, "Ajout de relation entre colons", "WARNING");
			        }
			        break;
			        
			     
			        
			    case 2:
			        System.out.println("\n-----------------------------------------");
			        System.out.println("        AJOUT DE PRÉFÉRENCE POUR UN COLON ");
			        System.out.println("-----------------------------------------\n");
			        try {
			            System.out.println("Vous avez choisi d'ajouter les préférences d'un colon");

			            Colon colon = null; // Déclaration de la variable colon
			            String inputLine;

			            // Lire le nom du colon et ses préférences dans une seule ligne
			            while (true) {
			                System.out.print("Indiquez le nom du colon suivi de ses préférences (séparées par un espace)");
			                inputLine = sc1.nextLine();

			                // Diviser la ligne en tokens
			                String[] tokens = inputLine.split("\\s+");
			                if (tokens.length < 2) {
			                    System.out.println("Erreur : saisie incomplète");
			                    continue;
			                }

			                // Extraire le nom du colon
			                String nomColon = tokens[0].toUpperCase();

			                // Valider le nom du colon
			                if (!Verificateur.validerNomColon(nomColon)) {
			                    System.out.println("Erreur : Veuillez entrer un seul caractère alphabétique (A-Z) pour le nom du colon.");
			                    continue;
			                }

			                // Vérifie si des préférences ont déjà été définies pour ce colon
			                if (colonsAvecPreferences.contains(nomColon)) {
			                    System.out.println("Erreur : Les préférences pour le colon " + nomColon + " ont déjà été définies.");
			                    return; // Sort de la case
			                }

			                // Trouver le colon
			                colon = gf.trouverColon(nomColon);
			                if (colon == null) {
			                    System.out.println("\n-----------------------------------------");
			                    System.out.println("ERREUR : Le colon " + nomColon + " n'existe pas !");
			                    System.out.println("-----------------------------------------\n");
			                    continue;
			                }

			                // Extraire les préférences (les éléments après le premier)
			                List<Integer> preferencesList = new ArrayList<>();
			                try {
			                    for (int i = 1; i < tokens.length; i++) {
			                        preferencesList.add(Integer.parseInt(tokens[i]));
			                    }
			                } catch (NumberFormatException e) {
			                    System.out.println("Erreur : Les préférences doivent être des nombres entiers.");
			                    continue;
			                }

			                // Vérification que le nombre de préférences est égal au nombre de colons
			                int nombreColons = gf.getColons().size();
			                if (preferencesList.size() != nombreColons) {
			                    System.out.println("Erreur : Le nombre de préférences doit être égal au nombre de colons (ici " + nombreColons + ").");
			                    continue;
			                }

			                // Validation avec Verificateur
			                if (Verificateur.verifierDoublons(preferencesList)) {
			                    System.out.println("Erreur : Les préférences contiennent des doublons !");
			                    continue;
			                }

			                if (!Verificateur.verifierRessourcesExistent(preferencesList, gf.getRessources())) {
			                    System.out.println("Erreur : Une ou plusieurs ressources dans les préférences n'existent pas.");
			                    return;
			                }

			                // Ajouter les préférences si tout est valide
			                int[] preferences = preferencesList.stream().mapToInt(Integer::intValue).toArray();
			                gf.ajouterPreferencesColon(colon, preferences);
			                colonsAvecPreferences.add(nomColon); // Ajoute le colon à la liste des préférences définies
			                System.out.println("Les préférences pour le colon " + nomColon + " ont été ajoutées avec succès.");
			                break;
			            }
			        } catch (Exception e) {
			            GestionnaireExceptions.gererException(e, "Ajout des préférences d'un colon", "WARNING");
			        }
			        break;

			        case 3:
			        	if (!gf.verifierPreferencesComplete()) {
			        		System.out.println("-------------------------------------------------");
			        		System.out.println("ERREUR : Tous les colons doivent avoir des préférences complètes !");
			        		System.out.println("-------------------------------------------------");

			    	        System.out.println("Veuillez compléter les préférences \n");
			    	       
			    	    }
			        	else {
			        		System.out.println("-------------------------------------------------");
			                System.out.printf("TOUS LES COLONS ONT DES PRÉFÉRENCES COMPLETES.\n");
			                System.out.printf("PASSAGE AU MENU 2 : GESTION ET INTERACTIONS.\n");
			                System.out.println("-------------------------------------------------");
			            continuer = false;
			            
			        	}
			            break;
			        	
			        default:
			            System.out.println("[ERREUR] Option invalide. Veuillez saisir un entier (1, 2 ou 3).");
			            break;
			    }
			}


			System.out.println("\n");
			System.out.println("===== PROPOSITION D'UNE SOLUTION NAÏVE =====");
			System.out.println("\n");
			gf.proposerSolutionNaive();
			System.out.println("\n");
			gf.afficherAffectations();
			System.out.println("\n");
			
   
			boolean programmeEnCours = true;
			
			while (programmeEnCours) {
				System.out.println("\n=== MENU 2 ===");
				 System.out.println("  1. Échanger les ressources de deux colons");
				 System.out.println("  2. Afficher le nombre de colons jaloux");
				 System.out.println("  3. Quitter le programme");
				 System.out.print("\nVotre choix : ");

			    int  choixMenu= -1; // Initialiser avec une valeur invalide
			    try {
			    	choixMenu = Integer.parseInt(sc.nextLine().trim()); 

			        if (choixMenu < 1 || choixMenu > 3) {
			            System.out.println("Option invalide. Veuillez choisir 1, 2 ou 3.");
			            continue; // Revenir au début de la boucle
			        }
			    } catch (NumberFormatException e) {
			        System.out.println("Erreur : Veuillez entrer un chiffre valide (1, 2 ou 3).");
			        continue;
			    }

			    switch (choixMenu) {

			    case 1:
			    	System.out.println("\n-----------------------------------------");
			        System.out.println("        ÉCHANGE DE RESSOURCES");
			        System.out.println("-----------------------------------------\n");
			        System.out.print("Nom du premier colon (un seul caractère alphabétique) : ");
			        String nomEchange1 = sc.nextLine().toUpperCase();

			        // Vérifie que le nom du premier colon est valide
			        if (!Verificateur.validerNomColon(nomEchange1)) {
			            System.out.println("Erreur : Veuillez entrer un seul caractère alphabétique (A-Z).");
			            break;
			        }

			        Colon colon1 = gf.trouverColon(nomEchange1);
			        if (colon1 == null) {
			            System.out.println("Erreur : Le colon " + nomEchange1 + " n'existe pas. Veuillez réessayer.");
			            break;
			        }

			        System.out.print("Nom du deuxième colon (un seul caractère alphabétique) : ");
			        String nomEchange2 = sc.nextLine().toUpperCase();

			        // Vérifie que le nom du deuxième colon est valide
			        if (!Verificateur.validerNomColon(nomEchange2)) {
			            System.out.println("Erreur : Veuillez entrer un seul caractère alphabétique (A-Z).");
			            break;
			        }

			        Colon colon2 = gf.trouverColon(nomEchange2);
			        if (colon2 == null) {
			            System.out.println("Erreur : Le colon " + nomEchange2 + " n'existe pas. Veuillez réessayer.");
			            break;
			        }

			        // Procede à l'échange si les deux noms sont valides et existent
			        gf.echangerRessources(nomEchange1, nomEchange2);
			        gf.afficherAffectations();
			        break;

			        case 2:
			        	System.out.println("\n-----------------------------------------");
			            System.out.println("        AFFICHAGE DES COLONS JALOUX");
			            System.out.println("-----------------------------------------\n");
			            System.out.println("Affichage des ressources avant le calcul des jaloux :");
			            gf.afficherAffectations();
			            calculateur.calculerNombreColonsJaloux(gf.getColons());
			            System.out.println("\nAffichage des ressources après le calcul des jaloux :");
			            gf.afficherAffectations();
			            gr.afficherRelations();
			            break;

			            
			        case 3:
			            programmeEnCours = false;
			            System.out.println("\n=========================================");
			            System.out.println("      MERCI D'AVOIR UTILISÉ LE PROGRAMME");
			            System.out.println("=========================================\n");
			           // System.out.println("[DEBUG] Taille des colons : " + gf.getColons().size());
			           // System.out.println("[DEBUG] Taille des ressources : " + gf.getRessources().size());
			          //  gf.getColons().forEach(c -> System.out.println("[DEBUG] Colon : " + c.getNom()));
			          //  gf.getRessources().forEach(r -> System.out.println("[DEBUG] Ressource : " + r.getId()));

			         // Calculer et afficher la solution optimale avant de quitter
			            System.out.println("Lancement de l'algorithme ...");
			          
			            
			            
			         // Calculer et afficher la solution optimale avant de quitter
			            System.out.println("[INFO] Lancement de l'algorithme de minimisation des jalousies...");

			         // Afficher l'état initial avant le lancement de l'algorithme
			            System.out.println("\n[INFO] État initial :");
			            System.out.println("AVANTTTT");
			            int jalousiesAvant = calculateur.calculerNombreColonsJaloux(colons);
			            System.out.println("[INFO] Nombre initial de colons jaloux : " + jalousiesAvant);
			            gf.afficherAffectations();

			            // Exécuter l'algorithme de minimisation des jalousies
			           gf.rechercheLocale(150);

			            // Afficher l'état final après l'exécution de l'algorithme
			            System.out.println("\n[INFO] État final :");
			            System.out.println("APRREEEEESSSSS.");
			            int jalousiesApres = calculateur.calculerNombreColonsJaloux(colons);
			            System.out.println("[INFO] Nombre final de colons jaloux : " + jalousiesApres);
			           gf. afficherAffectations();

			            
			            System.out.println("\nAffectation finale des ressources :");
			            gf.afficherAffectations();
			            break;
			            

			        default:
			        	System.out.println("[ERREUR] Option invalide. Veuillez saisir un entier (1, 2 ou 3).");
			            break;
			    }
			}
		}
	    sc.close();  
	

 }


   }
