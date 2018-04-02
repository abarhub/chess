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
		for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
			int nbPions = 0;
			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				PieceCouleur p = plateau.getCase(new Position(rangee, colonne));
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
		for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				PieceCouleur p = plateau.getCase(new Position(rangee, colonne));
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						Optional<Position> pos = PositionTools.getPosition(rangee, -1, colonne, 0);
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
		for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				PieceCouleur p = plateau.getCase(new Position(rangee, colonne));
				if (p != null && p.getCouleur() == couleur) {
					if (p.getPiece() == Piece.PION) {
						boolean trouve = false;
						for (int i = -1; i <= 1; i++) {
							Optional<Position> pos = PositionTools.getPosition(rangee, i, colonne, -1);
							if (pos.isPresent()) {
								PieceCouleur p2 = plateau.getCase(pos.get());
								if (p2 != null && p2.getCouleur() == couleur && p2.getPiece() == Piece.PION) {
									trouve = true;
									break;
								}
							}
							Optional<Position> pos2 = PositionTools.getPosition(rangee, i, colonne, 1);
							if (pos2.isPresent()) {
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
