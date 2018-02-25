package org.chess.chess.domain;

public enum Couleur {

	Blanc('B'), Noir('N');

	private final char nomCourt;

	Couleur(char nomCourt) {
		this.nomCourt = nomCourt;
	}

	public static Couleur getValue(char c) {
		for (Couleur couleur : Couleur.values()) {
			if (couleur.nomCourt == c) {
				return couleur;
			}
		}
		return null;
	}

	public char getNomCourt() {
		return nomCourt;
	}
}
