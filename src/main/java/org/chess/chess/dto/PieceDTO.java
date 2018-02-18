package org.chess.chess.dto;

public class PieceDTO {

	private int ligne;
	private int colonne;
	private char piece;
	private boolean couleurBlanc;

	public int getLigne() {
		return ligne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	public int getColonne() {
		return colonne;
	}

	public void setColonne(int colonne) {
		this.colonne = colonne;
	}

	public char getPiece() {
		return piece;
	}

	public void setPiece(char piece) {
		this.piece = piece;
	}

	public boolean isCouleurBlanc() {
		return couleurBlanc;
	}

	public void setCouleurBlanc(boolean couleurBlanc) {
		this.couleurBlanc = couleurBlanc;
	}
}
