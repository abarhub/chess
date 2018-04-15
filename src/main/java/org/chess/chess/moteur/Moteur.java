package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.evaluateur.ShannonEval;
import org.chess.chess.joueur.Joueur;
import org.chess.chess.joueur.JoueurHazard;
import org.chess.chess.joueur.JoueurNegaMax;
import org.chess.chess.service.InformationPartieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class Moteur {

	public static final Logger LOGGER = LoggerFactory.getLogger(Moteur.class);

	private Partie partie;

	private EtatJeux etatJeux;

	private Joueur joueurBlanc;

	private Joueur joueurNoir;

	@Autowired
	private MouvementService mouvementService;

	@Autowired
	private EtatService etatService;

	@Autowired
	private InformationPartieService informationPartieService;

	@PostConstruct
	public void initialise() {
		LOGGER.info("initialisation du moteur ...");

		Plateau plateau;
		Couleur joueurCourant;
		Joueur joueurBlanc, joueurNoir;

		if (true) {
			plateau = new Plateau();
			plateau.initialise();
			plateau.afficheConsole();
		} else {
			String str;
			str = "NT0.0;NF0.2;ND0.3;NR0.4;NF0.5;NT0.7;NP1.0;NP1.1;NP1.2;NP1.3;NP1.4;NP1.5;NP1.6;NP1.7;NC2.0;BP3.5;BP5.1;BC5.7;BP6.0;NC6.6;BP6.7;BT7.0;BF7.2;BR7.4;BT7.7;";
			plateau = new Plateau(str);
			plateau.afficheConsole();
		}

		joueurCourant = Couleur.Blanc;

		joueurBlanc = new JoueurHazard(Couleur.Blanc);
		//joueurNoir = new JoueurHazard(Couleur.Noir);
		joueurNoir = new JoueurNegaMax(Couleur.Noir, 1, new ShannonEval());

		initialise(plateau, joueurCourant, joueurBlanc, joueurNoir);

		LOGGER.info("initialisation du moteur OK");
	}

	public void initialise(Plateau plateau, Couleur joueur, Joueur joueurBlanc, Joueur joueurNoir) {
		Verify.verifyNotNull(plateau);
		Verify.verifyNotNull(joueur);
		Verify.verifyNotNull(joueurBlanc);
		Verify.verifyNotNull(joueurNoir);

		this.joueurBlanc = joueurBlanc;
		this.joueurNoir = joueurNoir;

		partie = new Partie(plateau, joueur, informationPartieService.createInformationPartie());
	}

	public Plateau getPlateau() {
		return partie.getPlateau();
	}

	public Partie getPartie() {
		return partie;
	}

	public void nextMove() {

		Verify.verifyNotNull(partie);

		EtatJeux etatJeuxAvantDeplacement = calculEtatJeux();

		if (etatJeuxAvantDeplacement.isFinPartie()) {
			// fin
			etatJeux = etatJeuxAvantDeplacement;
		} else {
			Couleur joueurAvantDeplacement = partie.getJoueurCourant();
			Verify.verifyNotNull(joueurAvantDeplacement);


			if (etatJeuxAvantDeplacement.getCouleur() == Couleur.Blanc) {
				joueurBlanc.nextMove(this);
			} else {
				joueurNoir.nextMove(this);
			}

			LOGGER.info("plateau={}", partie.getPlateau().getRepresentation());

			Verify.verifyNotNull(partie.getJoueurCourant());
			Verify.verify(joueurAvantDeplacement != partie.getJoueurCourant());

			etatJeux = calculEtatJeux();

			if (etatJeuxAvantDeplacement == EtatJeux.ECHECS_BLANC
					|| etatJeuxAvantDeplacement == EtatJeux.ECHECS_ET_MAT_NOIR) {
				LOGGER.info("etat={}", etatJeux);
				Verify.verify(etatJeux != EtatJeux.ECHECS_BLANC);
				Verify.verify(etatJeux != EtatJeux.ECHECS_NOIR);
			}
		}

		LOGGER.info("etat={}", etatJeux);
		if (etatJeux.isFinPartie()) {
			LOGGER.info("fin de partie");
		}
	}

	public Couleur couleurContraire(Couleur couleur) {
		return mouvementService.couleurContraire(couleur);
	}

	public List<Position> getMovablePieces(Couleur joueur) {
		return mouvementService.getMovablePieces(partie, joueur);
	}

	public List<Position> listePieces(Couleur couleur) {
		return mouvementService.listePieces(partie.getPlateau(), couleur);
	}

	public List<Position> listMove(Position position, boolean tousMouvementRois) {
		return mouvementService.listMove(partie, position, tousMouvementRois, partie.getJoueurCourant());
	}

	public EtatJeux calculEtatJeux() {
		return etatService.calculEtatJeux(partie);
	}

	public void move(Position src, Position dest) {
		partie.setMove(src, dest);
	}
}
