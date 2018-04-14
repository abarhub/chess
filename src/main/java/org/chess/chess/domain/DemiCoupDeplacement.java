package org.chess.chess.domain;

public class DemiCoupDeplacement implements DemiCoup {

	private final Piece piece;
	private final Position src;
	private final Position dest;
	private final boolean mangePiece;
	private final Piece promotion;
	private final boolean echec;
	private final boolean echecEtMat;

	public DemiCoupDeplacement(Piece piece, Position src, Position dest,
	                           boolean mangePiece, Piece promotion,
	                           boolean echec, boolean echecEtMat) {
		this.src = src;
		this.dest = dest;
		this.piece = piece;
		this.mangePiece = mangePiece;
		this.promotion = promotion;
		this.echec = echec;
		this.echecEtMat = echecEtMat;
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

	public boolean isMangePiece() {
		return mangePiece;
	}

	public Piece getPromotion() {
		return promotion;
	}

	public boolean isEchec() {
		return echec;
	}

	public boolean isEchecEtMat() {
		return echecEtMat;
	}

	@Override
	public String toString() {
		return "DemiCoupDeplacement{" +
				"piece=" + piece +
				", src=" + src +
				", dest=" + dest +
				", mangePiece=" + mangePiece +
				", promotion=" + promotion +
				", echec=" + echec +
				", echecEtMat=" + echecEtMat +
				'}';
	}
}
