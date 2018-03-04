package org.chess.chess.moteur;

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
				.filter(x -> x.getPiece() == Piece.ROI
								&& joueurCourant == x.getCouleur()
								&& mouvementService.caseAttaque(plateau,
						mouvementService.couleurContraire(joueurCourant),
						x.getPosition().getLigne(), x.getPosition().getColonne())
						//&& caseAttaque(couleurContraire(x.getCouleur()),
						//x.getPosition().getLigne(), x.getPosition().getColonne())
				)
				.collect(Collectors.toList());

		LOGGER.info("calculEtatJeux:liste={}", liste);

		for (PieceCouleurPosition p : liste) {
			if (mouvementService.caseAttaque(plateau, p.getCouleur(),
					p.getPosition().getLigne(), p.getPosition().getColonne())) {
				LOGGER.info("calculEtatJeux:attaque={}", p);
			} else {
				LOGGER.info("calculEtatJeux:pas attaque={}", p);
			}
		}

		if (liste == null || liste.isEmpty()) {
			Optional<?> opt = plateau.getStreamPosition().findAny();
			if (opt.isPresent()) {
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
			PieceCouleurPosition p = liste.get(0);
			List<Position> liste2 = mouvementService.listMove(plateau, p.getPosition(), false, joueurCourant);
			LOGGER.info("calculEtatJeux:liste2={}", liste2);
			if (liste2 == null || liste2.isEmpty()) {
				if (p.getCouleur() == Couleur.Blanc) {
					return EtatJeux.ECHECS_ET_MAT_BLANC;
				} else {
					return EtatJeux.ECHECS_ET_MAT_NOIR;
				}
			} else {
				if (p.getCouleur() == Couleur.Blanc) {
					return EtatJeux.ECHECS_BLANC;
				} else {
					return EtatJeux.ECHECS_NOIR;
				}
			}
		}
	}
}
