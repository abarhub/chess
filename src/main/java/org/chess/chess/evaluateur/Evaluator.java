package org.chess.chess.evaluateur;

import org.chess.chess.domain.*;
import org.chess.chess.moteur.Moteur;
import org.chess.chess.outils.IteratorPlateau;
import org.chess.chess.outils.PositionTools;

import java.util.List;
import java.util.Optional;

public abstract class Evaluator {

	public abstract float evaluation(Moteur moteur, Plateau plateau, Couleur couleur);

	protected int nbPieces(Plateau plateau, Couleur couleur, Piece piece) {
		return (int) plateau.getStream()
				.filter(x -> x.getCouleur() == couleur && x.getPiece() == piece)
				.count();
	}

	protected int doubled(Plateau plateau, Couleur couleur) {
		int res = 0;
		//for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
		for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
			int nbPions = 0;
			//for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				PieceCouleur p = plateau.getCase(new Position2(rangee, colonne));
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
		//for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
		for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
			//for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				PieceCouleur p = plateau.getCase(new Position2(rangee, colonne));
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						Optional<Position2> pos = PositionTools.getPosition(rangee, -1, colonne, 0);
						//if (Check.isPositionValide(ligne - 1, colonne)) {
						if (pos.isPresent()) {
							PieceCouleur p2 = plateau.getCase(pos.get());
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
		//for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
		for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
			//for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				PieceCouleur p = plateau.getCase(new Position2(rangee, colonne));
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						boolean trouve = false;
						for (int i = -1; i <= 1; i++) {
							Optional<Position2> pos = PositionTools.getPosition(rangee, i, colonne, -1);
							//if (Check.isPositionValide(ligne + i, colonne - 1)) {
							if (pos.isPresent()) {
								//PieceCouleur p2 = plateau.getCase(PositionTools.getPosition(ligne + i, colonne - 1));
								PieceCouleur p2 = plateau.getCase(pos.get());
								if (p2 != null && p2.getCouleur() == couleur && p2.getPiece() == Piece.PION) {
									trouve = true;
									break;
								}
							}
							Optional<Position2> pos2 = PositionTools.getPosition(rangee, i, colonne, 1);
							//if (Check.isPositionValide(ligne + i, colonne + 1)) {
							if (pos2.isPresent()) {
								//PieceCouleur p2 = plateau.getCase(PositionTools.getPosition(ligne + i, colonne + 1));
								PieceCouleur p2 = plateau.getCase(pos2.get());
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

		List<Position2> liste = moteur.listePieces(couleur);
		if (liste != null) {
			for (Position2 p : liste) {
				List<Position2> liste2 = moteur.listMove(p, false);
				if (liste2 != null) {
					res += liste2.size();
				}
			}
		}

		return res;
	}
}
