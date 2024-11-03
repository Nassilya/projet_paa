package app;
import java.util.Scanner;
import gestion.GestionRelations;
import model.Colon;

//This is url du dépot 
//https://github.com/Nassilya/projet_paa.git
public class monAppli {

	public static void main(String[] args) {
		GestionRelations gr=new GestionRelations();
		System.out.println("Bienvenue cher commandant ! ");
		System.out.println("Combien de colons souhaitez vous avoir dans votre colonie ? ");
		Scanner sc = new Scanner(System.in);
		int n=sc.nextInt();
	
		boolean continuer=true;
		while(continuer) {
			System.out.println("Choisissez une option parmi les 3 :");
			System.out.println("1.Ajouter une relation entre deux colons");
			System.out.println("2.Ajouter les préférances d'un colon");
			System.out.println("3.Fin");
			int x=sc.nextInt();
			sc.nextLine();
			switch(n) {
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
				Colon a=new Colon(sc.next());
				
				
			}
		}
		
	
		
sc.close();
	}

}
