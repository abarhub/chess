package org.chess.chess.moteur;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.chess.chess.Tools;
import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Plateau;
import org.chess.chess.outils.PositionTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class CalculMouvementsServiceTest {

	public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementsServiceTest.class);

	private CalculMouvementsService calculMouvementsService = new CalculMouvementsService();


	private Object[] createSerialisationValues() {
		return new Object[]{
				new Object[]{"rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R", 2, 5, Couleur.Blanc, true},
				new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R", 1, 5, Couleur.Blanc, true},
				new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR", 0, 6, Couleur.Blanc, true},
		};
	}

	@Test
	@Parameters(method = "createSerialisationValues")
	public void caseAttaque(String fenFormat, int ligne, int colonne,
	                        Couleur joueur, boolean attaqueRef) {

		LOGGER.info("caseAttaque({},{},{},{},{})", fenFormat, ligne, colonne, joueur, attaqueRef);

		Plateau plateau = Tools.createFromFen(fenFormat);

		boolean attaque = calculMouvementsService.caseAttaque(plateau, joueur, PositionTools.getPosition(ligne, colonne));

		assertEquals(attaqueRef, attaque);
	}
}