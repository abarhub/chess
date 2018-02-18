package org.chess.chess.moteur;

import org.chess.chess.domain.Plateau;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Moteur {

	public static final Logger LOGGER = LoggerFactory.getLogger(Moteur.class);

	private Plateau plateau;

	@PostConstruct
	public void initialise() {
		LOGGER.info("initialisation du moteur ...");

		plateau = new Plateau();
		plateau.initialise();
		plateau.afficheConsole();

		LOGGER.info("initialisation du moteur OK");
	}

}
