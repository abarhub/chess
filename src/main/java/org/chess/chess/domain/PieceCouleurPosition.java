package org.chess.chess.domain;

import java.util.Objects;

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PieceCouleurPosition)) return false;
		if (!super.equals(o)) return false;
		PieceCouleurPosition that = (PieceCouleurPosition) o;
		return Objects.equals(position, that.position);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), position);
	}

	@Override
	public String toString() {
		return super.toString() + " " + position;
//		return "PieceCouleurPosition{" +
//				"position=" + position +
//				",piece=" + getPiece() +
//				",couleur=" + getCouleur() +
//				'}';
	}
}
