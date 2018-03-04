package org.chess.chess.moteur;

import com.google.common.base.Verify;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.chess.chess.domain.Couleur;
import org.chess.chess.domain.EtatJeux;
import org.chess.chess.domain.Partie;
import org.chess.chess.domain.Plateau;
import org.chess.chess.joueur.JoueurHazard;
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

	@Before
	public void setUp() throws Exception {
		mouvementService = new MouvementService();

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

		Partie partie = new Partie(plateau, new JoueurHazard(Couleur.Blanc),
				new JoueurHazard(Couleur.Noir),
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