package bowling;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {

    private String[] joueurs;                     // Les noms
    private PartieMonoJoueur[] parties;           // Une partie par joueur
    private int joueurCourant = 0;                // Index du joueur qui joue
    private boolean demarree = false;             // Est-ce que la partie a commencé ?

    @Override
    public String demarreNouvellePartie(String[] nomsDesJoueurs) {

        if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
            throw new IllegalArgumentException("Il faut au moins un joueur");
        }

        joueurs = nomsDesJoueurs;
        parties = new PartieMonoJoueur[joueurs.length];

        // On crée une PartieMonoJoueur pour chaque joueur
        for (int i = 0; i < joueurs.length; i++) {
            parties[i] = new PartieMonoJoueur();
        }

        joueurCourant = 0;   // On commence par le 1er joueur
        demarree = true;

        // On renvoie le message demandé
        return messageProchainTir();
    }

    @Override
    public String enregistreLancer(int nombreDeQuillesAbattues) {

        if (!demarree) {
            throw new IllegalStateException("La partie n'est pas démarrée");
        }

        PartieMonoJoueur partieDuJoueur = parties[joueurCourant];

        // true = il rejoue dans le même tour
        boolean rejoue = partieDuJoueur.enregistreLancer(nombreDeQuillesAbattues);

        // Si le joueur ne rejoue pas → on passe au suivant
        if (!rejoue) {
            joueurCourant = (joueurCourant + 1) % joueurs.length;
        }

        // Si tout le monde a fini → partie terminée
        if (toutesLesPartiesSontFinies()) {
            return "Partie terminée";
        }

        return messageProchainTir();
    }

    @Override
    public int scorePour(String nomDuJoueur) {

        // Chercher le joueur
        for (int i = 0; i < joueurs.length; i++) {
            if (joueurs[i].equals(nomDuJoueur)) {
                return parties[i].score();
            }
        }

        throw new IllegalArgumentException("Joueur inconnu");
    }

    /** Vérifie si toutes les parties individuelles sont terminées */
    private boolean toutesLesPartiesSontFinies() {
        for (PartieMonoJoueur p : parties) {
            if (!p.estTerminee()) {
                return false;
            }
        }
        return true;
    }

    /** Construit le message "Prochain tir..." */
    private String messageProchainTir() {
        PartieMonoJoueur partie = parties[joueurCourant];

        return "Prochain tir : joueur " + joueurs[joueurCourant]
                + ", tour n° " + partie.numeroTourCourant()
                + ", boule n° " + partie.numeroProchainLancer();
    }
}
