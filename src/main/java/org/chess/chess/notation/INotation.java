package org.chess.chess.notation;

import org.chess.chess.domain.Partie;

public interface INotation {

	Partie createPlateau(String str);

	String serialize(Partie partie);

}
