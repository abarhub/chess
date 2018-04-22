package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.outils.PositionTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.chess.chess.outils.IteratorPlateau.getIterablePlateau;

@Service
public class CalculMouvementsService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementsService.class);

	//@Autowired
	//private NotationService notationService;

	public ListeMouvements calculMouvements(Partie partie) {
		return calculMouvements(partie, true);
	}

	public ListeMouvements calculMouvements(Partie partie, boolean avecSuppressionSiEchecs) {

		Verify.verifyNotNull(partie);

		Plateau plateau = partie.getPlateau();
		Verify.verifyNotNull(plateau);

		ListeMouvements listeMouvements = new ListeMouvements();
		Map<PieceCouleurPosition, List<Mouvement>> mapMouvements = new HashMap<>();
		listeMouvements.setMapMouvements(mapMouvements);

		Stream<PieceCouleurPosition> str = plateau.getStreamPosition();

		if (str != null) {
			List<Mouvement> list;

			// calcul des déplacements possibles
			for (PieceCouleurPosition piece : str.collect(Collectors.toList())) {
				list = getMouvements(plateau, piece);
				if (!CollectionUtils.isEmpty(list)) {
					mapMouvements.put(piece, list);
				}
			}

			boolean echecRoiBlanc = false;
			boolean echecRoiNoir = false;

			// suppression des mouvements des rois interdits
			for (Map.Entry<PieceCouleurPosition, List<Mouvement>> pieceCouleurPositionListEntry : mapMouvements.entrySet()) {

				PieceCouleurPosition piece = pieceCouleurPositionListEntry.getKey();

				if (piece.getPiece() == Piece.ROI) {

					boolean roiAttaque = listeMouvements.caseAttaque(piece.getPosition(), piece.getCouleur());

					if (roiAttaque) {
						if (piece.getCouleur() == Couleur.Blanc) {
							echecRoiBlanc = true;
							listeMouvements.setRoiBlancEchecs(true);
						} else {
							echecRoiNoir = true;
							listeMouvements.setRoiNoirEchecs(true);
						}
					}

					List<Mouvement> liste = mapMouvements.get(piece);

					List<Mouvement> listeResultat = new ArrayList<>();

					for (Mouvement mouvement : liste) {

						boolean caseAttaque = listeMouvements.caseAttaque(mouvement.getPosition(), piece.getCouleur());

						if (!caseAttaque) {
							listeResultat.add(mouvement);
						}

					}

					pieceCouleurPositionListEntry.setValue(listeResultat);
				}
			}

			// vérification s'il y a echecs d'un des rois

			Verify.verify(!echecRoiBlanc || !echecRoiNoir);

			if (echecRoiBlanc) {

				List<Mouvement> liste2 = listeMouvements.getMouvements(new PieceCouleur(Piece.ROI, Couleur.Blanc));

				if (CollectionUtils.isEmpty(liste2)) {
					listeMouvements.setRoiBlancEchecsMat(true);
				}

			}

			if (echecRoiNoir) {

				List<Mouvement> liste2 = listeMouvements.getMouvements(new PieceCouleur(Piece.ROI, Couleur.Noir));

				if (CollectionUtils.isEmpty(liste2)) {
					listeMouvements.setRoiNoirEchecs(true);
				}

			}

			if (avecSuppressionSiEchecs) {
				if (listeMouvements.isRoiBlancEchecs() || listeMouvements.isRoiNoirEchecs()) {
					LOGGER.info("echecs : blanc={}, noir={}",
							listeMouvements.isRoiBlancEchecs(), listeMouvements.isRoiNoirEchecs());
					if (listeMouvements.isRoiBlancEchecs()) {
						LOGGER.info("echecs blanc");

						Verify.verify(partie.getJoueurCourant() == Couleur.Noir);

						supprimeMouvementsPourEchecs(listeMouvements, Couleur.Blanc, partie);

						if (CollectionUtils.isEmpty(listeMouvements.getMouvements(Couleur.Blanc))) {
							listeMouvements.setRoiBlancEchecsMat(true);
						}

					} else if (listeMouvements.isRoiNoirEchecs()) {
						LOGGER.info("echecs noir");

						Verify.verify(partie.getJoueurCourant() == Couleur.Blanc);

						supprimeMouvementsPourEchecs(listeMouvements, Couleur.Noir, partie);

						if (CollectionUtils.isEmpty(listeMouvements.getMouvements(Couleur.Noir))) {
							listeMouvements.setRoiNoirEchecsMat(true);
						}

					} else {

					}


				}
			}
		}

		return listeMouvements;
	}

	private void supprimeMouvementsPourEchecs(ListeMouvements listeMouvements, Couleur couleurJoueur, Partie partie) {

		Couleur couleurJoueurEchecs = couleurJoueur;
		Couleur couleurJoueurAdverse = couleurContraire(couleurJoueur);

		Verify.verify(partie.getJoueurCourant() == couleurJoueurAdverse);

		int i = 0;
		int j = 0;

		Map<PieceCouleurPosition, List<Mouvement>> map = listeMouvements.getMapMouvements();
		for (Map.Entry<PieceCouleurPosition, List<Mouvement>> entry : map.entrySet()) {

			PieceCouleurPosition p = entry.getKey();
			List<Mouvement> liste = entry.getValue();
			if (p.getCouleur() == couleurJoueurEchecs) {

				j = 0;

				Iterator<Mouvement> iter = liste.iterator();

				while (iter.hasNext()) {

					//LOGGER.info("obj({},{})={} {}", partie, partie.getPlateau());

					//LOGGER.info("plateau({},{})={}", i, j, partie.getPlateau().getRepresentation2());

					Mouvement mouvement = iter.next();

					boolean deplacementBloqueEchecs = false;

					Partie partie2 = new Partie(new Plateau(partie.getPlateau()),
							couleurJoueurEchecs, new InformationPartie(), new ConfigurationPartie(couleurJoueurEchecs));

//					if (LOGGER.isInfoEnabled()) {
//						LOGGER.info("partie={}", notationService.serialize(partie, NotationEnum.FEN));
//						LOGGER.info("partie2={}", notationService.serialize(partie2, NotationEnum.FEN));
//						LOGGER.info("move {} :{}=>{} ", couleurJoueur, p.getPosition(), mouvement.getPosition());
//					}

					partie2.setMove(p.getPosition(), mouvement.getPosition());

					List<Position> liste2 = listePieces(partie2.getPlateau(), new PieceCouleur(Piece.ROI, couleurJoueurEchecs));

					Verify.verify(liste2.size() == 1);

					Position p2 = liste2.get(0);

					if (!caseAttaque(partie2, couleurJoueurEchecs, p2)) {
						deplacementBloqueEchecs = true;
					}

					if (!deplacementBloqueEchecs) {
						iter.remove();
					}

					j++;
				}
			}

			i++;

		}

		// suppression des cle qui n'ont plus de valeurs
		Iterator<Map.Entry<PieceCouleurPosition, List<Mouvement>>> iter2 = map.entrySet().iterator();

		while (iter2.hasNext()) {

			Map.Entry<PieceCouleurPosition, List<Mouvement>> entry = iter2.next();

			if (CollectionUtils.isEmpty(entry.getValue())) {
				iter2.remove();
			}
		}

	}

	public Couleur couleurContraire(Couleur couleur) {
		Verify.verifyNotNull(couleur);
		if (couleur == Couleur.Blanc) {
			return Couleur.Noir;
		} else {
			return Couleur.Blanc;
		}
	}

	public List<Mouvement> getMouvements(Plateau plateau, PieceCouleurPosition piece) {
		List<Mouvement> list;
		list = null;
		switch (piece.getPiece()) {
			case PION:
				list = calculPion(piece, plateau);
				break;
			case CAVALIER:
				list = calculCavalier(piece, plateau);
				break;
			case FOU:
				list = calculFou(piece, plateau);
				break;
			case TOUR:
				list = calculTour(piece, plateau);
				break;
			case REINE:
				list = calculReine(piece, plateau);
				break;
			case ROI:
				list = calculRoi(piece, plateau);
				break;
		}
		return list;
	}

	private List<Mouvement> calculRoi(PieceCouleurPosition piece, Plateau plateau) {

		Verify.verifyNotNull(piece);
		Verify.verify(piece.getPiece() == Piece.ROI);

		List<Mouvement> mouvements = new ArrayList<>();

		for (int ligne2 = -1; ligne2 <= 1; ligne2++) {
			for (int colonne2 = -1; colonne2 <= 1; colonne2++) {
				if (!(ligne2 == 0 && colonne2 == 0)) {
					Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), ligne2, colonne2);
					if (optPosition.isPresent()) {
						ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
//						if (tousMouvementRois) {
//							ajoutePositionRois(plateau, liste, ligne3, colonne3, joueurCourant);
//						} else if (!caseAttaque(plateau, couleurContraire(piece.getCouleur()), ligne3, colonne3)) {
//							ajoutePositionRois(plateau, liste, ligne3, colonne3, joueurCourant);
//						}
					}
				}
			}
		}

		return mouvements;
	}

	private List<Mouvement> calculReine(PieceCouleurPosition piece, Plateau plateau) {

		Verify.verifyNotNull(piece);
		Verify.verify(piece.getPiece() == Piece.REINE);

		List<Mouvement> mouvements = new ArrayList<>();

		for (int j = 0; j < 8; j++) {
			int decalageLigne, decalageColonne;
			if (j == 0) {
				decalageLigne = 1;
				decalageColonne = 0;
			} else if (j == 1) {
				decalageLigne = 0;
				decalageColonne = 1;
			} else if (j == 2) {
				decalageLigne = -1;
				decalageColonne = 0;
			} else if (j == 3) {
				decalageLigne = 0;
				decalageColonne = -1;
			} else if (j == 4) {// diagonales
				decalageLigne = 1;
				decalageColonne = 1;
			} else if (j == 5) {
				decalageLigne = 1;
				decalageColonne = -1;
			} else if (j == 6) {
				decalageLigne = -1;
				decalageColonne = 1;
			} else {
				decalageLigne = -1;
				decalageColonne = -1;
			}
			ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
		}

		return mouvements;
	}

	private List<Mouvement> calculTour(PieceCouleurPosition piece, Plateau plateau) {

		Verify.verifyNotNull(piece);
		Verify.verify(piece.getPiece() == Piece.TOUR);

		List<Mouvement> mouvements = new ArrayList<>();

		for (int j = 0; j < 4; j++) {
			int decalageLigne, decalageColonne;
			if (j == 0) {
				decalageLigne = 1;
				decalageColonne = 0;
			} else if (j == 1) {
				decalageLigne = 0;
				decalageColonne = 1;
			} else if (j == 2) {
				decalageLigne = -1;
				decalageColonne = 0;
			} else {
				decalageLigne = 0;
				decalageColonne = -1;
			}
			ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
		}

		return mouvements;
	}

	private List<Mouvement> calculFou(PieceCouleurPosition piece, Plateau plateau) {
		Verify.verifyNotNull(piece);
		Verify.verify(piece.getPiece() == Piece.FOU);

		List<Mouvement> mouvements = new ArrayList<>();

		for (int j = 0; j < 4; j++) {
			int decalageLigne, decalageColonne;
			if (j == 0) {
				decalageLigne = 1;
				decalageColonne = 1;
			} else if (j == 1) {
				decalageLigne = 1;
				decalageColonne = -1;
			} else if (j == 2) {
				decalageLigne = -1;
				decalageColonne = 1;
			} else {
				decalageLigne = -1;
				decalageColonne = -1;
			}
			ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
		}

		return mouvements;
	}


	private void ajouteDecalage(List<Mouvement> mouvements, Position position,
	                            int decalageLigne, int decalageColonne,
	                            PieceCouleurPosition piece, Plateau plateau) {
		Verify.verifyNotNull(mouvements);
		Verify.verifyNotNull(piece);
		Verify.verifyNotNull(plateau);
		for (int i = 1; i <= 8; i++) {
			boolean res = false;
			Optional<Position> optPosition = PositionTools.getPosition(position, decalageLigne * i, decalageColonne * i);
			if (optPosition.isPresent()) {
				res = ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
			}
			if (!res) {
				break;
			}
		}
	}

	private List<Mouvement> calculCavalier(PieceCouleurPosition piece, Plateau plateau) {
		Verify.verifyNotNull(piece);
		Verify.verify(piece.getPiece() == Piece.CAVALIER);

		List<Mouvement> mouvements = new ArrayList<>();

		Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), -2, -1);
		if (optPosition.isPresent()) {
			ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), -2, 1);
		if (optPosition.isPresent()) {
			ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), 1, -2);
		if (optPosition.isPresent()) {
			ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), -1, -2);
		if (optPosition.isPresent()) {
			ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), 1, 2);
		if (optPosition.isPresent()) {
			ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), -1, 2);
		if (optPosition.isPresent()) {
			ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), 2, -1);
		if (optPosition.isPresent()) {
			ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), 2, 1);
		if (optPosition.isPresent()) {
			ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
		}

		return mouvements;
	}

	private boolean ajoutePositionPiece(List<Mouvement> mouvements, Position position,
	                                    PieceCouleurPosition piece, Plateau plateau) {
		Verify.verifyNotNull(mouvements);
		Verify.verifyNotNull(piece);
		Verify.verifyNotNull(plateau);

		PieceCouleur caseCible = plateau.getCase(position);
		if (caseCible == null) {
			Mouvement mouvement = new Mouvement(position, false);
			mouvements.add(mouvement);
			return true;
		} else if (caseCible.getCouleur() != piece.getCouleur()) {
			Mouvement mouvement = new Mouvement(position, true);
			mouvements.add(mouvement);
			return false;
		}

		return false;
	}

	private List<Mouvement> calculPion(PieceCouleurPosition piece, Plateau plateau) {
		Verify.verifyNotNull(piece);
		Verify.verify(piece.getPiece() == Piece.PION);

		List<Mouvement> mouvements = new ArrayList<>();

		int decalage, decalage2 = 0;
		if (piece.getCouleur() == Couleur.Blanc) {
			decalage = 1;
			if (piece.getPosition().getRangee() == RangeeEnum.RANGEE2) {
				decalage2 = 2;
			}
		} else {
			decalage = -1;
			if (piece.getPosition().getRangee() == RangeeEnum.RANGEE7) {
				decalage2 = -2;
			}
		}
		Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), decalage, 0);
		if (optPosition.isPresent()) {
			ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, false);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), decalage, -1);
		if (optPosition.isPresent()) {
			ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, true);
		}
		optPosition = PositionTools.getPosition(piece.getPosition(), decalage, 1);
		if (optPosition.isPresent()) {
			ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, true);
		}
		if (decalage2 != 0) {
			PieceCouleur caseIntermediaire = null;
			if (decalage2 > 0) {
				optPosition = PositionTools.getPosition(piece.getPosition(), decalage2 - 1, 0);
				if (optPosition.isPresent()) {
					caseIntermediaire = plateau.getCase(optPosition.get());
				}
			} else {
				optPosition = PositionTools.getPosition(piece.getPosition(), decalage2 + 1, 0);
				if (optPosition.isPresent()) {
					caseIntermediaire = plateau.getCase(optPosition.get());
				}
			}
			if (caseIntermediaire == null) {
				optPosition = PositionTools.getPosition(piece.getPosition(), decalage2, 0);
				if (optPosition.isPresent()) {
					ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, false);
				}
			}
		}

		return mouvements;
	}

	private void ajoutePositionPions(List<Mouvement> mouvements, Position position,
	                                 PieceCouleurPosition piece, Plateau plateau, boolean mangePiece) {
		Verify.verifyNotNull(mouvements);
		Verify.verifyNotNull(piece);
		Verify.verifyNotNull(plateau);
		Verify.verifyNotNull(position);

		PieceCouleur caseCible = plateau.getCase(position);
		if (mangePiece) {
			if (caseCible != null && caseCible.getCouleur() != piece.getCouleur()) {
				Mouvement mouvement = new Mouvement(position, true);
				mouvements.add(mouvement);
			}
		} else {
			if (caseCible == null) {
				Mouvement mouvement = new Mouvement(position, false);
				mouvements.add(mouvement);
			}
		}
	}

	public List<Position> listMove(Partie partie, Position position, boolean tousMouvementRois, Couleur joueurCourant) {

		List<Position> list = new ArrayList<>();

		ListeMouvements listeMouvements = calculMouvements(partie);

		if (listeMouvements != null) {
			List<Mouvement> liste2 = listeMouvements.getMouvements(position);

			if (!CollectionUtils.isEmpty(liste2)) {

				for (Mouvement mouvement : liste2) {
					list.add(mouvement.getPosition());
				}
			}
		}

		return list;
	}

	public boolean caseAttaque(Partie partie, Couleur couleur, Position position) {

		ListeMouvements listeMouvements = calculMouvements(partie, false);

		if (listeMouvements != null) {
			return listeMouvements.caseAttaque(position, couleur);
		}

		return false;
	}


	public List<Position> listePieces(Plateau plateau, PieceCouleur pieceCouleur) {
		Verify.verifyNotNull(pieceCouleur);
		List<Position> liste = new ArrayList<>();
		for (Position position : getIterablePlateau()) {
			PieceCouleur piece = plateau.getCase(position);
			if (piece != null && piece.getCouleur() == pieceCouleur.getCouleur()
					&& piece.getPiece() == pieceCouleur.getPiece()) {
				liste.add(position);
			}
		}
		return liste;
	}
}
