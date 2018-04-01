package org.chess.chess.domain;

public class Deplacement {

	private final Position2 positionSrc;
	private final Position2 positionDest;

	public Deplacement(Position2 positionSrc, Position2 positionDest) {
		this.positionSrc = positionSrc;
		this.positionDest = positionDest;
	}

	public Position2 getPositionSrc() {
		return positionSrc;
	}

	public Position2 getPositionDest() {
		return positionDest;
	}
}
