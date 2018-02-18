package org.chess.chess.domain;

public class Position {

	private final int ligne;
	private final int colonne;

	public Position(int ligne, int colonne) {
		this.ligne = ligne;
		this.colonne = colonne;
	}

	public int getLigne() {
		return ligne;
	}

	public int getColonne() {
		return colonne;
	}
}
