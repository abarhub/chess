package org.chess.chess.dto;

import org.chess.chess.domain.Position;

public class PositionDTO {

	private int ligne;
	private int colonne;

	public PositionDTO() {
		// constructeur vide
	}

	public PositionDTO(int ligne, int colonne) {
		this.ligne = ligne;
		this.colonne = colonne;
	}

	public PositionDTO(Position position) {
		this(position.getLigne(), position.getColonne());
	}

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
}
