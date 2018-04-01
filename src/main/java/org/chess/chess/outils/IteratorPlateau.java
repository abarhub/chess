package org.chess.chess.outils;

import org.chess.chess.domain.ColonneEnum;
import org.chess.chess.domain.RangeeEnum;

import java.util.Arrays;

public class IteratorPlateau {

	public static Iterable<RangeeEnum> getIterableRangee() {
		return Arrays.asList(RangeeEnum.values());
	}

	public static Iterable<ColonneEnum> getIterableColonne() {
		return Arrays.asList(ColonneEnum.values());
	}
}
