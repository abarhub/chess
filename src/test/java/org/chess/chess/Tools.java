package org.chess.chess;

import org.chess.chess.domain.Plateau;
import org.chess.chess.notation.NotationFEN;

public class Tools {

	public static Plateau createFromFen(String str) {
		NotationFEN notationFEN = new NotationFEN();
		return notationFEN.createPlateau(str);
	}
}
