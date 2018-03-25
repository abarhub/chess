package org.chess.chess.notation;

import org.chess.chess.domain.NotationEnum;
import org.chess.chess.domain.Plateau;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotationService {

	@Autowired
	private NotationFEN notationFEN;

	@Autowired
	private NotationCustom notationCustom;

	public NotationFEN getNotationFEN() {
		return notationFEN;
	}

	public void setNotationFEN(NotationFEN notationFEN) {
		this.notationFEN = notationFEN;
	}

	public NotationCustom getNotationCustom() {
		return notationCustom;
	}

	public void setNotationCustom(NotationCustom notationCustom) {
		this.notationCustom = notationCustom;
	}

	public Plateau createFromString(String str, NotationEnum notationEnum) {
		if (notationEnum == NotationEnum.FEN) {
			return notationFEN.createPlateau(str);
		} else if (notationEnum == NotationEnum.CUSTOM) {
			return notationCustom.createPlateau(str);
		} else {
			throw new IllegalArgumentException("Type de notation non gere : " + notationEnum);
		}
	}

	public String serialize(Plateau plateau, NotationEnum notationEnum) {
		if (notationEnum == NotationEnum.FEN) {
			return notationFEN.serialize(plateau);
		} else if (notationEnum == NotationEnum.CUSTOM) {
			return notationCustom.serialize(plateau);
		} else {
			throw new IllegalArgumentException("Type de notation non gere : " + notationEnum);
		}
	}
}
