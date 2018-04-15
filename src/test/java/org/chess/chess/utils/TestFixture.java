package org.chess.chess.utils;

import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Plateau;
import org.chess.chess.joueur.Joueur;
import org.chess.chess.notation.NotationFEN;
import org.chess.chess.service.InformationPartieService;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.mock;

public class TestFixture {

	public static Plateau createFromFen(String str) {
		NotationFEN notationFEN = createNotationFEN();
		return notationFEN.createPlateau(str).getPlateau();
	}

	private static NotationFEN createNotationFEN() {
		NotationFEN notationFEN = new NotationFEN();
		InformationPartieService informationPartieService = new InformationPartieService();
		ReflectionTestUtils.setField(notationFEN, "informationPartieService", informationPartieService);
		return notationFEN;
	}

	public static Partie createPartie(Plateau plateau) {
		return new Partie(plateau, Couleur.Blanc, PartieFixture.createInformationService());
	}

	public static Joueur createJoueurMock() {
		return mock(Joueur.class);
	}

	public static String showFen(Plateau plateau) {
		NotationFEN notationFEN = createNotationFEN();
		return notationFEN.serialize(createPartieFromPlateau(plateau));
	}

	public static Partie createPartieFromPlateau(Plateau plateau) {
		return new Partie(plateau, Couleur.Blanc, PartieFixture.createInformationService());
	}
}
