package org.chess.chess.domain;

public enum Piece {

	ROI('R', 'K', 20),
	REINE('D', 'Q', 9),
	TOUR('T', 'R', 5),
	FOU('F', 'B', 3),
	CAVALIER('C', 'N', 3),
	PION('P', 'P', 1);

	private final char nomCourt;
	private final char nomCourtAnglais;
	private final int valeur;

	private Piece(char nomCourt, char nomCourtAnglais, int valeur) {
		this.nomCourt = nomCourt;
		this.nomCourtAnglais = nomCourtAnglais;
		this.valeur = valeur;
	}

	public static Piece getValue(char c) {
		for (Piece piece : Piece.values()) {
			if (piece.nomCourt == c) {
				return piece;
			}
		}
		return null;
	}

	public char getNomCourt() {
		return nomCourt;
	}

	public char getNomCourtAnglais() {
		return nomCourtAnglais;
	}

	public int getValeur() {
		return valeur;
	}
}
