package org.chess.chess.utils;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.service.InformationPartieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PartieFixture {

	public static final Logger LOG = LoggerFactory.getLogger(PartieFixture.class);

	public static Partie createPartieDebut() {
		Plateau plateau = TestFixture.createFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		Partie partie = createPartie(plateau);
		return partie;
	}

	public static Partie createPartieVide() {
		Plateau plateau = TestFixture.createFromFen("8/8/8/8/8/8/8/8");
		Partie partie = createPartie(plateau);
		return partie;
	}

	// crée un plateau avec une seule case occupé par le pion blanc en d4
	public static Partie createPartieUnePiece(Piece piece) {
		Verify.verifyNotNull(piece);
		String str = "8/8/8/8/3" + Character.toUpperCase(piece.getNomCourtAnglais()) + "4/8/8/8";
		LOG.info("fen:{}", str);
		Plateau plateau = TestFixture.createFromFen(str);
		Partie partie = createPartie(plateau);
		return partie;
	}

	public static Partie createPartie(Plateau plateau) {
		return new Partie(plateau, Couleur.Blanc, createInformationService());
	}

	public static InformationPartie createInformationService() {
		InformationPartieService informationPartieService = new InformationPartieService();
		return informationPartieService.createInformationPartie();
	}
}
