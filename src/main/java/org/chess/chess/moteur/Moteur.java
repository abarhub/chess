package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.outils.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class Moteur {

	public static final Logger LOGGER = LoggerFactory.getLogger(Moteur.class);

	private Plateau plateau;

	private Couleur joueurCourant;

	private Random random = new Random(System.currentTimeMillis());

	@PostConstruct
	public void initialise() {
		LOGGER.info("initialisation du moteur ...");

		plateau = new Plateau();
		plateau.initialise();
		plateau.afficheConsole();

		joueurCourant = Couleur.Blanc;

		LOGGER.info("initialisation du moteur OK");
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public void nextMove() {

		List<Position> listePieces;
		listePieces = getMovablePieces(joueurCourant);

		if (listePieces != null && !listePieces.isEmpty()) {
			int no = random.nextInt(listePieces.size());

			Position p = listePieces.get(no);

			List<Position> liste = listMove(p, false);

			Verify.verifyNotNull(liste);
			Verify.verify(!liste.isEmpty());

			Position deplacement;
			if (liste.size() == 1) {
				deplacement = liste.get(0);
			} else {
				no = random.nextInt(liste.size());
				deplacement = liste.get(no);
			}

			//PieceCouleur p2 = plateau.getCase(p.getLigne(), p.getColonne());
			plateau.move(p, deplacement);

			joueurCourant = couleurContraire(joueurCourant);

		} else {
			throw new IllegalStateException("Deplacement impossible !");
		}
	}

	private Couleur couleurContraire(Couleur couleur) {
		if (couleur == Couleur.Blanc) {
			return Couleur.Noir;
		} else {
			return Couleur.Blanc;
		}
	}

	private List<Position> getMovablePieces(Couleur joueur) {
		List<Position> listePieces;
		List<Position> liste = listePieces(joueur);
		listePieces = new ArrayList<>();
		for (Position p : liste) {
			List<Position> liste2 = listMove(p, false);
			if (liste2 != null && !liste2.isEmpty()) {
				listePieces.add(p);
			}
		}
		return listePieces;
	}

	private List<Position> listePieces(Couleur couleur) {
		Verify.verifyNotNull(couleur);
		List<Position> liste = new ArrayList<>();
		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				PieceCouleur piece = plateau.getCase(i, j);
				if (piece != null && piece.getCouleur() == couleur) {
					liste.add(new Position(i, j));
				}
			}
		}
		return liste;
	}

	private boolean caseAttaque(Couleur couleur, int ligne, int colonne) {
		List<Position> pieces = listePieces(couleurContraire(couleur));

		if (pieces != null) {
			Position positionRecherche = new Position(ligne, colonne);
			for (Position p : pieces) {
				if (p.getLigne() != ligne && p.getColonne() != colonne) {
					List<Position> liste = listMove(p, true);

					if (liste != null) {
						if (liste.contains(positionRecherche)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private List<Position> listMove(Position position, boolean tousMouvementRois) {
		return listMove(position.getLigne(), position.getColonne(), tousMouvementRois);
	}

	private List<Position> listMove(int ligne, int colonne, boolean tousMouvementRois) {
		Check.checkLigneColonne(ligne, colonne);
		List<Position> liste = new ArrayList<>();

		PieceCouleur piece = plateau.getCase(ligne, colonne);
		if (piece != null) {
			if (piece.getPiece() == Piece.PION) {
				int decalage, decalage2 = 0;
				if (piece.getCouleur() == Couleur.Blanc) {
					decalage = -1;
					if (ligne == 6) {
						decalage2 = -2;
					}
				} else {
					decalage = 1;
					if (ligne == 1) {
						decalage2 = 2;
					}
				}
				ajoutePositionPions(liste, ligne + decalage, colonne,
						piece.getCouleur(), false);
				ajoutePositionPions(liste, ligne + decalage, colonne - 1,
						piece.getCouleur(), true);
				ajoutePositionPions(liste, ligne + decalage, colonne + 1,
						piece.getCouleur(), true);
				if (decalage2 != 0) {
					ajoutePositionPions(liste, ligne + decalage2, colonne + 1,
							piece.getCouleur(), false);
				}
			} else if (piece.getPiece() == Piece.CAVALIER) {
				ajoutePositionPiece(liste, ligne - 2, colonne - 1,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne - 2, colonne + 1,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne + 1, colonne - 2,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne - 1, colonne - 2,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne + 1, colonne + 2,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne - 1, colonne + 2,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne + 2, colonne - 1,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne + 2, colonne + 1,
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
					ajouteDecalage(liste, ligne, colonne, decalageLigne, decalageColonne, piece.getCouleur());
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
					ajouteDecalage(liste, ligne, colonne, decalageLigne, decalageColonne, piece.getCouleur());
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
					ajouteDecalage(liste, ligne, colonne, decalageLigne, decalageColonne,
							piece.getCouleur());
				}
			} else if (piece.getPiece() == Piece.ROI) {
				for (int ligne2 = -1; ligne2 <= 1; ligne2++) {
					for (int colonne2 = -1; colonne2 <= 1; colonne2++) {
						if (ligne2 != colonne2) {
							if (Check.isPositionValide(ligne2, colonne2)) {
								if (tousMouvementRois) {
									ajoutePositionRois(liste, ligne + ligne2, colonne + colonne2,
											joueurCourant);
								} else if (!caseAttaque(piece.getCouleur(), ligne2, colonne2)) {
									ajoutePositionRois(liste, ligne + ligne2, colonne + colonne2,
											joueurCourant);
								}
							}
						}
					}
				}
			}
		}

		return liste;
	}

	private void ajouteDecalage(List<Position> liste, int ligne, int colonne,
	                            int decalageLigne, int decalageColonne, Couleur couleur) {
		for (int i = 1; i <= 8; i++) {
			boolean res = ajoutePositionPiece(liste, ligne + decalageLigne * i,
					colonne + decalageColonne * i,
					couleur);
			if (!res) {
				break;
			}
		}
	}

	private void ajoutePositionRois(List<Position> liste, int ligne, int colonne, Couleur couleur) {
		ajoutePosition(liste, ligne, colonne, couleur, true, false, true);
	}

	private void ajoutePositionPions(List<Position> liste, int ligne, int colonne,
	                                 Couleur couleur, boolean doitManger) {
		ajoutePosition(liste, ligne, colonne, couleur, false, doitManger, false);
	}

	private boolean ajoutePositionPiece(List<Position> liste, int ligne, int colonne,
	                                    Couleur couleur) {
		return ajoutePosition(liste, ligne, colonne, couleur, true, false, false);
	}

	private boolean ajoutePosition(List<Position> liste, int ligne, int colonne,
	                               Couleur couleur, boolean peutManger, boolean doitManger,
	                               boolean deplacementNonAttaquable) {
		if (Check.isPositionValide(ligne, colonne)) {
//			if (deplacementNonAttaquable) {
//				if (caseAttaque(couleurContraire(couleur), ligne, colonne)) {
//					return false;
//				}
//			}
			if (doitManger) {
				PieceCouleur piece = plateau.getCase(ligne, colonne);
				if (piece != null && piece.getCouleur() != couleur) {
					liste.add(new Position(ligne, colonne));
					return true;
				}
			} else {
				PieceCouleur piece = plateau.getCase(ligne, colonne);
				if (piece == null) {
					liste.add(new Position(ligne, colonne));
					return true;
				} else if (peutManger && piece.getCouleur() != couleur) {
					liste.add(new Position(ligne, colonne));
					return false;
				}
			}
		}
		return false;
	}
}
