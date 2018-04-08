package org.chess.chess.domain;

import java.util.Objects;

public class Mouvement {

	private final Position position;
	private final boolean attaque;

	public Mouvement(Position position, boolean attaque) {
		this.position = position;
		this.attaque = attaque;
	}

	public Position getPosition() {
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
