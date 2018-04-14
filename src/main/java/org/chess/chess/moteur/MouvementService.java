package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
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

	public List<Position> listMove(Partie partie, Position position, boolean tousMouvementRois,
	                               Couleur joueurCourant) {
		Verify.verifyNotNull(partie);
		Verify.verifyNotNull(position);
		Verify.verifyNotNull(joueurCourant);
		return calculMouvementsService.listMove(partie, position, tousMouvementRois, joueurCourant);
	}

	// vérifie si la case (ligne/colonne) est attaquée par une piece de couleur
	public boolean caseAttaque(Partie partie, Couleur couleur, Position position) {
		return calculMouvementsService.caseAttaque(partie, couleur, position);
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


	public List<Position> getMovablePieces(Partie partie, Couleur joueur) {
		Verify.verifyNotNull(partie);
		Verify.verifyNotNull(joueur);
		List<Position> listePieces;
		List<Position> liste = listePieces(partie.getPlateau(), joueur);
		listePieces = new ArrayList<>();
		for (Position p : liste) {
			List<Position> liste2 = listMove(partie, p, false, joueur);
			if (liste2 != null && !liste2.isEmpty()) {
				listePieces.add(p);
			}
		}
		return listePieces;
	}

	public List<Position> listePieces(Plateau plateau, PieceCouleur pieceCouleur) {
		Verify.verifyNotNull(pieceCouleur);
		List<Position> liste = new ArrayList<>();
		for (Position position : getIterablePlateau()) {
			PieceCouleur piece = plateau.getCase(position);
			if (piece != null && piece.getCouleur() == pieceCouleur.getCouleur()
					&& piece.getPiece() == pieceCouleur.getPiece()) {
				liste.add(position);
			}
		}
		return liste;
	}

}
