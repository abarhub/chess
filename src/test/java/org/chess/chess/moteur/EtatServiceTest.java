package org.chess.chess.moteur;

import com.google.common.base.Verify;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.EtatJeux;
import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Plateau;
import org.chess.chess.joueur.JoueurHazard;
import org.chess.chess.notation.NotationFEN;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnitParamsRunner.class)
public class EtatServiceTest {

	private MouvementService mouvementService;

	private EtatService etatService;

	private CalculMouvementsService calculMouvementsService;

	private NotationFEN notationFEN;

	@Before
	public void setUp() throws Exception {
		notationFEN = new NotationFEN();

		mouvementService = new MouvementService();

		calculMouvementsService = new CalculMouvementsService();
		ReflectionTestUtils.setField(mouvementService, "calculMouvementsService", calculMouvementsService);

		etatService = new EtatService();
		ReflectionTestUtils.setField(etatService, "mouvementService", mouvementService);
	}


	private Object[] calculEtatJeuxValues() {
		return new Object[]{
				new Object[]{"jeux/jeu1.txt", Couleur.Blanc, EtatJeux.ECHECS_BLANC},
				new Object[]{"jeux/jeu1.txt", Couleur.Noir, EtatJeux.ECHECS_BLANC},
		};
	}

	@Test
	@Parameters(method = "calculEtatJeuxValues")
	public void calculEtatJeux(final String nomFichier,
	                           final Couleur joueurCourant,
	                           final EtatJeux etatJeuxRef) throws IOException {

		Plateau plateau = getPlateau(nomFichier);

		Partie partie = new Partie(plateau, //new JoueurHazard(Couleur.Blanc),
				//new JoueurHazard(Couleur.Noir),
				joueurCourant);

		// methode testée
		final EtatJeux etatJeux = etatService.calculEtatJeux(partie);

		// vérifications
		assertNotNull(etatJeux);
		assertEquals(etatJeuxRef, etatJeux);
	}


	private Object[] calculEtatJeuxFenValues() {
		return new Object[]{
				new Object[]{"rnb2b1r/pp1qp1pp/P4k1n/3pP3/1P1P1p1P/R1p2NP1/2PNKP2/2BQ1B1R",
						Couleur.Blanc, EtatJeux.ECHECS_BLANC},
				new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R4KnP/2B4R",
						Couleur.Blanc, EtatJeux.ECHECS_BLANC},
				new Object[]{"r1b1kb1r/pp1pp1pp/nqp2p2/5P2/8/PP5N/R5nP/2B3KR",
						Couleur.Blanc, EtatJeux.ECHECS_BLANC},
		};
	}

	@Test
	@Parameters(method = "calculEtatJeuxFenValues")
	public void calculEtatJeuxFen(final String formatFen,
	                              final Couleur joueurCourant,
	                              final EtatJeux etatJeuxRef) throws IOException {

		Partie partie2 = notationFEN.createPlateau(formatFen);

		Plateau plateau = partie2.getPlateau();

		Partie partie = new Partie(plateau, //new JoueurHazard(Couleur.Blanc),
				//new JoueurHazard(Couleur.Noir),
				joueurCourant);

		// methode testée
		final EtatJeux etatJeux = etatService.calculEtatJeux(partie);

		// vérifications
		assertNotNull(etatJeux);
		assertEquals(etatJeuxRef, etatJeux);
	}

	// methodes utilitaires

	private Plateau getPlateau(String nomFichier) throws IOException {
		Verify.verifyNotNull(nomFichier);
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(nomFichier)) {
			Verify.verifyNotNull(input);
			StringBuilder textBuilder = new StringBuilder();
			try (Reader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8.name()))) {
				int c = 0;
				while ((c = reader.read()) != -1) {
					textBuilder.append((char) c);
				}
			}
			String str = textBuilder.toString();

			return new Plateau(str);
		}
	}
}