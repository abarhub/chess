package org.chess.chess.notation;

import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Plateau;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class NotationPGN implements INotation {


	@Override
	public Partie createPlateau(String str) {
		throw new NotImplementedException();
		//return null;
	}

	@Override
	public String serialize(Partie plateau) {
		throw new NotImplementedException();
		//return null;
	}
}
