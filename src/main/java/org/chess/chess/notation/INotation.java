package org.chess.chess.notation;

import org.chess.chess.domain.Plateau;

public interface INotation {

	Plateau createPlateau(String str);

	String serialize(Plateau plateau);

}
