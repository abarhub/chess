package org.chess.chess.domain;

public enum Couleur {

	Blanc('B'), Noir('N');

	private final char nomCourt;

	Couleur(char nomCourt) {
		this.nomCourt = nomCourt;
	}
}
