package org.chess.chess.outils;

import com.google.common.base.Verify;
import org.chess.chess.domain.Plateau;

public class Check {

	public static void checkLigne(int ligne) {
		Verify.verify(ligne >= 0,
				"ligne invalide (" + ligne + "<0)");
		Verify.verify(ligne < Plateau.NB_LIGNES,
				"ligne invalide (" + ligne + ">" + Plateau.NB_LIGNES + ")");
	}

	public static void checkColonne(int colonne) {
		Verify.verify(colonne >= 0,
				"colonne invalide (" + colonne + "<0)");
		Verify.verify(colonne < Plateau.NB_COLONNES,
				"colonne invalide (" + colonne + ">" + Plateau.NB_COLONNES + ")");
	}

	public static void checkLigneColonne(int ligne, int colonne) {
		checkLigne(ligne);
		checkLigne(colonne);
	}

	public static boolean isPositionValide(int ligne, int colonne) {
		if (ligne >= 0 && ligne < Plateau.NB_LIGNES
				&& colonne >= 0 && colonne < Plateau.NB_COLONNES) {
			return true;
		}
		return false;
	}
}
