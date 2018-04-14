package org.chess.chess.notation;

import org.chess.chess.domain.NotationEnum;
import org.chess.chess.domain.Partie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotationService {

	@Autowired
	private NotationFEN notationFEN;

	@Autowired
	private NotationCustom notationCustom;

	@Autowired
	private NotationPGN notationPGN;

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

	public Partie createFromString(String str, NotationEnum notationEnum) {
		if (notationEnum == NotationEnum.FEN) {
			return notationFEN.createPlateau(str);
		} else if (notationEnum == NotationEnum.CUSTOM) {
			return notationCustom.createPlateau(str);
		} else if (notationEnum == NotationEnum.PGN) {
			return notationPGN.createPlateau(str);
		} else {
			throw new IllegalArgumentException("Type de notation non gere : " + notationEnum);
		}
	}

	public String serialize(Partie partie, NotationEnum notationEnum) {
		if (notationEnum == NotationEnum.FEN) {
			return notationFEN.serialize(partie);
		} else if (notationEnum == NotationEnum.CUSTOM) {
			return notationCustom.serialize(partie);
		} else if (notationEnum == NotationEnum.PGN) {
			return notationPGN.serialize(partie);
		} else {
			throw new IllegalArgumentException("Type de notation non gere : " + notationEnum);
		}
	}
}
