package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtatService {

	public static final Logger LOGGER = LoggerFactory.getLogger(EtatService.class);

	@Autowired
	private MouvementService mouvementService;

	public EtatJeux calculEtatJeux(Partie partie) {
		Plateau plateau = partie.getPlateau();
		Couleur joueurCourant = partie.getJoueurCourant();
		List<PieceCouleurPosition> liste = plateau.getStreamPosition()
				.peek(x -> LOGGER.info("pos={}", x))
				.filter(x -> x.getPiece() == Piece.ROI)
				.peek(x -> LOGGER.info("pos2={}", x))
				.filter(x -> mouvementService.caseAttaque(partie,
						mouvementService.couleurContraire(x.getCouleur()),
						x.getPosition())
				)
				.peek(x -> LOGGER.info("pos3={}", x))
				.collect(Collectors.toList());

		LOGGER.info("calculEtatJeux:liste={}", liste);

//		for (PieceCouleurPosition p : liste) {
//			if (mouvementService.caseAttaque(plateau, p.getCouleur(),
//					p.getPosition().getLigne(), p.getPosition().getColonne())) {
//				LOGGER.info("calculEtatJeux:attaque={}", p);
//			} else {
//				LOGGER.info("calculEtatJeux:pas attaque={}", p);
//			}
//		}

		if (liste == null || liste.isEmpty()) {
			// aucun roi n'est en echecs
			Optional<?> opt = plateau.getStreamPosition().findAny();
			if (opt.isPresent()) {
				// il y a des mouvements possibles
				if (joueurCourant == Couleur.Blanc) {
					return EtatJeux.MOUVEMENT_BLANC;
				} else {
					return EtatJeux.MOUVEMENT_NOIR;
				}
			} else {
				// plus de mouvement possible => MAT
				return EtatJeux.MAT;
			}
		} else {
			// il y a des rois qui sont en echecs
			for (PieceCouleurPosition p : liste) {
				List<Position> liste2 = mouvementService.listMove(partie, p.getPosition(),
						false, p.getCouleur());
				if (liste2 == null || liste2.isEmpty()) {
					// le roi n'a pas de mouvement possible
					if (p.getCouleur() == Couleur.Blanc) {
						return EtatJeux.ECHECS_ET_MAT_BLANC;
					} else {
						return EtatJeux.ECHECS_ET_MAT_NOIR;
					}
				}
			}
			Verify.verify(liste.size() == 1);
			PieceCouleurPosition p = liste.get(0);
			if (p.getCouleur() == Couleur.Blanc) {
				return EtatJeux.ECHECS_BLANC;
			} else {
				return EtatJeux.ECHECS_NOIR;
			}
		}
	}
}
