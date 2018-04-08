package org.chess.chess.utils;

import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Plateau;


public class PartieFixture {

	public static Partie createPartieDebut() {
		Plateau plateau = TestFixture.createFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		Partie partie = new Partie(plateau,
				TestFixture.createJoueurMock(),
				TestFixture.createJoueurMock(), Couleur.Blanc);
		return partie;
	}
}
