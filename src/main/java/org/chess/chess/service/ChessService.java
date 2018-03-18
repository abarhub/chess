package org.chess.chess.service;

import org.chess.chess.domain.*;
import org.chess.chess.dto.PieceDTO;
import org.chess.chess.dto.PlateauDTO;
import org.chess.chess.dto.PositionDTO;
import org.chess.chess.moteur.CalculMouvementsService;
import org.chess.chess.moteur.Moteur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ChessService.class);

	@Autowired
	private Moteur moteur;

	@Autowired
	private CalculMouvementsService calculMouvementsService;

	public PlateauDTO getPlateauDto() {

		PlateauDTO plateauDTO = new PlateauDTO();
		plateauDTO.setListePieces(new ArrayList<>());

		Plateau plateau = moteur.getPlateau();

		for (int ligne = 0; ligne < Plateau.NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < Plateau.NB_COLONNES; colonne++) {
				PieceCouleur pieceCouleur = plateau.getCase(ligne, colonne);

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
}
