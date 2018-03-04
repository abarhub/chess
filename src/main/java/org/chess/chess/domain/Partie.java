package org.chess.chess.domain;

import com.google.common.base.Verify;
import org.chess.chess.joueur.Joueur;

public class Partie {

	private final Plateau plateau;
	private final Joueur joueurBlanc;
	private final Joueur joueurNoir;
	private Couleur joueurCourant;

	public Partie(Plateau plateau, Joueur joueurBlanc, Joueur joueurNoir, Couleur joueurCourant) {
		Verify.verifyNotNull(plateau);
		Verify.verifyNotNull(joueurBlanc);
		Verify.verifyNotNull(joueurNoir);
		Verify.verifyNotNull(joueurCourant);
		this.plateau = plateau;
		this.joueurBlanc = joueurBlanc;
		this.joueurNoir = joueurNoir;
		this.joueurCourant = joueurCourant;
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public Joueur getJoueurBlanc() {
		return joueurBlanc;
	}

	public Joueur getJoueurNoir() {
		return joueurNoir;
	}

	public Couleur getJoueurCourant() {
		return joueurCourant;
	}

	public void setMove(Position src, Position dest) {
		Verify.verifyNotNull(src);
		Verify.verifyNotNull(dest);

		PieceCouleur pieceSource;
		pieceSource = plateau.getCase(src.getLigne(), src.getColonne());

		Verify.verifyNotNull(pieceSource, "la piece source n'existe pas");
		Verify.verify(pieceSource.getCouleur() == joueurCourant,
				"la piece source n'est pas de la couleur du joueur qui doit jouer");

		if (joueurCourant == Couleur.Blanc) {
			Verify.verify(pieceSource.getCouleur() == joueurCourant);
		}

		PieceCouleur pieceDestination = plateau.getCase(dest.getLigne(), dest.getColonne());

		if (pieceDestination != null) {
			Verify.verify(pieceDestination.getCouleur() != joueurCourant);
			Verify.verify(pieceDestination.getPiece() != Piece.ROI);
		}

		plateau.move(src, dest);

		if (joueurCourant == Couleur.Blanc) {
			joueurCourant = Couleur.Noir;
		} else {
			joueurCourant = Couleur.Blanc;
		}
	}
}
