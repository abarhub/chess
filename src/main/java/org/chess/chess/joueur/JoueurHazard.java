package org.chess.chess.joueur;

import com.google.common.base.Verify;
import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Piece;
import org.chess.chess.domain.PieceCouleur;
import org.chess.chess.domain.Position;
import org.chess.chess.moteur.Moteur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class JoueurHazard extends Joueur {

	public static final Logger LOGGER = LoggerFactory.getLogger(JoueurHazard.class);

	private Random random;
	private boolean agressif;

	public JoueurHazard(Couleur couleur) {
		this(couleur, true);
	}

	public JoueurHazard(Couleur couleur, boolean agressif) {
		super(couleur);
		long seed = System.currentTimeMillis();
		LOGGER.info("seed({})={}", couleur, seed);
		random = new Random(seed);
		this.agressif = agressif;
	}

	@Override
	public void nextMove(Moteur moteur) {

		LOGGER.info("nextMove: {}", getCouleur());

		List<Position> listePieces = moteur.getMovablePieces(getCouleur());

		if (listePieces != null && !listePieces.isEmpty()) {

			if (agressif) {

				trouveMove(listePieces, moteur);

			} else {

				deplacementAleatoire(moteur, listePieces);

			}

		} else {
			throw new IllegalStateException("Deplacement impossible !");
		}
	}

	private void deplacementAleatoire(Moteur moteur, List<Position> listePieces) {
		int no = random.nextInt(listePieces.size());

		Position p = listePieces.get(no);

		List<Position> liste = moteur.listMove(p, false);

		Verify.verifyNotNull(liste);
		Verify.verify(!liste.isEmpty());

		Position deplacement;
		if (liste.size() == 1) {
			deplacement = liste.get(0);
		} else {
			no = random.nextInt(liste.size());
			deplacement = liste.get(no);
		}

		moteur.move(p, deplacement);
	}

	private void trouveMove(List<Position> listePieces, Moteur moteur) {

		Position meilleurDeplacement = null;
		Piece pieceMange = null;
		Position pieceQuiMange = null;

		for (Position piece : listePieces) {

			List<Position> liste = moteur.listMove(piece, false);

			for (Position pos : liste) {
				PieceCouleur p = moteur.getPlateau().getCase(pos);

				if (p != null) {
					if (p.getCouleur() != getCouleur()) {
						if (pieceMange == null || pieceMange.getValeur() < p.getPiece().getValeur()) {
							meilleurDeplacement = pos;
							pieceMange = p.getPiece();
							pieceQuiMange = piece;
						}
					}
				}
			}

		}

		if (meilleurDeplacement != null) {
			moteur.move(pieceQuiMange, meilleurDeplacement);
		} else {
			deplacementAleatoire(moteur, listePieces);
		}

	}
}
