package org.chess.chess.outils;

import org.chess.chess.domain.ColonneEnum;
import org.chess.chess.domain.Position2;
import org.chess.chess.domain.RangeeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IteratorPlateau {

	public static Iterable<RangeeEnum> getIterableRangee() {
		return Arrays.asList(RangeeEnum.values());
	}

	public static Iterable<RangeeEnum> getIterableRangeeInverse() {
		List<RangeeEnum> liste = Arrays.asList(RangeeEnum.values());
		Collections.reverse(liste);
		return liste;
	}

	public static Iterable<ColonneEnum> getIterableColonne() {
		return Arrays.asList(ColonneEnum.values());
	}

	public static Iterable<ColonneEnum> getIterableColonneInverse() {
		List<ColonneEnum> liste = Arrays.asList(ColonneEnum.values());
		Collections.reverse(liste);
		return liste;
	}

	public static Iterable<Position2> getIterablePlateau() {
		List<Position2> liste = new ArrayList<>();
		for (RangeeEnum range : getIterableRangeeInverse()) {
			for (ColonneEnum colonne : getIterableColonne()) {
				liste.add(new Position2(range, colonne));
			}
		}
		return liste;
	}
}
