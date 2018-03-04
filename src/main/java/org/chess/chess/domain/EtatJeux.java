package org.chess.chess.domain;

import com.google.common.base.Verify;

public enum EtatJeux {

	MOUVEMENT_BLANC(false, Couleur.Blanc),
	MOUVEMENT_NOIR(false, Couleur.Noir),
	ECHECS_BLANC(false, Couleur.Blanc),
	ECHECS_NOIR(false, Couleur.Noir),
	MAT(true, Couleur.Blanc),
	ECHECS_ET_MAT_BLANC(true, Couleur.Blanc),
	ECHECS_ET_MAT_NOIR(true, Couleur.Noir);

	private final boolean finPartie;
	private final Couleur couleur;

	EtatJeux(boolean finPartie, Couleur couleur) {
		Verify.verifyNotNull(couleur);
		this.finPartie = finPartie;
		this.couleur = couleur;
	}

	public boolean isFinPartie() {
		return finPartie;
	}

	public Couleur getCouleur() {
		return couleur;
	}
}
