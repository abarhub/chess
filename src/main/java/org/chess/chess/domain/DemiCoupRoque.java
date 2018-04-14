package org.chess.chess.domain;

public class DemiCoupRoque implements DemiCoup {

	private final Position src;
	private final Position dest;
	private final boolean echec;
	private final boolean echecEtMat;

	public DemiCoupRoque(Position src, Position dest,
	                     boolean echec, boolean echecEtMat) {
		this.src = src;
		this.dest = dest;
		this.echec = echec;
		this.echecEtMat = echecEtMat;
	}

	public Position getSrc() {
		return src;
	}

	public Position getDest() {
		return dest;
	}

	@Override
	public boolean isEchec() {
		return echec;
	}

	@Override
	public boolean isEchecEtMat() {
		return echecEtMat;
	}
}
