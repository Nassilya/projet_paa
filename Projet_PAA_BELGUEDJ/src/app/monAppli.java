package app;
import java.util.Scanner;
import gestion.GestionRelations;
import gestion.GestionAffectation;
import model.Colon;

//This is url du dépot 
//https://github.com/Nassilya/projet_paa.git
public class monAppli {

	public static void main(String[] args) {
		GestionRelations gr=new GestionRelations();
		GestionAffectation gf=new GestionAffectation();
		System.out.println("Bienvenue cher commandant ! ");
		System.out.println("Combien de colons souhaitez vous avoir dans votre colonie ? ");
		Scanner sc = new Scanner(System.in);
		int n=sc.nextInt();
		
		for (int i = 0; i < n; i++) {
            char nomColon = (char) ('A' + i); // Convertit en lettre de A à Z
            Colon colon = new Colon(String.valueOf(nomColon));
            gr.ajouterColon(colon); // Méthode dans GestionRelations pour ajouter un colon
        } 
		
		boolean continuer=true;
		while(continuer) {
			System.out.println("Choisissez une option parmi les 3 :");
			System.out.println("1.Ajouter une relation entre deux colons");
			System.out.println("2.Ajouter les préférances d'un colon");
			System.out.println("3.Fin");
			int n1=sc.nextInt();
			sc.nextLine();
			switch(n1) {
			case 1:
				System.out.println("Vous avez choisi d'jouter une relation entre deux colons");
				System.out.println("Veuillez entrer le 1er colon : ");
				//String a=sc.next();
				Colon c1=new Colon(sc.next());
				System.out.println("Veuillez entrer le 2eme colon : ");
				Colon c2=new Colon(sc.next());
				gr.ajouterRelation(c1,c2);
				break;
			case 2:
				System.out.println("Vous avez choisi d'jouter les préférances d'un colon");
				System.out.println("Veuillez entrer le nom du colon : ");
				 String nomColon = sc.nextLine().toUpperCase();
				 Colon cn = new Colon(nomColon);
				 System.out.println("Veuillez entrer les préférances du colon ex : 1,2,3... ");
				 String preferencesLine=sc.nextLine();
				 Scanner lineScanner=new Scanner(preferencesLine);
				 
				 gf.ajouterPreferencesColon(cn,);
				 break;
				
			case 3:
				continuer=false;
				System.out.println("Vous avez choisi de nous quitter :'( ");
				gf.verifierPreferencesCompletes();
				break;
				 
			default:
                System.out.println("Option non reconnue, veuillez réessayer.");
                break;
                
				
			}
		}
		
	
		
sc.close();
	}

}
