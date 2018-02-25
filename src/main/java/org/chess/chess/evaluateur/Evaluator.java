package org.chess.chess.evaluateur;

import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Plateau;
import org.chess.chess.moteur.Moteur;

public abstract class Evaluator {

	public abstract float evaluation(Moteur moteur, Plateau plateau, Couleur couleur);

}
