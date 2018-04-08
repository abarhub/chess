package org.chess.chess.moteur;

import com.google.common.collect.Lists;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.chess.chess.domain.*;
import org.chess.chess.outils.IteratorPlateau;
import org.chess.chess.outils.PositionTools;
import org.chess.chess.utils.PartieFixture;
import org.chess.chess.utils.TestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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

		Plateau plateau = TestFixture.createFromFen(fenFormat);

		Partie partie = TestFixture.createPartie(plateau);

		boolean attaque = calculMouvementsService.caseAttaque(partie, joueur, PositionTools.getPosition(ligne, colonne));

		assertEquals(attaqueRef, attaque);
	}

	private Object[] createCalculMouvementsValues() {
		return new Object[]{
				new Object[]{PartieFixture.createPartieDebut(), createMouvementDebutPartie()},
				//new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R", 1, 5, Couleur.Blanc, true},
				//new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR", 0, 6, Couleur.Blanc, true},
		};
	}

	@Test
	@Parameters(method = "createCalculMouvementsValues")
	public void calculMouvements(Partie partie, ListeMouvements listeMouvementsRef) {

		assertNotNull(listeMouvementsRef);

		ListeMouvements listeMouvement = calculMouvementsService.calculMouvements(partie);

		LOGGER.info("listeMouvement={}", listeMouvement);

		assertNotNull(listeMouvement);
		//assertEquals(listeMouvementsRef, listeMouvement);
		assertTrue(compare(listeMouvementsRef, listeMouvement));
	}

	// methodes utilitaires

	private ListeMouvements createMouvementDebutPartie() {
		ListeMouvements listeMouvements;

		listeMouvements = new ListeMouvements();

		Map<PieceCouleurPosition, List<Mouvement>> map = new HashMap<>();

		for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
			map.put(new PieceCouleurPosition(Piece.PION, Couleur.Noir, new Position(RangeeEnum.RANGEE7, colonne)),
					Lists.newArrayList(createMouvement(RangeeEnum.RANGEE6, colonne, false),
							createMouvement(RangeeEnum.RANGEE5, colonne, false)));
		}


		for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
			map.put(new PieceCouleurPosition(Piece.PION, Couleur.Blanc, new Position(RangeeEnum.RANGEE2, colonne)),
					Lists.newArrayList(createMouvement(RangeeEnum.RANGEE3, colonne, false),
							createMouvement(RangeeEnum.RANGEE4, colonne, false)));
		}

		map.put(new PieceCouleurPosition(Piece.CAVALIER, Couleur.Noir, new Position(RangeeEnum.RANGEE8, ColonneEnum.COLONNEB)),
				Lists.newArrayList(createMouvement(RangeeEnum.RANGEE6, ColonneEnum.COLONNEA, false),
						createMouvement(RangeeEnum.RANGEE6, ColonneEnum.COLONNEC, false)));

		map.put(new PieceCouleurPosition(Piece.CAVALIER, Couleur.Blanc, new Position(RangeeEnum.RANGEE1, ColonneEnum.COLONNEB)),
				Lists.newArrayList(createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEA, false),
						createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEC, false)));

		listeMouvements.setMapMouvements(map);

		return listeMouvements;
	}

	private Mouvement createMouvement(RangeeEnum rangee, ColonneEnum colonne, boolean attaque) {
		return new Mouvement(new Position(rangee, colonne), attaque);
	}

	public boolean compare(ListeMouvements listeMouvements0, ListeMouvements listeMouvements) {
		if (listeMouvements0.isRoiBlancEchecs() == listeMouvements.isRoiBlancEchecs() &&
				listeMouvements0.isRoiNoirEchecs() == listeMouvements.isRoiNoirEchecs() &&
				listeMouvements0.isRoiBlancEchecsMat() == listeMouvements.isRoiBlancEchecsMat() &&
				listeMouvements0.isRoiNoirEchecsMat() == listeMouvements.isRoiNoirEchecsMat()) {

			if (CollectionUtils.isEmpty(listeMouvements0.getMapMouvements())) {
				boolean res = CollectionUtils.isEmpty(listeMouvements.getMapMouvements());
				if (!res) {
					LOGGER.error("la 2eme liste a des mouvements");
				}
				return res;
			} else {

				Map<PieceCouleurPosition, List<Mouvement>> map = listeMouvements.getMapMouvements();

				if (CollectionUtils.isEmpty(map)) {
					LOGGER.error("la 2eme liste est vide");
					return false;
				}

				if (!listeMouvements0.getMapMouvements().keySet().equals(map.keySet())) {
					LOGGER.error("la 2eme liste n'a pas les mêmes cle ({}<>{})",
							listeMouvements0.getMapMouvements().keySet(),
							map.keySet());
					return false;
				}

				for (Map.Entry<PieceCouleurPosition, List<Mouvement>> entry : listeMouvements0.getMapMouvements().entrySet()) {

					PieceCouleurPosition pieceCouleurPosition = entry.getKey();

					if (!map.containsKey(pieceCouleurPosition)) {
						LOGGER.error("la 2eme liste n'a pas la cle {}", pieceCouleurPosition);
						return false;
					} else {
						List<Mouvement> list = map.get(pieceCouleurPosition);
						if (list.size() != entry.getValue().size()) {
							LOGGER.error("la 2eme liste n'a pas le même nombre de mouvement pour la cle {}", pieceCouleurPosition);
							return false;
						}

						for (Mouvement mouvement : entry.getValue()) {

							boolean res = list.stream()
									.anyMatch(x -> x.isAttaque() == mouvement.isAttaque()
											&& x.getPosition().equals(mouvement.getPosition()));

							if (!res) {
								LOGGER.error("la 2eme liste n'a pas le mouvement {} pour la cle {}",
										mouvement, pieceCouleurPosition);
								return false;
							}
						}
					}
				}


				return true;
			}

		} else {
			LOGGER.error("la 2eme liste n'a pas le même etat");
			return false;
		}
	}

}