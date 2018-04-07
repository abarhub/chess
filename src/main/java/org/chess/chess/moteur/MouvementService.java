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

import static org.chess.chess.outils.IteratorPlateau.getIterablePlateau;

@Service
public class MouvementService {

	public static final Logger LOGGER = LoggerFactory.getLogger(MouvementService.class);

	@Autowired
	private CalculMouvementsService calculMouvementsService;

	public List<Position> listMove(Plateau plateau, Position position, boolean tousMouvementRois,
	                               Couleur joueurCourant) {
		return calculMouvementsService.listMove(plateau, position, tousMouvementRois, joueurCourant);
	}

	// vérifie si la case (ligne/colonne) est attaquée par une piece de couleur
	public boolean caseAttaque(Plateau plateau, Couleur couleur, Position position) {
		return calculMouvementsService.caseAttaque(plateau, couleur, position);
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
		for (Position position : getIterablePlateau()) {
			PieceCouleur piece = plateau.getCase(position);
			if (piece != null && piece.getCouleur() == couleur) {
				liste.add(position);
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
