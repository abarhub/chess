package org.chess.chess.domain;

public enum Piece {

	ROI('R', 'K'),
	REINE('D', 'Q'),
	TOUR('T', 'R'),
	FOU('F', 'B'),
	CAVALIER('C', 'N'),
	PION('P', 'P');

	private final char nomCourt;
	private final char nomCourtAnglais;

	private Piece(char nomCourt, char nomCourtAnglais) {
		this.nomCourt = nomCourt;
		this.nomCourtAnglais = nomCourtAnglais;
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
}
