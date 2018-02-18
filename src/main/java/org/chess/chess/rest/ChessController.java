package org.chess.chess.rest;

import org.chess.chess.dto.Greeting;
import org.chess.chess.dto.PlateauDTO;
import org.chess.chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {

	@Autowired
	private ChessService chessService;

	@RequestMapping("/plateau")
	public PlateauDTO plateau() {
		return chessService.getPlateauDto();
	}


}
