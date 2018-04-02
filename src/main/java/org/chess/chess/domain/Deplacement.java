package org.chess.chess.domain;

public class Deplacement {

	private final Position positionSrc;
	private final Position positionDest;

	public Deplacement(Position positionSrc, Position positionDest) {
		this.positionSrc = positionSrc;
		this.positionDest = positionDest;
	}

	public Position getPositionSrc() {
		return positionSrc;
	}

	public Position getPositionDest() {
		return positionDest;
	}
}
