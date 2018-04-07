package org.chess.chess.domain;

import com.google.common.base.Verify;
import org.chess.chess.joueur.Joueur;

import java.util.ArrayList;
import java.util.List;

public class Partie {

	private final Plateau plateau;
	private final Joueur joueurBlanc;
	private final Joueur joueurNoir;
	private final List<DemiCoup> listeCoupsBlancs;
	private final List<DemiCoup> listeCoupsNoirs;
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
		listeCoupsBlancs = new ArrayList<>();
		listeCoupsNoirs = new ArrayList<>();
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
		pieceSource = plateau.getCase(src);

		Verify.verifyNotNull(pieceSource, "la piece source n'existe pas");
		Verify.verify(pieceSource.getCouleur() == joueurCourant,
				"la piece source n'est pas de la couleur du joueur qui doit jouer");

		if (joueurCourant == Couleur.Blanc) {
			Verify.verify(pieceSource.getCouleur() == joueurCourant);
		}

		PieceCouleur pieceDestination = plateau.getCase(dest);

		if (pieceDestination != null) {
			Verify.verify(pieceDestination.getCouleur() != joueurCourant);
			Verify.verify(pieceDestination.getPiece() != Piece.ROI);
		}

		plateau.move(src, dest);

		DemiCoupDeplacement demiCoupDeplacement = new DemiCoupDeplacement(pieceSource.getPiece(), src, dest);

		if (joueurCourant == Couleur.Blanc) {
			listeCoupsBlancs.add(demiCoupDeplacement);
			joueurCourant = Couleur.Noir;
		} else {
			listeCoupsNoirs.add(demiCoupDeplacement);
			joueurCourant = Couleur.Blanc;
		}
	}
}
