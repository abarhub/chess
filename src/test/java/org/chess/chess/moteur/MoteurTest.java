package org.chess.chess.moteur;

import org.chess.chess.domain.*;
import org.chess.chess.joueur.Joueur;
import org.chess.chess.outils.PositionTools;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MoteurTest {

	public static final Logger LOGGER = LoggerFactory.getLogger(MoteurTest.class);

	@Mock
	private MouvementService mouvementService;

	@InjectMocks
	private Moteur moteur;

	private Plateau plateau;

	private Partie partie;

	@Mock
	private Joueur joueurBlanc;

	@Mock
	private Joueur joueurNoir;

	@Mock
	private EtatService etatService;

	@Before
	public void init() {

		plateau = new Plateau();

		partie = new Partie(plateau, joueurBlanc, joueurNoir, Couleur.Blanc);

		ReflectionTestUtils.setField(moteur, "partie", partie);
	}

	@Test
	public void couleurContraire() {

		when(mouvementService.couleurContraire(eq(Couleur.Noir))).thenReturn(Couleur.Blanc);

		// methode testée
		Couleur res = moteur.couleurContraire(Couleur.Noir);

		// vérifications
		assertEquals(Couleur.Blanc, res);

		verify(mouvementService).couleurContraire(eq(Couleur.Noir));

		verifyNoMoreInteractions(mouvementService);
		verifyNoMoreInteractions(etatService);
	}


	@Test
	public void getMovablePieces() {

		List<Position2> liste = new ArrayList<>();

		when(mouvementService.getMovablePieces(any(Plateau.class), eq(Couleur.Blanc))).thenReturn(liste);

		// methode testée
		List<Position2> res = moteur.getMovablePieces(Couleur.Blanc);

		// vérifications
		assertNotNull(res);
		assertEquals(liste, res);

		verify(mouvementService).getMovablePieces(any(Plateau.class), eq(Couleur.Blanc));

		verifyNoMoreInteractions(mouvementService);
		verifyNoMoreInteractions(etatService);
	}

	@Test
	public void listePieces() {

		List<Position2> liste = new ArrayList<>();

		when(mouvementService.listePieces(any(Plateau.class), eq(Couleur.Blanc))).thenReturn(liste);

		// methode testée
		List<Position2> res = moteur.listePieces(Couleur.Blanc);

		// vérifications
		assertNotNull(res);
		assertEquals(liste, res);

		verify(mouvementService).listePieces(any(Plateau.class), eq(Couleur.Blanc));

		verifyNoMoreInteractions(mouvementService);
		verifyNoMoreInteractions(etatService);
	}

	@Test
	public void listMove() {

		final Position2 position = createPosition(2, 5);
		final List<Position2> liste = new ArrayList<>();
		final boolean tousMouvementsRoi = true;

		when(mouvementService.listMove(any(Plateau.class), eq(position), eq(tousMouvementsRoi),
				eq(Couleur.Blanc))).thenReturn(liste);


		// methode testée
		List<Position2> res = moteur.listMove(position, tousMouvementsRoi);

		// vérifications
		assertNotNull(res);
		assertEquals(liste, res);

		verify(mouvementService).listMove(any(Plateau.class), eq(position), eq(tousMouvementsRoi),
				eq(Couleur.Blanc));

		verifyNoMoreInteractions(mouvementService);
		verifyNoMoreInteractions(etatService);
	}

	@Test
	public void calculEtatJeux() {

		final EtatJeux etatJeux = EtatJeux.MOUVEMENT_BLANC;

		when(etatService.calculEtatJeux(any(Partie.class))).thenReturn(etatJeux);

		// methode testée
		EtatJeux res = moteur.calculEtatJeux();

		// vérifications
		assertNotNull(res);
		assertEquals(etatJeux, res);

		verify(etatService).calculEtatJeux(any(Partie.class));
		verifyNoMoreInteractions(etatService);
		verifyNoMoreInteractions(mouvementService);
	}

	@Test
	public void move() {

		final Position2 src = createPosition(2, 5);
		final Position2 dest = createPosition(4, 5);

		Partie partie = Mockito.mock(Partie.class);
		ReflectionTestUtils.setField(moteur, "partie", partie);

		// methode testée
		moteur.move(src, dest);

		verify(partie).setMove(src, dest);

		verifyNoMoreInteractions(partie);
		verifyNoMoreInteractions(mouvementService);
		verifyNoMoreInteractions(etatService);
	}

	// methodes utilitaires

	private Position2 createPosition(int ligne, int colonne) {
		return PositionTools.getPosition(ligne, colonne);
	}
}