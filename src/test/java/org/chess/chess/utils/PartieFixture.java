package org.chess.chess.utils;

import com.google.common.base.Verify;
import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Piece;
import org.chess.chess.domain.Plateau;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PartieFixture {

	public static final Logger LOG = LoggerFactory.getLogger(PartieFixture.class);

	public static Partie createPartieDebut() {
		Plateau plateau = TestFixture.createFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		Partie partie = new Partie(plateau,
				//TestFixture.createJoueurMock(),
				//TestFixture.createJoueurMock(),
				Couleur.Blanc);
		return partie;
	}

	public static Partie createPartieVide() {
		Plateau plateau = TestFixture.createFromFen("8/8/8/8/8/8/8/8");
		Partie partie = new Partie(plateau,
				//TestFixture.createJoueurMock(),
				//TestFixture.createJoueurMock(),
				Couleur.Blanc);
		return partie;
	}

	// crée un plateau avec une seule case occupé par le pion blanc en d4
	public static Partie createPartieUnePiece(Piece piece) {
		Verify.verifyNotNull(piece);
		String str = "8/8/8/8/3" + Character.toUpperCase(piece.getNomCourtAnglais()) + "4/8/8/8";
		LOG.info("fen:{}", str);
		Plateau plateau = TestFixture.createFromFen(str);
		Partie partie = new Partie(plateau,
				//TestFixture.createJoueurMock(),
				//TestFixture.createJoueurMock(),
				Couleur.Blanc);
		return partie;
	}
}
