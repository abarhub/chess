package org.chess.chess.domain;

public class PieceCouleur {

	private final Piece piece;
	private final Couleur couleur;

	public PieceCouleur(Piece piece, Couleur couleur) {
		this.piece = piece;
		this.couleur = couleur;
	}

	public Piece getPiece() {
		return piece;
	}

	public Couleur getCouleur() {
		return couleur;
	}
}
