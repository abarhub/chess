package org.chess.chess.domain;

public class Mouvement {

	private final Position2 position;
	private final boolean attaque;

	public Mouvement(Position2 position, boolean attaque) {
		this.position = position;
		this.attaque = attaque;
	}

	public Position2 getPosition() {
		return position;
	}

	public boolean isAttaque() {
		return attaque;
	}

	@Override
	public String toString() {
		return "Mouvement{" +
				"position=" + position +
				", attaque=" + attaque +
				'}';
	}
}
