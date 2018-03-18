package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.PieceCouleur;
import org.chess.chess.domain.Plateau;
import org.chess.chess.domain.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MouvementService {

	public static final Logger LOGGER = LoggerFactory.getLogger(MouvementService.class);

	public static final boolean methode2 = true;

	@Autowired
	private MouvementService2 mouvementService2;

	@Autowired
	private CalculMouvementsService calculMouvementsService;

	public List<Position> listMove(Plateau plateau, Position position, boolean tousMouvementRois,
	                               Couleur joueurCourant) {
		if (methode2) {
			return calculMouvementsService.listMove(plateau, position, tousMouvementRois, joueurCourant);
		} else {
			return mouvementService2.listMove(plateau, position, tousMouvementRois, joueurCourant);
		}
	}


	// vérifie si la case (ligne/colonne) est attaquée par une piece de couleur
	public boolean caseAttaque(Plateau plateau, Couleur couleur, int ligne, int colonne) {

		if (methode2) {
			return calculMouvementsService.caseAttaque(plateau, couleur, ligne, colonne);
		} else {
			return mouvementService2.caseAttaque(plateau, couleur, ligne, colonne);
		}
	}


	public Couleur couleurContraire(Couleur couleur) {
		Verify.verifyNotNull(couleur);
		if (couleur == Couleur.Blanc) {
			return Couleur.Noir;
		} else {
			return Couleur.Blanc;
		}
	}


	public List<Position> listePieces(Plateau plateau, Couleur couleur) {
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


	public List<Position> getMovablePieces(Plateau plateau, Couleur joueur) {
		Verify.verifyNotNull(joueur);
		List<Position> listePieces;
		List<Position> liste = listePieces(plateau, joueur);
		listePieces = new ArrayList<>();
		for (Position p : liste) {
			List<Position> liste2 = listMove(plateau, p, false, joueur);
			if (liste2 != null && !liste2.isEmpty()) {
				listePieces.add(p);
			}
		}
		return listePieces;
	}

}
