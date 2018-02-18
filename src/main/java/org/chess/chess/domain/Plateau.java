package org.chess.chess.domain;

import com.google.common.base.Verify;

public class Plateau {

	public static final int NB_LIGNES = 8;
	public static final int NB_COLONNES = 8;

	private PieceCouleur[][] tableau;

	public Plateau() {

	}

	public void initialise() {
		tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];

		lignePieces(0, Couleur.Noir);
		lignePions(1, Couleur.Noir);


		lignePions(6, Couleur.Blanc);
		lignePieces(7, Couleur.Blanc);
	}

	private void lignePieces(int ligne, Couleur couleur) {
		setCase(ligne, 0, new PieceCouleur(Piece.TOUR, couleur));
		setCase(ligne, 1, new PieceCouleur(Piece.CAVALIER, couleur));
		setCase(ligne, 2, new PieceCouleur(Piece.FOU, couleur));
		setCase(ligne, 3, new PieceCouleur(Piece.REINE, couleur));
		setCase(ligne, 4, new PieceCouleur(Piece.ROI, couleur));
		setCase(ligne, 5, new PieceCouleur(Piece.FOU, couleur));
		setCase(ligne, 6, new PieceCouleur(Piece.CAVALIER, couleur));
		setCase(ligne, 7, new PieceCouleur(Piece.TOUR, couleur));
	}

	private void lignePions(int ligne, Couleur couleur) {
		for (int i = 0; i < NB_COLONNES; i++) {
			setCase(ligne, i, new PieceCouleur(Piece.PION, couleur));
		}
	}

	private void setCase(int ligne, int colonne, PieceCouleur pieceCouleur) {
		Verify.verify(ligne >= 0);
		Verify.verify(ligne < NB_LIGNES);
		Verify.verify(colonne >= 0);
		Verify.verify(colonne < NB_COLONNES);
		tableau[ligne][colonne] = pieceCouleur;
	}

	public PieceCouleur getCase(int ligne, int colonne) {
		Verify.verify(ligne >= 0);
		Verify.verify(ligne < NB_LIGNES);
		Verify.verify(colonne >= 0);
		Verify.verify(colonne < NB_COLONNES);
		return tableau[ligne][colonne];
	}

	public void afficheConsole() {
		for (int ligne = 0; ligne < NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				PieceCouleur p = getCase(ligne, colonne);
				if (p == null) {
					System.out.print(' ');
				} else {
					System.out.print(p.getPiece().getNomCourt());
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
