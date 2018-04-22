package org.chess.chess.moteur;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.chess.chess.domain.*;
import org.chess.chess.outils.PositionTools;
import org.chess.chess.utils.TestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class MouvementServiceTest {

	public static final Logger LOGGER = LoggerFactory.getLogger(MoteurTest.class);

	private MouvementService mouvementService;

	private CalculMouvementsService calculMouvementsService;

	@Before
	public void setup() {
		calculMouvementsService = new CalculMouvementsService();

		mouvementService = new MouvementService();
		ReflectionTestUtils.setField(mouvementService, "calculMouvementsService", calculMouvementsService);
	}

	private Object[] listMoveRoiValues() {
		return new Object[]{
				new Object[]{4, 4, Couleur.Blanc, createListPosition(3, 3,
						3, 4,
						3, 5,
						4, 5,
						5, 5,
						5, 4,
						5, 3,
						4, 3)},
				new Object[]{0, 0, Couleur.Blanc, createListPosition(1, 0,
						1, 1,
						0, 1)},
				new Object[]{0, 5, Couleur.Blanc, createListPosition(1, 4,
						1, 5,
						1, 6,
						0, 4,
						0, 6)},
				new Object[]{5, 0, Couleur.Blanc, createListPosition(4, 0,
						4, 1,
						5, 1,
						6, 0,
						6, 1)},
		};
	}

	@Test
	@Parameters(method = "listMoveRoiValues")
	public void listMoveRoi(int ligne, int colonne, Couleur joueurCourant, List<Position> resultatsAttendus) {
		LOGGER.info("listMoveRoiCentre");

		List<PieceCouleurPosition> liste = createPieces(Piece.ROI, joueurCourant, ligne, colonne);
		Plateau plateau = new Plateau(liste);

		LOGGER.info("plateau={}", plateau.getRepresentation());

		Partie partie = TestFixture.createPartie(plateau);

		// methode testée
		List<Position> res = mouvementService.listMove(partie, createPosition(ligne, colonne),
				false, joueurCourant);

		// vérifications
		LOGGER.info("res={}", res);
		if (resultatsAttendus == null || resultatsAttendus.isEmpty()) {
			assertTrue(res == null || res.isEmpty());
		} else {
			assertNotNull(res);
			assertEquals(res.toString(), resultatsAttendus.size(), res.size());
			for (Position p : resultatsAttendus) {
				assertTrue(res.contains(p));
			}
		}
	}

	private Object[] listMovePionValues() {
		return new Object[]{
				new Object[]{6, 0, Couleur.Blanc, createListPosition(5, 0, 4, 0)},
				new Object[]{4, 5, Couleur.Blanc, createListPosition(3, 5)},
				new Object[]{4, 0, Couleur.Blanc, createListPosition(3, 0)},
				new Object[]{1, 0, Couleur.Noir, createListPosition(2, 0, 3, 0)},
				new Object[]{2, 0, Couleur.Noir, createListPosition(3, 0)},
				new Object[]{0, 0, Couleur.Blanc, createListPosition()},
				new Object[]{1, 0, Couleur.Blanc, createListPosition(0, 0)},
				new Object[]{7, 0, Couleur.Noir, createListPosition()},
				new Object[]{6, 0, Couleur.Noir, createListPosition(7, 0)},
				//new Object[]{17, false},
				//new Object[]{18, true},
				//new Object[]{22, true}
		};
	}

	@Test
	@Parameters(method = "listMovePionValues")
	public void listMovePion(int ligne, int colonne, Couleur joueurCourant, List<Position> resultats) {
		LOGGER.info("listMovePion({},{},{},{})", ligne, colonne, joueurCourant, resultats);

		List<PieceCouleurPosition> liste = createPieces(Piece.PION, joueurCourant, ligne, colonne);
		Plateau plateau = new Plateau(liste);

		LOGGER.info("plateau={}", plateau.getRepresentation());

		Partie partie = TestFixture.createPartie(plateau);

		// methode testée
		List<Position> res = mouvementService.listMove(partie, createPosition(ligne, colonne), false, joueurCourant);

		// vérifications
		LOGGER.info("res={}", res);
		if (resultats == null || resultats.isEmpty()) {
			assertTrue(res == null || res.isEmpty());
		} else {
			assertNotNull(res);
			assertEquals(res.toString(), resultats.size(), res.size());
			for (Position p : resultats) {
				assertTrue(res.contains(p));
			}
		}
	}


	// methodes utilitaires


	private List<PieceCouleurPosition> createPieces(Piece roi, Couleur couleur, int ligne, int colonne) {
		List<PieceCouleurPosition> liste = new ArrayList<>();
		liste.add(new PieceCouleurPosition(roi, couleur, PositionTools.getPosition(ligne, colonne)));
		return liste;
	}

	private Position createPosition(int ligne, int colonne) {
		return PositionTools.getPosition(ligne, colonne);
	}

	private List<Position> createListPosition(int... tab) {
		List<Position> liste = new ArrayList<>();
		if (tab != null && tab.length > 0) {
			assertTrue(tab.length % 2 == 0);
			for (int i = 0; i < tab.length; i += 2) {
				Position p = PositionTools.getPosition(tab[i], tab[i + 1]);
				liste.add(p);
			}
		}
		return liste;
	}
}