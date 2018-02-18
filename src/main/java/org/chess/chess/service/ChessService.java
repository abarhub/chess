package org.chess.chess.service;

import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.PieceCouleur;
import org.chess.chess.domain.Plateau;
import org.chess.chess.dto.PieceDTO;
import org.chess.chess.dto.PlateauDTO;
import org.chess.chess.moteur.Moteur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChessService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ChessService.class);

	@Autowired
	private Moteur moteur;

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

		return plateauDTO;
	}
}
