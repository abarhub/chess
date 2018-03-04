package org.chess.chess.evaluateur;

import org.chess.chess.domain.*;
import org.chess.chess.moteur.Moteur;

public class ShannonEval extends Evaluator {

	@Override
	public float evaluation(Moteur moteur, Plateau plateau, Couleur couleur) {
		float res;
		Couleur couleurInverse = moteur.couleurContraire(couleur);

		res = 200.0f * (nbPieces(plateau, couleur, Piece.ROI) - nbPieces(plateau, couleurInverse, Piece.ROI))
				+ 9 * (nbPieces(plateau, couleur, Piece.REINE) - nbPieces(plateau, couleurInverse, Piece.REINE))
				+ 5 * (nbPieces(plateau, couleur, Piece.TOUR) - nbPieces(plateau, couleurInverse, Piece.TOUR))
				+ 3 * (nbPieces(plateau, couleur, Piece.FOU) - nbPieces(plateau, couleurInverse, Piece.FOU))
				+ 3 * (nbPieces(plateau, couleur, Piece.CAVALIER) - nbPieces(plateau, couleurInverse, Piece.CAVALIER))
				+ 1 * (nbPieces(plateau, couleur, Piece.PION) - nbPieces(plateau, couleurInverse, Piece.PION))
				- 0.5f * (doubled(plateau, couleur) - doubled(plateau, couleurInverse))
				- 0.5f * (blocked(plateau, couleur) - blocked(plateau, couleurInverse))
				- 0.5f * (isolated(plateau, couleur) - isolated(plateau, couleurInverse))
				+ 0.1f * (moves(moteur, plateau, couleur) - moves(moteur, plateau, couleurInverse));

		return res;
	}


}
