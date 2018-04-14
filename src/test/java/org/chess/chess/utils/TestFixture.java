package org.chess.chess.utils;

import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Plateau;
import org.chess.chess.joueur.Joueur;
import org.chess.chess.notation.NotationFEN;

import static org.mockito.Mockito.mock;

public class TestFixture {

	public static Plateau createFromFen(String str) {
		NotationFEN notationFEN = new NotationFEN();
		return notationFEN.createPlateau(str).getPlateau();
	}

	public static Partie createPartie(Plateau plateau) {
		Joueur joueurBlanc = createJoueurMock();
		Joueur joueurNoir = createJoueurMock();
		return new Partie(plateau, joueurBlanc, joueurNoir, Couleur.Blanc);
	}

	public static Joueur createJoueurMock() {
		return mock(Joueur.class);
	}

	public static String showFen(Plateau plateau) {
		NotationFEN notationFEN = new NotationFEN();
		return notationFEN.serialize(createPartieFromPlateau(plateau));
	}

	public static Partie createPartieFromPlateau(Plateau plateau) {
		return new Partie(plateau, createJoueurMock(), createJoueurMock(), Couleur.Blanc);
	}
}
