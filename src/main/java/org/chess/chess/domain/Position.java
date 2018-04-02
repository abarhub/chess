package org.chess.chess.domain;

import com.google.common.base.Verify;

import java.util.Objects;

public class Position {

	private final RangeeEnum rangee;
	private final ColonneEnum colonne;

	public Position(RangeeEnum rangee, ColonneEnum colonne) {
		Verify.verifyNotNull(rangee);
		Verify.verifyNotNull(colonne);
		this.rangee = rangee;
		this.colonne = colonne;
	}

	public RangeeEnum getRangee() {
		return rangee;
	}

	public ColonneEnum getColonne() {
		return colonne;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Position)) return false;
		Position position2 = (Position) o;
		return rangee == position2.rangee &&
				colonne == position2.colonne;
	}

	@Override
	public int hashCode() {

		return Objects.hash(rangee, colonne);
	}

	@Override
	public String toString() {
		return rangee.getText() + colonne;
	}
}
