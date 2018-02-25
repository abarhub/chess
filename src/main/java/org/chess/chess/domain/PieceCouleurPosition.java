package org.chess.chess.domain;

public class PieceCouleurPosition extends PieceCouleur {

	private final Position position;

	public PieceCouleurPosition(Piece piece, Couleur couleur, Position position) {
		super(piece, couleur);
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return "PieceCouleurPosition{" +
				"position=" + position +
				",piece=" + getPiece() +
				",couleur=" + getCouleur() +
				'}';
	}
}
