package org.chess.chess.service;

import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import org.chess.chess.domain.*;
import org.chess.chess.dto.*;
import org.chess.chess.evaluateur.ShannonEval;
import org.chess.chess.joueur.Joueur;
import org.chess.chess.joueur.JoueurHazard;
import org.chess.chess.joueur.JoueurNegaMax;
import org.chess.chess.joueur.TypeJoueur;
import org.chess.chess.moteur.CalculMouvementsService;
import org.chess.chess.moteur.Moteur;
import org.chess.chess.notation.NotationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChessService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ChessService.class);

	@Autowired
	private Moteur moteur;

	@Autowired
	private CalculMouvementsService calculMouvementsService;

	@Autowired
	private NotationService notationService;

	public PlateauDTO getPlateauDto() {

		PlateauDTO plateauDTO = new PlateauDTO();
		plateauDTO.setListePieces(new ArrayList<>());

		Plateau plateau = moteur.getPlateau();

		for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
				PieceCouleur pieceCouleur = plateau.getCase(new Position(ligne, colonne));

				if (pieceCouleur != null) {
					PieceDTO pieceDTO = new PieceDTO();
					pieceDTO.setLigne(ligne);
					pieceDTO.setColonne(colonne);
					pieceDTO.setPiece(pieceCouleur.getPiece().getNomCourt());
					pieceDTO.setCouleurBlanc(pieceCouleur.getCouleur() == Couleur.Blanc);

					plateauDTO.getListePieces().add(pieceDTO);
				}
			}
		}

		EtatJeux etat = moteur.calculEtatJeux();
		plateauDTO.setEtatJeux(etat.name());

		return plateauDTO;
	}

	public void nextMove() {
		moteur.nextMove();
	}

	public List<PositionDTO> getDeplacements(int ligne, int colonne) {
		if (false) {
			return getDeplacements0(ligne, colonne);
		} else {
			return getDeplacements2(ligne, colonne);
		}
	}

	public List<PositionDTO> getDeplacements0(int ligne, int colonne) {
		Position position = new Position(ligne, colonne);
		List<Position> res = moteur.listMove(position, true);

		List<PositionDTO> liste = new ArrayList<>();

		if (!CollectionUtils.isEmpty(res)) {
			for (Position p : res) {
				PositionDTO positionDTO = new PositionDTO(p.getLigne(), p.getColonne());
				liste.add(positionDTO);
			}
		}

		LOGGER.info("positions: {}", liste);

		return liste;
	}

	public List<PositionDTO> getDeplacements2(int ligne, int colonne) {
		List<PositionDTO> list = new ArrayList<>();

		ListeMouvements listeMouvements = calculMouvementsService.calculMouvements(moteur.getPlateau());

		if (listeMouvements != null) {
			List<Mouvement> mouvementList = listeMouvements.getMouvements(new Position(ligne, colonne));

			LOGGER.info("mouvementList: {}", mouvementList);

			if (!CollectionUtils.isEmpty(mouvementList)) {
				for (Mouvement mouvement : mouvementList) {
					list.add(new PositionDTO(mouvement.getPosition()));
				}
			}
		}

		return list;
	}

	public FenDTO getPlateauFenDto() {
		FenDTO fenDTO = new FenDTO();

		Plateau plateau = moteur.getPlateau();

		fenDTO.setFen(notationService.serialize(plateau, NotationEnum.FEN));

		return fenDTO;
	}

	public List<TypeJoueur> getListeTypeJoueur() {
		List<TypeJoueur> liste = Lists.newArrayList(TypeJoueur.values());
		Collections.sort(liste);
		return liste;
	}

	public void demarrage(String joueurBlanc, String joueurNoir, String valeursInitiales) {

		Verify.verifyNotNull(joueurBlanc);
		Verify.verifyNotNull(joueurNoir);

		Plateau plateau;

		if (StringUtils.isEmpty(valeursInitiales)) {

			plateau = new Plateau();

			plateau.initialise();
		} else {
			plateau = notationService.createFromString(valeursInitiales, NotationEnum.FEN);
		}

		TypeJoueur joueurBlanc2 = TypeJoueur.valueOf(joueurBlanc);
		TypeJoueur joueurNoir2 = TypeJoueur.valueOf(joueurNoir);

		moteur.initialise(plateau, Couleur.Blanc,
				creerJoueur(joueurBlanc2, Couleur.Blanc),
				creerJoueur(joueurNoir2, Couleur.Noir));
	}

	public Joueur creerJoueur(TypeJoueur joueur, Couleur couleur) {
		Verify.verifyNotNull(joueur);
		Verify.verifyNotNull(couleur);

		if (joueur == TypeJoueur.JOUEUR_HAZARD) {
			return new JoueurHazard(couleur);
		} else if (joueur == TypeJoueur.JOUEUR_NEGAMAX1) {
			return new JoueurNegaMax(couleur, 1, new ShannonEval());
		} else {
			throw new IllegalArgumentException("Type de joueur non géré : " + joueur);
		}
	}

	public StatusDTO getStatus() {
		StatusDTO statusDTO = new StatusDTO();

		EtatJeux etatJeux = moteur.calculEtatJeux();

		if (etatJeux == EtatJeux.MAT) {
			statusDTO.setEtat("MAT");
			statusDTO.setJoueur(Couleur.Blanc.name());
		} else if (etatJeux == EtatJeux.MOUVEMENT_BLANC
				|| etatJeux == EtatJeux.MOUVEMENT_NOIR) {
			statusDTO.setEtat("EN_COURS");
			statusDTO.setJoueur(etatJeux.getCouleur().name());
		} else if (etatJeux == EtatJeux.ECHECS_NOIR
				|| etatJeux == EtatJeux.ECHECS_BLANC) {
			statusDTO.setEtat("ECHECS");
			statusDTO.setJoueur(etatJeux.getCouleur().name());
		} else if (etatJeux == EtatJeux.ECHECS_ET_MAT_NOIR
				|| etatJeux == EtatJeux.ECHECS_ET_MAT_BLANC) {
			statusDTO.setEtat("ECHECS_ET_MAT");
			statusDTO.setJoueur(etatJeux.getCouleur().name());
		}

		return statusDTO;
	}

	public void logInfos() {
		LOGGER.info("logInfos debut");

		EtatJeux etatJeux = moteur.calculEtatJeux();
		LOGGER.info("etatJeux={}", etatJeux);

		Plateau plateau = moteur.getPlateau();
		LOGGER.info("fen={}", notationService.serialize(plateau, NotationEnum.FEN));

		LOGGER.info("custom={}", notationService.serialize(plateau, NotationEnum.CUSTOM));


		ListeMouvements listeMouvements = calculMouvementsService.calculMouvements(moteur.getPlateau());

		LOGGER.info("mouvementList: {}", listeMouvements);

		LOGGER.info("logInfos fin");
	}
}
