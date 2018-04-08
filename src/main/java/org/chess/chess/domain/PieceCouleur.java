package org.chess.chess.domain;

import com.google.common.base.Verify;

import java.util.Objects;

public class PieceCouleur {

	private final Piece piece;
	private final Couleur couleur;

	public PieceCouleur(Piece piece, Couleur couleur) {
		Verify.verifyNotNull(piece);
		Verify.verifyNotNull(couleur);
		this.piece = piece;
		this.couleur = couleur;
	}

	public Piece getPiece() {
		return piece;
	}

	public Couleur getCouleur() {
		return couleur;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PieceCouleur)) return false;
		PieceCouleur that = (PieceCouleur) o;
		return piece == that.piece &&
				couleur == that.couleur;
	}

	@Override
	public int hashCode() {

		return Objects.hash(piece, couleur);
	}

	@Override
	public String toString() {
		if (couleur == Couleur.Noir) {
			return "" + Character.toLowerCase(piece.getNomCourt());
		} else {
			return "" + Character.toUpperCase(piece.getNomCourt());
		}
//		return "PieceCouleur{" +
//				"piece=" + piece +
//				", couleur=" + couleur +
//				'}';
	}
}
