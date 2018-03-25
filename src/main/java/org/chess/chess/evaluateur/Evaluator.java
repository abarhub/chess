package org.chess.chess.evaluateur;

import org.chess.chess.domain.*;
import org.chess.chess.moteur.Moteur;
import org.chess.chess.outils.Check;

import java.util.List;

public abstract class Evaluator {

	public abstract float evaluation(Moteur moteur, Plateau plateau, Couleur couleur);

	protected int nbPieces(Plateau plateau, Couleur couleur, Piece piece) {
		return (int) plateau.getStream()
				.filter(x -> x.getCouleur() == couleur && x.getPiece() == piece)
				.count();
	}

	protected int doubled(Plateau plateau, Couleur couleur) {
		int res = 0;
		for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
			int nbPions = 0;
			for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
				PieceCouleur p = plateau.getCase(new Position(ligne, colonne));
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

	protected int blocked(Plateau plateau, Couleur couleur) {
		int res = 0;
		for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
				PieceCouleur p = plateau.getCase(new Position(ligne, colonne));
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						if (Check.isPositionValide(ligne - 1, colonne)) {
							PieceCouleur p2 = plateau.getCase(new Position(ligne - 1, colonne));
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

	protected int isolated(Plateau plateau, Couleur couleur) {
		int res = 0;
		for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
				PieceCouleur p = plateau.getCase(new Position(ligne, colonne));
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						boolean trouve = false;
						for (int i = -1; i <= 1; i++) {
							if (Check.isPositionValide(ligne + i, colonne - 1)) {
								PieceCouleur p2 = plateau.getCase(new Position(ligne + i, colonne - 1));
								if (p2 != null && p2.getCouleur() == couleur && p2.getPiece() == Piece.PION) {
									trouve = true;
									break;
								}
							}
							if (Check.isPositionValide(ligne + i, colonne + 1)) {
								PieceCouleur p2 = plateau.getCase(new Position(ligne + i, colonne + 1));
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

	protected int moves(Moteur moteur, Plateau plateau, Couleur couleur) {
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
