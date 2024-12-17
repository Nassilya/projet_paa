package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class VerifieFichier {
	/**
	 * @author NAGULESWARAN ALICIA
	 * Vérifie la validité syntaxique et sémantique d'un fichier de configuration pour l'affectation des ressources.
	 * 
	 * @param fichier Le chemin du fichier à vérifier.
	 * @return true si le fichier est valide, false sinon.
	 * 
	 * La méthode procède comme suit :
	 * 1. Lit le fichier ligne par ligne en utilisant un BufferedReader.
	 * 2. Effectue plusieurs vérifications pour chaque ligne :
	 *    - Vérifie l'absence d'espaces non autorisés.
	 *    - Vérifie la présence d'un point en fin de ligne.
	 *    - Valide la syntaxe des éléments "colon", "ressource", "deteste" et "preferences".
	 *    - Assure le respect de l'ordre des blocs (colon -> ressource -> deteste -> preferences).
	 * 3. Compte et compare le nombre de colons et de ressources pour s'assurer de leur correspondance.
	 * 4. Affiche des messages d'erreur pour chaque problème détecté avec les détails de la ligne concernée.
	 * 5. Retourne true si aucune erreur n'a été trouvée, sinon retourne false.
	 * 
	 * @throws FileNotFoundException Si le fichier spécifié n'existe pas.
	 * @throws IOException           En cas d'erreur lors de la lecture du fichier.
	 */

	public static boolean verifie(String fichier) {
		try(BufferedReader buff=new BufferedReader(new FileReader(fichier))){
			String ligne;
			int numLigne=0, nbColon=0, nbRessources=0;
			boolean fichierValide=true;
			int etatActuel=0;
			while((ligne=buff.readLine())!=null){
				numLigne++;
				
				ligne = ligne.trim().replaceAll("\\s+", "");
				if (ligne.contains(" ")) {
				    System.out.println("Votre fichier contient des espaces à la ligne " + numLigne + ".");
				    fichierValide = false;
				}

				ligne = ligne.trim().replaceAll("\\s+", "").replace("\r", ""); // Supprimer espaces et retours chariots
				if (!ligne.endsWith(").")) {
				    System.out.println("Votre fichier ne contient pas un point à la fin de la ligne " + numLigne + ".");
				    fichierValide = false;
				}

				/*
				if (!ligne.startsWith("colon(") || !ligne.startsWith("ressource(") || !ligne.startsWith("deteste(") || !ligne.startsWith("preferences(")) {
					System.out.println("La ligne"+ numLigne + " ne commence ni par colon ni par ressource ni par deteste ni par preferences. \nVeuillez vérifier que votre syntaxe est correcte(pas de majuscule ni d'accents) et que vous n'avez pas ajouter autre fonction que colon, ressource, deteste, et preferences. \nATTENTION: votre fichier ne doit pas contenir d'espaces ni autre caractere que virgule parenthese et un point.");
				}
				*/			
				if (ligne.startsWith("colon")) {
					int paramColon=ligne.split("[(),.]").length;
					if (paramColon!=2) {
						System.out.println("Votre element colon qui se situe à la ligne "+numLigne+" n'a pas le bon nombre d'argument (1)");
						fichierValide=false;
					}
					nbColon++;
                   if (etatActuel > 1) {
                       System.out.println("L'ordre des lignes est incorrect à la ligne " + numLigne + ". On attendait 'colon' avant.");
                       fichierValide = false;
                   }
                   etatActuel = 1;
               }
               else if (ligne.startsWith("ressource")) {
               	int paramRessource=ligne.split("[(),.]").length;
               	if (paramRessource!=2) {
					System.out.println("Votre element ressource qui se situe à la ligne "+numLigne+" n'a pas le bon nombre d'argument (1)");
					fichierValide=false;
				}
           	nbRessources++;
               if (etatActuel > 2) {
                   System.out.println("L'ordre des lignes est incorrect à la ligne " + numLigne + ". On attendait 'ressource' avant.");
                   fichierValide = false;
               }
               etatActuel = 2;
           }
           else if (ligne.startsWith("deteste")) {
        	    String contenu = ligne.substring(ligne.indexOf("(") + 1, ligne.lastIndexOf(")"));
        	    String[] arguments = contenu.split(",");
        	    if (arguments.length != 2) {
        	        System.out.println("Votre element deteste qui se situe à la ligne " + numLigne + " n'a pas le bon nombre d'argument (2).");
        	        fichierValide = false;
        	    }
        	

           }
           else if (ligne.startsWith("preferences")) {
        	    if (etatActuel > 4) {
        	        System.out.println("L'ordre des lignes est incorrect à la ligne " + numLigne + ". On attendait 'preferences' avant.");
        	        fichierValide = false;
        	    }
        	    if (fichierValide) {
        	        // Nettoyer la ligne avant le split
        	        String contenu = ligne.trim().replaceAll("\\s+", "").replace(").", ")"); 
        	        
        	        // Extraire uniquement le contenu entre parenthèses
        	        contenu = contenu.substring(contenu.indexOf("(") + 1, contenu.lastIndexOf(")"));
        	        String[] arguments = contenu.split(",");

        	        // Vérification du nombre d'arguments
        	        if (arguments.length != nbColon + 1) {
        	            System.out.println("Votre élément preferences qui se situe à la ligne " + numLigne 
        	                               + " n'a pas le bon nombre d'arguments (attendu : " + (nbColon + 1) 
        	                               + ", trouvé : " + arguments.length + ")");
        	            fichierValide = false;
        	        } else {
        	            System.out.println("Preferences de la ligne " + numLigne + " correctement formatées.");
        	        }
        	    }
        	    etatActuel = 4;
        	}

			
		}
		if (nbColon!=nbRessources) {
			System.out.println("Il n'y a pas autant de ressource que de colon");
		}
		if(fichierValide) {System.out.println("Votre fichier est valide");
		}else {
			System.out.println("Votre fichier n'est pas valide, corriger votre fichier");
		}
		return fichierValide;
	}catch (FileNotFoundException e) {
		System.err.println(e.getMessage());
		System.exit(1);
		return false;
	}catch(IOException e) {
		System.err.println("Erreur lors de la lecture de votre fichier : "+ e.getMessage());
		return false;
	}
  }
}


