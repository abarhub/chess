package org.chess.chess.outils;

import com.google.common.base.Verify;
import org.chess.chess.domain.ColonneEnum;
import org.chess.chess.domain.Position;
import org.chess.chess.domain.RangeeEnum;

import java.util.Optional;

public class PositionTools {

	public static Position getPosition(int ligne, int colonne) {
		Check.checkLigneColonne(ligne, colonne);
		return new Position(RangeeEnum.get(ligne + 1), ColonneEnum.get(colonne + 1));
	}

	public static int getLigne(Position position) {
		Verify.verifyNotNull(position);
		return position.getRangee().getNo() - 1;
	}

	public static int getColonne(Position position) {
		Verify.verifyNotNull(position);
		return position.getColonne().getNo() - 1;
	}

	public static Optional<Position> getPosition(RangeeEnum rangee, int decalageRange, ColonneEnum colonne, int decalageColonne) {
		Verify.verifyNotNull(rangee);
		Verify.verifyNotNull(colonne);
		int noLigne = rangee.getNo() - 1 + decalageRange;
		int noColonne = colonne.getNo() - 1 + decalageColonne;
		if (Check.isPositionValide(noLigne, noColonne)) {
			return Optional.of(new Position(RangeeEnum.get(noLigne + 1), ColonneEnum.get(noColonne + 1)));
		} else {
			return Optional.empty();
		}
	}

	public static Optional<Position> getPosition(Position position, int decalageRange, int decalageColonne) {
		Verify.verifyNotNull(position);
		return getPosition(position.getRangee(), decalageRange, position.getColonne(), decalageColonne);
	}
}
