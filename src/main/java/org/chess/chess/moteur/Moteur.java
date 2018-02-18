package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.outils.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class Moteur {

	public static final Logger LOGGER = LoggerFactory.getLogger(Moteur.class);

	private Plateau plateau;

	private Couleur joueur;

	@PostConstruct
	public void initialise() {
		LOGGER.info("initialisation du moteur ...");

		plateau = new Plateau();
		plateau.initialise();
		plateau.afficheConsole();

		joueur = Couleur.Blanc;

		LOGGER.info("initialisation du moteur OK");
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public void nextMove() {

	}

	private List<Position> listePieces(Couleur couleur) {
		Verify.verifyNotNull(couleur);
		List<Position> liste = new ArrayList<>();
		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				PieceCouleur piece = plateau.getCase(i, j);
				if (piece != null && piece.getCouleur() == couleur) {
					liste.add(new Position(i, j));
				}
			}
		}
		return liste;
	}

	private List<Position> listMove(int ligne, int colonne) {
		Check.checkLigneColonne(ligne, colonne);
		List<Position> liste = new ArrayList<>();

		PieceCouleur piece = plateau.getCase(ligne, colonne);
		if (piece != null) {
			if (piece.getPiece() == Piece.PION) {
				int decalage;
				if (piece.getCouleur() == Couleur.Blanc) {
					decalage = -1;
				} else {
					decalage = 1;
				}
				ajoutePosition(liste, ligne, colonne + decalage);
			} else if(piece.getPiece() == Piece.CAVALIER){

			}
		}

		return liste;
	}

	private void ajoutePosition(List<Position> liste, int ligne, int colonne) {
		if (Check.isPositionValide(ligne, colonne)) {
			PieceCouleur piece = plateau.getCase(ligne, colonne);
			if (piece == null) {
				liste.add(new Position(ligne, colonne));
			}
		}
	}
}
