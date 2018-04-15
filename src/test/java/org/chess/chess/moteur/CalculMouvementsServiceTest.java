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

import java.util.*;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class CalculMouvementsServiceTest {

	public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementsServiceTest.class);

	private CalculMouvementsService calculMouvementsService = new CalculMouvementsService();


	private Object[] createSerialisationValues() {
		return new Object[]{
				//new Object[]{"rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R", getPosition(2, 5), Couleur.Blanc, true},
				new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R", getPosition(1, 5), Couleur.Blanc, true},
				new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR", getPosition(0, 6), Couleur.Blanc, true},
		};
	}

	@Test
	@Parameters(method = "createSerialisationValues")
	public void caseAttaque(String fenFormat, Position position,
	                        Couleur joueur, boolean attaqueRef) {

		LOGGER.info("caseAttaque({},{},{},{})", fenFormat, position, joueur, attaqueRef);

		assertNotNull(fenFormat);
		assertNotNull(position);
		assertNotNull(joueur);

		Plateau plateau = TestFixture.createFromFen(fenFormat);

		//plateau.afficheConsole();
		//LOGGER.info("plateau:{}", plateau.getRepresentation());

		Partie partie = TestFixture.createPartie(plateau);

		boolean attaque = calculMouvementsService.caseAttaque(partie, joueur, position);

		assertEquals(attaqueRef, attaque);
	}

	private Object[] createCalculMouvementsValues() {
		return new Object[]{
				new Object[]{PartieFixture.createPartieDebut(), createMouvementDebutPartie()},
				new Object[]{PartieFixture.createPartieVide(), new ListeMouvements()},
				new Object[]{PartieFixture.createPartieUnePiece(Piece.PION), createMouvementPiece(Piece.PION)},
				new Object[]{PartieFixture.createPartieUnePiece(Piece.ROI), createMouvementPiece(Piece.ROI)},
				new Object[]{PartieFixture.createPartieUnePiece(Piece.TOUR), createMouvementPiece(Piece.TOUR)},
				new Object[]{PartieFixture.createPartieUnePiece(Piece.FOU), createMouvementPiece(Piece.FOU)},
				new Object[]{PartieFixture.createPartieUnePiece(Piece.CAVALIER), createMouvementPiece(Piece.CAVALIER)},
				new Object[]{PartieFixture.createPartieUnePiece(Piece.REINE), createMouvementPiece(Piece.REINE)},
				new Object[]{PartieFixture.createPartieFromFen("rnQ1kb2/pp1p1p1r/7n/8/4p3/5P2/PP1PP1P1/R1B1KBN1"),
						createMouvementPiece(Piece.ROI, getPosition(RangeeEnum.RANGEE8, ColonneEnum.COLONNEE),
								getPosition(RangeeEnum.RANGEE7, ColonneEnum.COLONNEE))},
				//new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R", 1, 5, Couleur.Blanc, true},
				//new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR", 0, 6, Couleur.Blanc, true},
		};
	}

	@Test
	@Parameters(method = "createCalculMouvementsValues")
	public void calculMouvements(Partie partie, ListeMouvements listeMouvementsRef) {

		assertNotNull(listeMouvementsRef);

		LOGGER.info("fen:{}", TestFixture.showFen(partie.getPlateau()));

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

		map.put(new PieceCouleurPosition(Piece.CAVALIER, Couleur.Noir, new Position(RangeeEnum.RANGEE8, ColonneEnum.COLONNEG)),
				Lists.newArrayList(createMouvement(RangeeEnum.RANGEE6, ColonneEnum.COLONNEF, false),
						createMouvement(RangeeEnum.RANGEE6, ColonneEnum.COLONNEH, false)));

		map.put(new PieceCouleurPosition(Piece.CAVALIER, Couleur.Blanc, new Position(RangeeEnum.RANGEE1, ColonneEnum.COLONNEB)),
				Lists.newArrayList(createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEA, false),
						createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEC, false)));

		map.put(new PieceCouleurPosition(Piece.CAVALIER, Couleur.Blanc, new Position(RangeeEnum.RANGEE1, ColonneEnum.COLONNEG)),
				Lists.newArrayList(createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEF, false),
						createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEH, false)));

		listeMouvements.setMapMouvements(map);

		return listeMouvements;
	}

	private ListeMouvements createMouvementPiece(Piece piece) {

		assertNotNull(piece);

		ListeMouvements listeMouvements = new ListeMouvements();

		Map<PieceCouleurPosition, List<Mouvement>> map = new HashMap<>();

		listeMouvements.setMapMouvements(map);

		Position positionDepart = new Position(RangeeEnum.RANGEE4, ColonneEnum.COLONNED);

		if (piece == Piece.PION) {
			map.put(new PieceCouleurPosition(Piece.PION, Couleur.Blanc, positionDepart),
					Lists.newArrayList(createMouvement(RangeeEnum.RANGEE5, ColonneEnum.COLONNED, false)));
		} else if (piece == Piece.ROI) {
			map.put(new PieceCouleurPosition(Piece.ROI, Couleur.Blanc, positionDepart),
					Lists.newArrayList(createMouvement(RangeeEnum.RANGEE5, ColonneEnum.COLONNEC, false),
							createMouvement(RangeeEnum.RANGEE5, ColonneEnum.COLONNED, false),
							createMouvement(RangeeEnum.RANGEE5, ColonneEnum.COLONNEE, false),
							createMouvement(RangeeEnum.RANGEE4, ColonneEnum.COLONNEC, false),
							createMouvement(RangeeEnum.RANGEE4, ColonneEnum.COLONNEE, false),
							createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEC, false),
							createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNED, false),
							createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEE, false)));
		} else if (piece == Piece.TOUR) {

			List<Mouvement> liste = Lists.newArrayList();

			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				if (colonne != positionDepart.getColonne()) {
					liste.add(createMouvement(positionDepart.getRangee(), colonne, false));
				}
			}

			for (RangeeEnum rangeeEnum : IteratorPlateau.getIterableRangee()) {
				if (rangeeEnum != positionDepart.getRangee()) {
					liste.add(createMouvement(rangeeEnum, positionDepart.getColonne(), false));
				}
			}

			map.put(new PieceCouleurPosition(Piece.TOUR, Couleur.Blanc, positionDepart), liste);

		} else if (piece == Piece.FOU) {

			List<Mouvement> liste = Lists.newArrayList();

			for (int j = 0; j < 4; j++) {

				int decalageLigne = 0;
				int decalageColonne = 0;

				if (j == 0) {
					decalageLigne = 1;
					decalageColonne = 1;
				} else if (j == 1) {
					decalageLigne = 1;
					decalageColonne = -1;
				} else if (j == 2) {
					decalageLigne = -1;
					decalageColonne = 1;
				} else if (j == 3) {
					decalageLigne = -1;
					decalageColonne = -1;
				}

				for (int i = 1; i <= 8; i++) {
					Optional<Position> optPosition = PositionTools.getPosition(positionDepart,
							decalageLigne * i, decalageColonne * i);
					if (optPosition.isPresent()) {
						liste.add(createMouvement(optPosition.get().getRangee(),
								optPosition.get().getColonne(), false));
					}
				}
			}

			map.put(new PieceCouleurPosition(Piece.FOU, Couleur.Blanc, positionDepart), liste);

		} else if (piece == Piece.CAVALIER) {

			map.put(new PieceCouleurPosition(Piece.CAVALIER, Couleur.Blanc, positionDepart),
					Lists.newArrayList(createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEB, false),
							createMouvement(RangeeEnum.RANGEE2, ColonneEnum.COLONNEC, false),
							createMouvement(RangeeEnum.RANGEE5, ColonneEnum.COLONNEB, false),
							createMouvement(RangeeEnum.RANGEE6, ColonneEnum.COLONNEC, false),
							createMouvement(RangeeEnum.RANGEE6, ColonneEnum.COLONNEE, false),
							createMouvement(RangeeEnum.RANGEE5, ColonneEnum.COLONNEF, false),
							createMouvement(RangeeEnum.RANGEE3, ColonneEnum.COLONNEF, false),
							createMouvement(RangeeEnum.RANGEE2, ColonneEnum.COLONNEE, false)));

		} else if (piece == Piece.REINE) {

			List<Mouvement> liste = Lists.newArrayList();

			for (int j = 0; j < 4; j++) {

				int decalageLigne = 0;
				int decalageColonne = 0;

				if (j == 0) {
					decalageLigne = 1;
					decalageColonne = 1;
				} else if (j == 1) {
					decalageLigne = 1;
					decalageColonne = -1;
				} else if (j == 2) {
					decalageLigne = -1;
					decalageColonne = 1;
				} else if (j == 3) {
					decalageLigne = -1;
					decalageColonne = -1;
				}

				for (int i = 1; i <= 8; i++) {
					Optional<Position> optPosition = PositionTools.getPosition(positionDepart,
							decalageLigne * i, decalageColonne * i);
					if (optPosition.isPresent()) {
						liste.add(createMouvement(optPosition.get().getRangee(),
								optPosition.get().getColonne(), false));
					}
				}
			}

			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				if (colonne != positionDepart.getColonne()) {
					liste.add(createMouvement(positionDepart.getRangee(), colonne, false));
				}
			}

			for (RangeeEnum rangeeEnum : IteratorPlateau.getIterableRangee()) {
				if (rangeeEnum != positionDepart.getRangee()) {
					liste.add(createMouvement(rangeeEnum, positionDepart.getColonne(), false));
				}
			}

			map.put(new PieceCouleurPosition(Piece.REINE, Couleur.Blanc, positionDepart), liste);

		} else {
			throw new IllegalArgumentException("Piece " + piece + " non géré");
		}

		return listeMouvements;
	}

	private Mouvement createMouvement(RangeeEnum rangee, ColonneEnum colonne, boolean attaque) {
		return new Mouvement(new Position(rangee, colonne), attaque);
	}

	public boolean compare(ListeMouvements listeMouvementsRef, ListeMouvements listeMouvements) {
		if (listeMouvementsRef.isRoiBlancEchecs() == listeMouvements.isRoiBlancEchecs() &&
				listeMouvementsRef.isRoiNoirEchecs() == listeMouvements.isRoiNoirEchecs() &&
				listeMouvementsRef.isRoiBlancEchecsMat() == listeMouvements.isRoiBlancEchecsMat() &&
				listeMouvementsRef.isRoiNoirEchecsMat() == listeMouvements.isRoiNoirEchecsMat()) {

			if (CollectionUtils.isEmpty(listeMouvementsRef.getMapMouvements())) {
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

				if (!listeMouvementsRef.getMapMouvements().keySet().equals(map.keySet())) {
					LOGGER.error("la 2eme liste n'a pas les mêmes cle ({}<>{})",
							listeMouvementsRef.getMapMouvements().keySet(),
							map.keySet());
					return false;
				}

				for (Map.Entry<PieceCouleurPosition, List<Mouvement>> entry : listeMouvementsRef.getMapMouvements().entrySet()) {

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

	private Position getPosition(int ligne, int colonne) {
		return PositionTools.getPosition(ligne, colonne);
	}

	private Position getPosition(RangeeEnum rangeeEnum, ColonneEnum colonneEnum) {
		return new Position(rangeeEnum, colonneEnum);
	}

	private ListeMouvements createMouvementPiece(Piece piece, Position positionDepart, Position... deplacements) {

		ListeMouvements listeMouvements = new ListeMouvements();

		Map<PieceCouleurPosition, List<Mouvement>> map = new HashMap<>();

		listeMouvements.setMapMouvements(map);

		List<Mouvement> mouvementList = new ArrayList<>();

		if (deplacements != null) {
			for (Position position : deplacements) {
				mouvementList.add(createMouvement(position.getRangee(), position.getColonne(), false));
			}
		}

		map.put(new PieceCouleurPosition(piece, Couleur.Blanc, positionDepart), mouvementList);

		return listeMouvements;
	}

}