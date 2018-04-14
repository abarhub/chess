package org.chess.chess.domain;

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Partie {

	private final Plateau plateau;
	//private final Joueur joueurBlanc;
	//private final Joueur joueurNoir;
	private final List<DemiCoup> listeCoupsBlancs;
	private final List<DemiCoup> listeCoupsNoirs;
	private Couleur joueurCourant;

	public Partie(Plateau plateau, //Joueur joueurBlanc, Joueur joueurNoir,
	              Couleur joueurCourant) {
		Verify.verifyNotNull(plateau);
		//Verify.verifyNotNull(joueurBlanc);
		//Verify.verifyNotNull(joueurNoir);
		Verify.verifyNotNull(joueurCourant);
		this.plateau = plateau;
		//this.joueurBlanc = joueurBlanc;
		//this.joueurNoir = joueurNoir;
		this.joueurCourant = joueurCourant;
		listeCoupsBlancs = new ArrayList<>();
		listeCoupsNoirs = new ArrayList<>();
	}

	public Plateau getPlateau() {
		return plateau;
	}

//	public Joueur getJoueurBlanc() {
//		return joueurBlanc;
//	}
//
//	public Joueur getJoueurNoir() {
//		return joueurNoir;
//	}

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

	private void verificationDeplacementCommun(Position src, Position dest) {

		Verify.verifyNotNull(src);
		Verify.verifyNotNull(dest);
		Verify.verify(!src.equals(dest));

		PieceCouleur pieceSource;
		pieceSource = plateau.getCase(src);

		Verify.verifyNotNull(pieceSource, "la piece source n'existe pas");
		Verify.verify(pieceSource.getCouleur() == joueurCourant,
				"la piece source n'est pas de la couleur du joueur qui doit jouer");
	}
}
