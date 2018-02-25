package org.chess.chess.joueur;

import org.chess.chess.domain.Couleur;
import org.chess.chess.moteur.Moteur;

public abstract class Joueur {

	private final Couleur couleur;

	public Joueur(Couleur couleur) {
		this.couleur = couleur;
	}

	public Couleur getCouleur() {
		return couleur;
	}

	public abstract void nextMove(Moteur moteur);

}
