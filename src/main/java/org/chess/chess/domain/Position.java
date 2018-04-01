package org.chess.chess.domain;

import org.chess.chess.outils.Check;

import java.util.Objects;

@Deprecated
public class Position {

	private final int ligne;
	private final int colonne;

	public Position(int ligne, int colonne) {
		Check.checkLigneColonne(ligne, colonne);
		this.ligne = ligne;
		this.colonne = colonne;
	}

	public int getLigne() {
		return ligne;
	}

	public int getColonne() {
		return colonne;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Position)) return false;
		Position position = (Position) o;
		return ligne == position.ligne &&
				colonne == position.colonne;
	}

	@Override
	public int hashCode() {

		return Objects.hash(ligne, colonne);
	}

	@Override
	public String toString() {
		return "Position{" +
				"ligne=" + ligne +
				", colonne=" + colonne +
				'}';
	}
}
