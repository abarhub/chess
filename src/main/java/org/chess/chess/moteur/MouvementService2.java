package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.outils.Check;
import org.chess.chess.outils.IteratorPlateau;
import org.chess.chess.outils.PositionTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MouvementService2 {

	public static final Logger LOGGER = LoggerFactory.getLogger(MouvementService2.class);

	public List<Position2> listMove(Plateau plateau, Position2 position, boolean tousMouvementRois,
	                                Couleur joueurCourant) {
		return listMove2(plateau, position, tousMouvementRois, joueurCourant);
	}

	private List<Position2> listMove2(Plateau plateau, Position2 position, boolean tousMouvementRois,
	                                  Couleur joueurCourant) {
		Check.checkLigneColonne(PositionTools.getLigne(position), PositionTools.getColonne(position));
		List<Position2> liste = new ArrayList<>();

		PieceCouleur piece = plateau.getCase(position);
		if (piece != null) {
			if (piece.getPiece() == Piece.PION) {
				int decalage, decalage2 = 0;
				if (piece.getCouleur() == Couleur.Blanc) {
					decalage = 1;
					if (position.getRangee() == RangeeEnum.RANGEE2) {
						decalage2 = 2;
					}
				} else {
					decalage = -1;
					if (position.getRangee() == RangeeEnum.RANGEE7) {
						decalage2 = -2;
					}
				}
				ajoutePositionPions(plateau, liste, position, decalage, 0,
						piece.getCouleur(), false);
				ajoutePositionPions(plateau, liste, position, decalage, -1,
						piece.getCouleur(), true);
				ajoutePositionPions(plateau, liste, position, decalage, 1,
						piece.getCouleur(), true);
				if (decalage2 != 0) {
					ajoutePositionPions(plateau, liste, position, decalage2, 0,
							piece.getCouleur(), false);
				}
			} else if (piece.getPiece() == Piece.CAVALIER) {
				ajoutePositionPiece(plateau, liste, position, -2, -1,
						piece.getCouleur());
				ajoutePositionPiece(plateau, liste, position, -2, +1,
						piece.getCouleur());
				ajoutePositionPiece(plateau, liste, position, 1, -2,
						piece.getCouleur());
				ajoutePositionPiece(plateau, liste, position, -1, -2,
						piece.getCouleur());
				ajoutePositionPiece(plateau, liste, position, 1, 2,
						piece.getCouleur());
				ajoutePositionPiece(plateau, liste, position, -1, 2,
						piece.getCouleur());
				ajoutePositionPiece(plateau, liste, position, 2, -1,
						piece.getCouleur());
				ajoutePositionPiece(plateau, liste, position, 2, 1,
						piece.getCouleur());
			} else if (piece.getPiece() == Piece.FOU) {
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
					ajouteDecalage(plateau, liste, position, decalageLigne, decalageColonne, piece.getCouleur());
				}
			} else if (piece.getPiece() == Piece.TOUR) {
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
					ajouteDecalage(plateau, liste, position, decalageLigne, decalageColonne, piece.getCouleur());
				}
			} else if (piece.getPiece() == Piece.REINE) {
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
					ajouteDecalage(plateau, liste, position, decalageLigne, decalageColonne, piece.getCouleur());
				}
			} else if (piece.getPiece() == Piece.ROI) {
				for (int ligne2 = -1; ligne2 <= 1; ligne2++) {
					for (int colonne2 = -1; colonne2 <= 1; colonne2++) {
						if (!(ligne2 == 0 && colonne2 == 0)) {
							Optional<Position2> pos = PositionTools.getPosition(position, ligne2, colonne2);
							//final int ligne3 = position.getLigne() + ligne2;
							//final int colonne3 = position.getColonne() + colonne2;
							//if (Check.isPositionValide(ligne3, colonne3)) {
							if (pos.isPresent()) {
								if (tousMouvementRois) {
									ajoutePositionRois(plateau, liste, pos.get(), 0, 0, joueurCourant);
								} else if (!caseAttaque(plateau, couleurContraire(piece.getCouleur()), pos.get())) {
									ajoutePositionRois(plateau, liste, pos.get(), 0, 0, joueurCourant);
								}
							}
						}
					}
				}
			}
		}

		return liste;
	}

	private void ajouteDecalage(Plateau plateau, List<Position2> liste, Position2 position,
	                            int decalageLigne, int decalageColonne, Couleur couleur) {
		Verify.verifyNotNull(liste);
		Verify.verifyNotNull(couleur);
		for (int i = 1; i <= 8; i++) {
			boolean res = ajoutePositionPiece(plateau, liste, position, decalageLigne * i,
					decalageColonne * i,
					couleur);
			if (!res) {
				break;
			}
		}
	}

	private void ajoutePositionRois(Plateau plateau, List<Position2> liste, Position2 position, int decalageLigne,
	                                int decalageColonne, Couleur couleur) {
		ajoutePosition(plateau, liste, position, decalageLigne, decalageColonne, couleur, true, false, true);
	}

	private void ajoutePositionPions(Plateau plateau, List<Position2> liste, Position2 position,
	                                 int decalageLigne, int decalageColonne,
	                                 Couleur couleur, boolean doitManger) {
		ajoutePosition(plateau, liste, position, decalageLigne, decalageColonne, couleur, false, doitManger, false);
	}

	private boolean ajoutePositionPiece(Plateau plateau, List<Position2> liste, Position2 position, int decalageLigne,
	                                    int decalageColonne, Couleur couleur) {
		return ajoutePosition(plateau, liste, position, decalageLigne, decalageColonne, couleur, true, false, false);
	}

	private boolean ajoutePosition(Plateau plateau, List<Position2> liste, Position2 position, int decalageLigne,
	                               int decalageColonne, Couleur couleur, boolean peutManger, boolean doitManger,
	                               boolean deplacementNonAttaquable) {
		Verify.verifyNotNull(liste);
		Verify.verifyNotNull(couleur);
		Optional<Position2> pos = PositionTools.getPosition(position, decalageLigne, decalageColonne);
		//if (Check.isPositionValide(ligne, colonne)) {
		if (pos.isPresent()) {
//			if (deplacementNonAttaquable) {
//				if (caseAttaque(couleurContraire(couleur), ligne, colonne)) {
//					return false;
//				}
//			}
			if (doitManger) {
				PieceCouleur piece = plateau.getCase(pos.get());
				if (piece != null && piece.getCouleur() != couleur) {
					liste.add(pos.get());
					return true;
				}
			} else {
				PieceCouleur piece = plateau.getCase(pos.get());
				if (piece == null) {
					liste.add(pos.get());
					return true;
				} else if (peutManger && piece.getCouleur() != couleur) {
					liste.add(pos.get());
					return false;
				}
			}
		}
		return false;
	}


	// vérifie si la case (ligne/colonne) est attaquée par une piece de couleur
	public boolean caseAttaque(Plateau plateau, Couleur couleur, Position2 position) {
		Verify.verifyNotNull(couleur);
		Verify.verifyNotNull(position);
		Verify.verifyNotNull(plateau);
		//Check.checkLigneColonne(position.getLigne(), position.getColonne());

		LOGGER.info("caseAttaque:debut({},{})", couleur, position);

		List<Position2> pieces = listePieces(plateau, couleur);

		LOGGER.info("pieces:{}", pieces);

		if (pieces != null) {
			Position2 positionRecherche = position;
			for (Position2 p : pieces) {
				if (p.getRangee() != position.getRangee() && p.getColonne() != position.getColonne()) {
					List<Position2> liste = listMove(plateau, p, true, couleurContraire(couleur));

					LOGGER.info("p:{} => listMove:{}", p, liste);

					if (liste != null) {
						if (liste.contains(positionRecherche)) {
							LOGGER.info("caseAttaque:fin=true");
							return true;
						}
					}
				}
			}
		}
		LOGGER.info("caseAttaque:fin=false");

		return false;
	}


	public Couleur couleurContraire(Couleur couleur) {
		Verify.verifyNotNull(couleur);
		if (couleur == Couleur.Blanc) {
			return Couleur.Noir;
		} else {
			return Couleur.Blanc;
		}
	}


	public List<Position2> listePieces(Plateau plateau, Couleur couleur) {
		Verify.verifyNotNull(couleur);
		List<Position2> liste = new ArrayList<>();
		//for (int i = 0; i < Plateau.NB_LIGNES; i++) {
		for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
			//for (int j = 0; j < Plateau.NB_COLONNES; j++) {
			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				PieceCouleur piece = plateau.getCase(new Position2(rangee, colonne));
				if (piece != null && piece.getCouleur() == couleur) {
					liste.add(new Position2(rangee, colonne));
				}
			}
		}
		return liste;
	}


//	public List<Position> getMovablePieces(Plateau plateau, Couleur joueur) {
//		Verify.verifyNotNull(joueur);
//		List<Position> listePieces;
//		List<Position> liste = listePieces(plateau, joueur);
//		listePieces = new ArrayList<>();
//		for (Position p : liste) {
//			List<Position> liste2 = listMove(plateau, p, false, joueur);
//			if (liste2 != null && !liste2.isEmpty()) {
//				listePieces.add(p);
//			}
//		}
//		return listePieces;
//	}

}
