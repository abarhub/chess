package org.chess.chess.joueur;

import com.google.common.base.Verify;
import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Position2;
import org.chess.chess.moteur.Moteur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class JoueurHazard extends Joueur {

	public static final Logger LOGGER = LoggerFactory.getLogger(JoueurHazard.class);

	private Random random;

	public JoueurHazard(Couleur couleur) {
		super(couleur);
		long seed = System.currentTimeMillis();
		LOGGER.info("seed({})={}", couleur, seed);
		random = new Random(seed);
	}

	@Override
	public void nextMove(Moteur moteur) {

		LOGGER.info("nextMove: {}", getCouleur());

		List<Position2> listePieces = moteur.getMovablePieces(getCouleur());

		if (listePieces != null && !listePieces.isEmpty()) {
			int no = random.nextInt(listePieces.size());

			Position2 p = listePieces.get(no);

			List<Position2> liste = moteur.listMove(p, false);

			Verify.verifyNotNull(liste);
			Verify.verify(!liste.isEmpty());

			Position2 deplacement;
			if (liste.size() == 1) {
				deplacement = liste.get(0);
			} else {
				no = random.nextInt(liste.size());
				deplacement = liste.get(no);
			}

			//PieceCouleur p2 = plateau.getCase(p.getLigne(), p.getColonne());
			moteur.move(p, deplacement);

			//joueurCourant = couleurContraire(joueurCourant);

		} else {
			throw new IllegalStateException("Deplacement impossible !");
		}
	}
}
