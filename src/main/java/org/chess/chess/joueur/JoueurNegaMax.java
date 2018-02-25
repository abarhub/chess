package org.chess.chess.joueur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.moteur.Moteur;
import org.chess.chess.outils.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JoueurNegaMax extends Joueur {

	public static final Logger LOGGER = LoggerFactory.getLogger(JoueurNegaMax.class);

	private final int profondeur;

	public JoueurNegaMax(Couleur couleur, int profondeur) {
		super(couleur);
		this.profondeur = profondeur;
	}

	@Override
	public void nextMove(Moteur moteur) {
		LOGGER.info("nextMove: {}", getCouleur());

		Deplacement deplacement = calculNegaMax(moteur, getCouleur(), profondeur);

		Verify.verifyNotNull(deplacement);

		moteur.getPlateau().move(deplacement.getPositionSrc(), deplacement.getPositionDest());
	}

	private Deplacement calculNegaMax(Moteur moteur, Couleur couleur, int profondeur) {
		Plateau plateau = moteur.getPlateau();

		Deplacement meilleurDeplacement = null;
		float meilleurEval = 0.0f;

		List<Position> liste = moteur.listePieces(couleur);
		if (liste != null) {
			for (Position p : liste) {
				List<Position> liste2 = moteur.listMove(p, false);
				if (liste2 != null && !liste2.isEmpty()) {
					for (Position p2 : liste2) {
						Plateau plateau2 = new Plateau(plateau);
						plateau2.move(p, p2);
						float eval = evaluation(moteur, plateau2, couleur);

						if (meilleurDeplacement == null
								|| meilleurEval < eval) {
							meilleurDeplacement = new Deplacement(p, p2);
							meilleurEval = eval;
						}
					}
				}
			}
		}

		return meilleurDeplacement;
	}

	private float evaluation(Moteur moteur, Plateau plateau, Couleur couleur) {
		float res;
		Couleur couleurInverse = moteur.couleurContraire(couleur);

		res = 200.0f * (nbPieces(plateau, couleur, Piece.ROI) - nbPieces(plateau, couleurInverse, Piece.ROI))
				+ 9 * (nbPieces(plateau, couleur, Piece.REINE) - nbPieces(plateau, couleurInverse, Piece.REINE))
				+ 5 * (nbPieces(plateau, couleur, Piece.TOUR) - nbPieces(plateau, couleurInverse, Piece.TOUR))
				+ 3 * (nbPieces(plateau, couleur, Piece.FOU) - nbPieces(plateau, couleurInverse, Piece.FOU))
				+ 3 * (nbPieces(plateau, couleur, Piece.CAVALIER) - nbPieces(plateau, couleurInverse, Piece.CAVALIER))
				+ 1 * (nbPieces(plateau, couleur, Piece.PION) - nbPieces(plateau, couleurInverse, Piece.PION))
				- 0.5f * (doubled(plateau, couleur) - doubled(plateau, couleurInverse))
				- 0.5f * (bloked(plateau, couleur) - bloked(plateau, couleurInverse))
				- 0.5f * (isolated(plateau, couleur) - isolated(plateau, couleurInverse))
				+ 0.1f * (moves(moteur, plateau, couleur) - moves(moteur, plateau, couleurInverse));

		return res;
	}

	private int nbPieces(Plateau plateau, Couleur couleur, Piece piece) {
		return (int) plateau.getStream()
				.filter(x -> x.getCouleur() == couleur && x.getPiece() == piece)
				.count();
	}


	private int doubled(Plateau plateau, Couleur couleur) {
		int res = 0;
		for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
			int nbPions = 0;
			for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
				PieceCouleur p = plateau.getCase(ligne, colonne);
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						nbPions++;
					}
				}
			}
			if (nbPions > 1) {
				res++;
			}
		}
		return res;
	}

	private int bloked(Plateau plateau, Couleur couleur) {
		int res = 0;
		for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
				PieceCouleur p = plateau.getCase(ligne, colonne);
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						if (Check.isPositionValide(ligne - 1, colonne)) {
							PieceCouleur p2 = plateau.getCase(ligne - 1, colonne);
							if (p2 != null) {
								res++;
							}
						}
					}
				}
			}
		}
		return res;
	}

	private int isolated(Plateau plateau, Couleur couleur) {
		int res = 0;
		for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
				PieceCouleur p = plateau.getCase(ligne, colonne);
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						boolean trouve = false;
						for (int i = -1; i <= 1; i++) {
							if (Check.isPositionValide(ligne + i, colonne - 1)) {
								PieceCouleur p2 = plateau.getCase(ligne + i, colonne - 1);
								if (p2 != null && p2.getCouleur() == couleur && p2.getPiece() == Piece.PION) {
									trouve = true;
									break;
								}
							}
							if (Check.isPositionValide(ligne + i, colonne + 1)) {
								PieceCouleur p2 = plateau.getCase(ligne + i, colonne + 1);
								if (p2 != null && p2.getCouleur() == couleur && p2.getPiece() == Piece.PION) {
									trouve = true;
									break;
								}
							}
						}
						if (!trouve) {
							res++;
						}
					}
				}
			}
		}
		return res;
	}

	private int moves(Moteur moteur, Plateau plateau, Couleur couleur) {
		int res = 0;

		List<Position> liste = moteur.listePieces(couleur);
		if (liste != null) {
			for (Position p : liste) {
				List<Position> liste2 = moteur.listMove(p, false);
				if (liste2 != null) {
					res += liste2.size();
				}
			}
		}

		return res;
	}

}
