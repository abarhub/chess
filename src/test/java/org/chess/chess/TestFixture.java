package org.chess.chess;

import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Plateau;
import org.chess.chess.joueur.Joueur;
import org.chess.chess.notation.NotationFEN;

import static org.mockito.Mockito.mock;

public class TestFixture {

	public static Plateau createFromFen(String str) {
		NotationFEN notationFEN = new NotationFEN();
		return notationFEN.createPlateau(str);
	}

	public static Partie createPartie(Plateau plateau){
		Joueur joueurBlanc = mock(Joueur.class);
		Joueur joueurNoir = mock(Joueur.class);
		return new Partie(plateau, joueurBlanc, joueurNoir, Couleur.Blanc);
	}
}
