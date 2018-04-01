package org.chess.chess.domain;

public class PieceCouleurPosition extends PieceCouleur {

	private final Position2 position;

	public PieceCouleurPosition(Piece piece, Couleur couleur, Position2 position) {
		super(piece, couleur);
		this.position = position;
	}

	public Position2 getPosition() {
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
