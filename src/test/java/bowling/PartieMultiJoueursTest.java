package bowling;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartieMultiJoueursTest {

    private PartieMultiJoueurs partie;
    private String[] joueurs = {"Pierre", "Paul"};

    @BeforeEach
    void setUp() {
        partie = new PartieMultiJoueurs();
    }

    @Test
    void testDemarrageCorrect() {
        String msg = partie.demarreNouvellePartie(joueurs);
        assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1", msg);
    }

    @Test
    void testDemarrageSansJoueurs() {
        assertThrows(IllegalArgumentException.class,
                () -> partie.demarreNouvellePartie(new String[]{}));
    }

    @Test
    void testPremierJoueurPuisPaul() {
        partie.demarreNouvellePartie(joueurs);

        // Pierre joue 5
        assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2",
                partie.enregistreLancer(5));

        // Pierre joue 3 → Tour terminé → Paul commence
        assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1",
                partie.enregistreLancer(3));
    }

    @Test
    void testStrikePasseAuJoueurSuivant() {
        partie.demarreNouvellePartie(joueurs);

        // Pierre strike → Paul directement
        String msg = partie.enregistreLancer(10);
        assertTrue(msg.contains("joueur Paul"));
    }

    @Test
    void testScorePourChaqueJoueurCorrect() {
        partie.demarreNouvellePartie(joueurs);

        // Pierre : strike → 10 points (bonus pas encore pris car Pierre ne rejoue pas)
        partie.enregistreLancer(10);

        // Paul : 3 + 4
        partie.enregistreLancer(3);
        partie.enregistreLancer(4);

        // Scores :
        // Pierre : 10
        // Paul   : 7
        assertEquals(10, partie.scorePour("Pierre"));
        assertEquals(7, partie.scorePour("Paul"));
    }

    @Test
    void testScoreJoueurInconnu() {
        partie.demarreNouvellePartie(joueurs);
        assertThrows(IllegalArgumentException.class,
                () -> partie.scorePour("Jacques"));
    }

    @Test
    void testFinDePartie() {
        partie.demarreNouvellePartie(joueurs);

        String message = "";
        // 40 lancers : 20 pour Pierre et 20 pour Paul
        for (int i = 0; i < 40; i++) {
            message = partie.enregistreLancer(0);
        }

        assertEquals("Partie terminée", message);
    }
}
