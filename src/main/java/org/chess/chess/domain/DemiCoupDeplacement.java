package org.chess.chess.domain;

public class DemiCoupDeplacement implements DemiCoup {

	private final Piece piece;
	private final Position src;
	private final Position dest;

	public DemiCoupDeplacement(Piece piece, Position src, Position dest) {
		this.src = src;
		this.dest = dest;
		this.piece = piece;
	}

	public Piece getPiece() {
		return piece;
	}

	public Position getSrc() {
		return src;
	}

	public Position getDest() {
		return dest;
	}
}
