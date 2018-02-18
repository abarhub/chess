package org.chess.chess.domain;

public enum Piece {

	ROI('R', 'K'),
	REINE('D', 'D'),
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

	public char getNomCourt() {
		return nomCourt;
	}

	public char getNomCourtAnglais() {
		return nomCourtAnglais;
	}
}
