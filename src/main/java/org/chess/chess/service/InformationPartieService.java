package org.chess.chess.service;

import org.chess.chess.domain.InformationPartie;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InformationPartieService {

	public InformationPartie createInformationPartie() {
		InformationPartie informationPartie = new InformationPartie();
		informationPartie.setDate(LocalDate.now());
		return informationPartie;
	}
}
