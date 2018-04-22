package org.chess.chess.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Partie {

	private final Plateau plateau;
	//private final Joueur joueurBlanc;
	//private final Joueur joueurNoir;
	private final List<DemiCoup> listeCoupsBlancs;
	private final List<DemiCoup> listeCoupsNoirs;
	private final InformationPartie informationPartie;
	private final ConfigurationPartie configurationPartie;
	private Couleur joueurCourant;

	public Partie(Plateau plateau,
	              Couleur joueurCourant, InformationPartie informationPartie, ConfigurationPartie configurationPartie) {
		Preconditions.checkNotNull(plateau);
		Preconditions.checkNotNull(joueurCourant);
		Preconditions.checkNotNull(informationPartie);
		Preconditions.checkNotNull(configurationPartie);
		Preconditions.checkArgument(joueurCourant == configurationPartie.getJoueurTrait());
		this.plateau = plateau;
		this.joueurCourant = joueurCourant;
		listeCoupsBlancs = new ArrayList<>();
		listeCoupsNoirs = new ArrayList<>();
		this.informationPartie = informationPartie;
		this.configurationPartie = configurationPartie;
	}

	public Partie(Partie partie) {
		this(new Plateau(partie.getPlateau()), partie.getJoueurCourant(),
				partie.informationPartie, new ConfigurationPartie(partie.getConfigurationPartie()));
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public Couleur getJoueurCourant() {
		return joueurCourant;
	}

	public List<DemiCoup> getListeCoupsBlancs() {
		if (listeCoupsBlancs == null) {
			return ImmutableList.of();
		} else {
			return ImmutableList.copyOf(listeCoupsBlancs);
		}
	}

	public List<DemiCoup> getListeCoupsNoirs() {
		if (listeCoupsNoirs == null) {
			return ImmutableList.of();
		} else {
			return ImmutableList.copyOf(listeCoupsNoirs);
		}
	}

	public void setMove(DemiCoup demiCoup) {
		Preconditions.checkNotNull(demiCoup);
		if (demiCoup instanceof DemiCoupDeplacement) {
			setMove(((DemiCoupDeplacement) demiCoup).getSrc(), ((DemiCoupDeplacement) demiCoup).getDest());
		} else if (demiCoup instanceof DemiCoupRoque) {
			setRoque(((DemiCoupRoque) demiCoup).getSrc(), ((DemiCoupRoque) demiCoup).getDest());
		} else {
			throw new NotImplementedException();
		}
	}

	public void setMove(Position src, Position dest) {

		verificationDeplacementCommun(src, dest);

		PieceCouleur pieceSource;
		pieceSource = plateau.getCase(src);

		PieceCouleur pieceDestination = plateau.getCase(dest);

		if (pieceDestination != null) {
			Verify.verify(pieceDestination.getCouleur() != joueurCourant);
			Verify.verify(pieceDestination.getPiece() != Piece.ROI);
		}

		boolean mangePiece = pieceDestination != null;
		Piece promotion = null;
		boolean echec = false;
		boolean echecEtMat = false;

		plateau.move(src, dest);

		//echec=calculEchecs(plateau,couleurContraire(joueurCourant));

		DemiCoupDeplacement demiCoupDeplacement = new DemiCoupDeplacement(pieceSource.getPiece(),
				src, dest, mangePiece, promotion, echec, echecEtMat);

		if (joueurCourant == Couleur.Blanc) {
			listeCoupsBlancs.add(demiCoupDeplacement);
			joueurCourant = Couleur.Noir;
		} else {
			listeCoupsNoirs.add(demiCoupDeplacement);
			joueurCourant = Couleur.Blanc;
		}
	}

//	private boolean calculEchecs(Plateau plateau, Couleur joueur) {
//		CalculMouvementsService calculMouvementsService=new CalculMouvementsService();
//		calculMouvementsService.caseAttaque(new Partie(plateau,joueur,new InformationPartie()),
//				couleurContraire(joueur),);
//	}

	public void setRoque(Position src, Position dest) {

		verificationDeplacementCommun(src, dest);

		PieceCouleur pieceSource;
		pieceSource = plateau.getCase(src);

		Verify.verifyNotNull(pieceSource);
		Verify.verify(pieceSource.getPiece() == Piece.ROI);
		Verify.verify(pieceSource.getCouleur() == joueurCourant);
		if (joueurCourant == Couleur.Blanc) {
			Verify.verify(src.getRangee() == RangeeEnum.RANGEE1);
		} else {
			Verify.verify(src.getRangee() == RangeeEnum.RANGEE8);
		}
		Verify.verify(src.getColonne() == ColonneEnum.COLONNEE);

		Verify.verify(dest.getRangee() == dest.getRangee());

		boolean echec = false;
		boolean echecEtMat = false;

		plateau.move(src, dest);

		// TODO: faire les verifications + deplacement de la tour
		if (true) {
			throw new UnsupportedOperationException("Le roque n'est pas implementé");
		}

		DemiCoupRoque demiCoupRoque = new DemiCoupRoque(src, dest, echec, echecEtMat);

		if (joueurCourant == Couleur.Blanc) {
			listeCoupsBlancs.add(demiCoupRoque);
			joueurCourant = Couleur.Noir;
		} else {
			listeCoupsNoirs.add(demiCoupRoque);
			joueurCourant = Couleur.Blanc;
		}
	}

	public Couleur couleurContraire(Couleur couleur) {
		Verify.verifyNotNull(couleur);
		if (couleur == Couleur.Blanc) {
			return Couleur.Noir;
		} else {
			return Couleur.Blanc;
		}
	}

	private void verificationDeplacementCommun(Position src, Position dest) {

		Verify.verifyNotNull(src);
		Verify.verifyNotNull(dest);
		Verify.verify(!src.equals(dest));

		PieceCouleur pieceSource;
		pieceSource = plateau.getCase(src);

		Verify.verifyNotNull(pieceSource, "la piece source n'existe pas (pos=" + src + ")");
		Verify.verify(pieceSource.getCouleur() == joueurCourant,
				"la piece source n'est pas de la couleur du joueur " +
						"qui doit jouer (" + pieceSource + "<>" + joueurCourant + ")");
	}

	public InformationPartie getInformationPartie() {
		return informationPartie;
	}

	public ConfigurationPartie getConfigurationPartie() {
		return configurationPartie;
	}
}
