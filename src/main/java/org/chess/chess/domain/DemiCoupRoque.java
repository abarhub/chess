package org.chess.chess.domain;

public class DemiCoupRoque implements DemiCoup {

	private final Position src;
	private final Position dest;

	public DemiCoupRoque(Position src, Position dest) {
		this.src = src;
		this.dest = dest;
	}

	public Position getSrc() {
		return src;
	}

	public Position getDest() {
		return dest;
	}
}
