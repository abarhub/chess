package org.chess.chess.domain;

public enum ColonneEnum {

	COLONNEA(1),
	COLONNEB(2),
	COLONNEC(3),
	COLONNED(4),
	COLONNEE(5),
	COLONNEF(6),
	COLONNEG(7),
	COLONNEH(8);

	private final int no;

	ColonneEnum(int no) {
		this.no = no;
	}

	public static ColonneEnum get(int no) {
		for (ColonneEnum colonne : ColonneEnum.values()) {
			if (colonne.getNo() == no) {
				return colonne;
			}
		}
		return null;
	}

	public int getNo() {
		return no;
	}

	public String getText() {
		return "" + (no - 1 + 'a');
	}
}
