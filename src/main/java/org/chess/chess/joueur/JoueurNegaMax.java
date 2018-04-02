package org.chess.chess.joueur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.evaluateur.Evaluator;
import org.chess.chess.moteur.Moteur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JoueurNegaMax extends Joueur {

	public static final Logger LOGGER = LoggerFactory.getLogger(JoueurNegaMax.class);

	private final int profondeur;

	private final Evaluator evaluator;

	public JoueurNegaMax(Couleur couleur, int profondeur, Evaluator evaluator) {
		super(couleur);
		this.profondeur = profondeur;
		this.evaluator = evaluator;
	}

	@Override
	public void nextMove(Moteur moteur) {
		LOGGER.info("nextMove: {}", getCouleur());

		Deplacement deplacement = calculNegaMax(moteur, getCouleur(), profondeur);

		Verify.verifyNotNull(deplacement);

		moteur.move(deplacement.getPositionSrc(), deplacement.getPositionDest());
	}

	private Deplacement calculNegaMax(Moteur moteur, Couleur couleur, int profondeur) {
		Plateau plateau = moteur.getPlateau();

		Deplacement meilleurDeplacement = null;
		float meilleurEval = 0.0f;

		List<Position> liste = moteur.listePieces(couleur);
		if (liste != null) {
			for (Position p : liste) {
				List<Position> liste2 = moteur.listMove(p, false);
				if (liste2 != null && !liste2.isEmpty()) {
					for (Position p2 : liste2) {
						Plateau plateau2 = new Plateau(plateau);
						plateau2.move(p, p2);
						float eval = evaluation(moteur, plateau2, couleur);

						if (meilleurDeplacement == null
								|| meilleurEval < eval) {
							meilleurDeplacement = new Deplacement(p, p2);
							meilleurEval = eval;
						}
					}
				}
			}
		}

		return meilleurDeplacement;
	}

	private float evaluation(Moteur moteur, Plateau plateau, Couleur couleur) {
		return evaluator.evaluation(moteur, plateau, couleur);
	}

}
