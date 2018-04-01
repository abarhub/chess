package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.outils.IteratorPlateau;
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

	public List<Position2> listMove(Plateau plateau, Position2 position, boolean tousMouvementRois,
	                                Couleur joueurCourant) {
		if (methode2) {
			return calculMouvementsService.listMove(plateau, position, tousMouvementRois, joueurCourant);
		} else {
			return mouvementService2.listMove(plateau, position, tousMouvementRois, joueurCourant);
		}
	}


	// vérifie si la case (ligne/colonne) est attaquée par une piece de couleur
	public boolean caseAttaque(Plateau plateau, Couleur couleur, Position2 position) {

		if (methode2) {
			return calculMouvementsService.caseAttaque(plateau, couleur, position);
		} else {
			return mouvementService2.caseAttaque(plateau, couleur, position);
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


	public List<Position2> listePieces(Plateau plateau, Couleur couleur) {
		Verify.verifyNotNull(couleur);
		List<Position2> liste = new ArrayList<>();
		//for (int i = 0; i < Plateau.NB_LIGNES; i++) {
		for (RangeeEnum rangee : IteratorPlateau.getIterableRangee()) {
			//for (int j = 0; j < Plateau.NB_COLONNES; j++) {
			for (ColonneEnum colonne : IteratorPlateau.getIterableColonne()) {
				PieceCouleur piece = plateau.getCase(new Position2(rangee, colonne));
				if (piece != null && piece.getCouleur() == couleur) {
					liste.add(new Position2(rangee, colonne));
				}
			}
		}
		return liste;
	}


	public List<Position2> getMovablePieces(Plateau plateau, Couleur joueur) {
		Verify.verifyNotNull(joueur);
		List<Position2> listePieces;
		List<Position2> liste = listePieces(plateau, joueur);
		listePieces = new ArrayList<>();
		for (Position2 p : liste) {
			List<Position2> liste2 = listMove(plateau, p, false, joueur);
			if (liste2 != null && !liste2.isEmpty()) {
				listePieces.add(p);
			}
		}
		return listePieces;
	}

}
