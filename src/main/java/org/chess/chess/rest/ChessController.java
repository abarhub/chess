package org.chess.chess.rest;

import org.chess.chess.dto.FenDTO;
import org.chess.chess.dto.PlateauDTO;
import org.chess.chess.dto.PositionDTO;
import org.chess.chess.dto.StatusDTO;
import org.chess.chess.joueur.TypeJoueur;
import org.chess.chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChessController {

	@Autowired
	private ChessService chessService;

	@RequestMapping("/plateau")
	public PlateauDTO plateau() {
		return chessService.getPlateauDto();
	}

	@RequestMapping("/action")
	public void action(@RequestParam(value = "nom") String nom) {
		if (nom != null) {
			if (nom.equals("next")) {
				chessService.nextMove();
			}
		}
	}

	@RequestMapping("/deplacements/{ligne}/{colonne}")
	public List<PositionDTO> getDeplacements(@PathVariable("ligne") int ligne,
	                                         @PathVariable("colonne") int colonne) {
		return chessService.getDeplacements(ligne, colonne);
	}

	@RequestMapping("/plateauFen")
	public FenDTO plateauFen() {
		return chessService.getPlateauFenDto();
	}

	@RequestMapping("/listeTypeJoueur")
	public List<TypeJoueur> typeJoueurs() {
		return chessService.getListeTypeJoueur();
	}


	@RequestMapping("/demarrage")
	public void demarrage(@RequestParam(value = "joueurBlanc") String joueurBlanc,
	                      @RequestParam(value = "joueurNoir") String joueurNoir,
	                      @RequestParam(value = "valeursInitiales", required = false) String valeursInitiales) {
		chessService.demarrage(joueurBlanc, joueurNoir, valeursInitiales);
	}

	@RequestMapping("/status")
	public StatusDTO status() {
		return chessService.getStatus();
	}

	@RequestMapping("/logInfos")
	public void logInfos() {
		chessService.logInfos();
	}


}
