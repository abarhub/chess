package org.chess.chess.notation;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.chess.chess.domain.ConfigurationPartie;
import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Plateau;
import org.chess.chess.service.InformationPartieService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnitParamsRunner.class)
public class NotationFENTest {

	public static final Logger LOGGER = LoggerFactory.getLogger(NotationFENTest.class);

	private NotationFEN notationFEN = new NotationFEN();

	private InformationPartieService informationPartieService = new InformationPartieService();

	@Before
	public void setup() {
		ReflectionTestUtils.setField(notationFEN, "informationPartieService", informationPartieService);
	}

	private Object[] createSerialisationValues() {
		return new Object[]{
				new Object[]{"rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R w KQkq - 0 1"},
				new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R w KQkq - 0 1"},
				new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR w KQkq - 0 1"},
		};
	}

	@Test
	@Parameters(method = "createSerialisationValues")
	public void createSerialisation(String fenFormat) {

		LOGGER.info("fenFormat={}", fenFormat);

		Partie partie = notationFEN.createPlateau(fenFormat);

		assertNotNull(partie);

		Plateau plateau = partie.getPlateau();

		assertNotNull(plateau);

		String res = notationFEN.serialize(partie);

		LOGGER.info("res={}", res);

		assertNotNull(res);
		assertEquals(fenFormat, res);

	}

	@Test
	public void createPlateau() {

		String fenFormat = "rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R w KQkq - 0 1";

		Partie partie = notationFEN.createPlateau(fenFormat);

		assertNotNull(partie);

		assertNotNull(partie.getConfigurationPartie());

		assertEquals(Couleur.Blanc, partie.getJoueurCourant());

		ConfigurationPartie config = partie.getConfigurationPartie();

		assertEquals(true, config.isRoqueBlancRoi());
		assertEquals(true, config.isRoqueBlancDame());
		assertEquals(true, config.isRoqueNoirRoi());
		assertEquals(true, config.isRoqueNoirDame());

		assertEquals(0, config.getNbDemiCoupSansCapture());

		assertEquals(1, config.getNbCoup());

		String res = notationFEN.serialize(partie);

		assertEquals(fenFormat, res);
	}

	// fen avec des valeurs differentes
	@Test
	public void createPlateau2() {

		String fenFormat = "rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R b Kq - 15 34";

		Partie partie = notationFEN.createPlateau(fenFormat);

		assertNotNull(partie);

		assertNotNull(partie.getConfigurationPartie());

		assertEquals(Couleur.Noir, partie.getJoueurCourant());

		ConfigurationPartie config = partie.getConfigurationPartie();

		assertEquals(true, config.isRoqueBlancRoi());
		assertEquals(false, config.isRoqueBlancDame());
		assertEquals(false, config.isRoqueNoirRoi());
		assertEquals(true, config.isRoqueNoirDame());

		assertEquals(15, config.getNbDemiCoupSansCapture());

		assertEquals(34, config.getNbCoup());

		String res = notationFEN.serialize(partie);

		assertEquals(fenFormat, res);
	}

	// fen sans les information de fin
	@Test
	public void createPlateau3() {

		String fenFormat = "rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R";

		Partie partie = notationFEN.createPlateau(fenFormat);

		assertNotNull(partie);

		assertNotNull(partie.getConfigurationPartie());

		assertEquals(Couleur.Blanc, partie.getJoueurCourant());

		ConfigurationPartie config = partie.getConfigurationPartie();

		assertEquals(true, config.isRoqueBlancRoi());
		assertEquals(true, config.isRoqueBlancDame());
		assertEquals(true, config.isRoqueNoirRoi());
		assertEquals(true, config.isRoqueNoirDame());

		assertEquals(0, config.getNbDemiCoupSansCapture());

		assertEquals(1, config.getNbCoup());

		String res = notationFEN.serialize(partie);

		assertEquals(fenFormat + " w KQkq - 0 1", res);
	}

	@Test
	public void serialize() {
	}
}