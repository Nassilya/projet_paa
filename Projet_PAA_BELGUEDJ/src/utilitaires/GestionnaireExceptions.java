package utilitaires;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestionnaireExceptions {

    private static final String LOG_FILE = "erreurs_gestion_affectation.log"; // Fichier de log

    /**
     * @author BELGUEDJ NASSILYA
     * Gère une exception avec un niveau de gravité spécifique
     *
     * @param e        Exception levée
     * @param contexte Contexte où l'erreur s'est produite
     * @param niveau   Niveau de gravité (INFO, WARNING, CRITICAL)
     */
    public static void gererException(Exception e, String contexte, String niveau) {
        String message = formaterMessage(e, contexte, niveau);
        System.err.println(message); // Afficher dans la console
        logErreurDansFichier(message); // Enregistrer dans un fichier
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Gère une exception critique et arrête le programme
     *
     * @param e        Exception levée
     * @param contexte Contexte où l'erreur s'est produite
     */
    public static void gererErreurCritique(Exception e, String contexte) {
        String message = formaterMessage(e, contexte, "CRITICAL");
        System.err.println(message);
        logErreurDansFichier(message);
        System.exit(1); // Arrêter le programme
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Formate un message détaillé pour une exception
     *
     * @param e        Exception levée
     * @param contexte Contexte où l'erreur s'est produite
     * @param niveau   Niveau de gravité
     * @return Un message détaillé
     */
    private static String formaterMessage(Exception e, String contexte, String niveau) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String typeException = e.getClass().getSimpleName();
        String cause = (e.getCause() != null) ? e.getCause().toString() : "Aucune cause";

        return String.format("[%s] [%s] Contexte: %s | Exception: %s | Message: %s | Cause: %s",
                timestamp, niveau, contexte, typeException, e.getMessage(), cause);
    }

    /**
     * @author BELGUEDJ NASSILYA
     * Enregistre l'erreur dans un fichier de log
     *
     * @param message Message d'erreur à enregistrer
     */
    private static void logErreurDansFichier(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n");
        } catch (IOException ioe) {
            System.err.println("Impossible d'écrire dans le fichier de log : " + ioe.getMessage());
        }
    }
}
