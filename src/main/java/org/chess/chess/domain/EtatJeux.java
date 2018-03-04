package org.chess.chess.domain;

import com.google.common.base.Verify;

public enum EtatJeux {

	MOUVEMENT_BLANC(false, Couleur.Blanc), // prochain déplacement pour les blancs
	MOUVEMENT_NOIR(false, Couleur.Noir), // prochain déplacement pour les noirs
	ECHECS_BLANC(false, Couleur.Blanc),// les blancs sont en echecs
	ECHECS_NOIR(false, Couleur.Noir),// les noirs sont en echecs
	MAT(true, Couleur.Blanc),// mat
	ECHECS_ET_MAT_BLANC(true, Couleur.Blanc),// les blancs sont en echecs et mat
	ECHECS_ET_MAT_NOIR(true, Couleur.Noir);// les noirs sont en echecs et mat

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
