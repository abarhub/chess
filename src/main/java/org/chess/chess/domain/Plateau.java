package org.chess.chess.domain;

import com.google.common.base.Verify;
import org.chess.chess.outils.Check;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Plateau {

	public static final int NB_LIGNES = 8;
	public static final int NB_COLONNES = 8;

	private PieceCouleur[][] tableau;

	public Plateau() {

	}

	public Plateau(Plateau plateau) {
		tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];
		for (int lignes = 0; lignes < NB_LIGNES; lignes++) {
			for (int colonnes = 0; colonnes < NB_COLONNES; colonnes++) {
				tableau[lignes][colonnes] = plateau.tableau[lignes][colonnes];
			}
		}
	}

	public Plateau(String str) {
		tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];

		if (str != null && !str.isEmpty()) {
			int pos = 0;
			while (pos < str.length()) {

				Couleur couleur = null;
				Piece piece = null;
				char c = str.charAt(pos);
				couleur = Couleur.getValue(c);
				Verify.verifyNotNull(couleur);
				char p = str.charAt(pos + 1);
				piece = Piece.getValue(p);
				Verify.verifyNotNull(piece);

				int ligne = str.charAt(pos + 2) - '0';
				Verify.verify(str.charAt(pos + 3) == '.');
				int colonne = str.charAt(pos + 4) - '0';
				Verify.verify(str.charAt(pos + 5) == ';');
				Check.isPositionValide(ligne, colonne);

				PieceCouleur pieceCouleur = new PieceCouleur(piece, couleur);
				Verify.verify(tableau[ligne][colonne] == null);
				tableau[ligne][colonne] = pieceCouleur;

				pos += 6;
			}
		}
	}

	public void initialise() {
		tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];

		lignePieces(0, Couleur.Noir);
		lignePions(1, Couleur.Noir);


		lignePions(6, Couleur.Blanc);
		lignePieces(7, Couleur.Blanc);
	}

	private void lignePieces(int ligne, Couleur couleur) {
		setCase(ligne, 0, new PieceCouleur(Piece.TOUR, couleur));
		setCase(ligne, 1, new PieceCouleur(Piece.CAVALIER, couleur));
		setCase(ligne, 2, new PieceCouleur(Piece.FOU, couleur));
		setCase(ligne, 3, new PieceCouleur(Piece.REINE, couleur));
		setCase(ligne, 4, new PieceCouleur(Piece.ROI, couleur));
		setCase(ligne, 5, new PieceCouleur(Piece.FOU, couleur));
		setCase(ligne, 6, new PieceCouleur(Piece.CAVALIER, couleur));
		setCase(ligne, 7, new PieceCouleur(Piece.TOUR, couleur));
	}

	private void lignePions(int ligne, Couleur couleur) {
		for (int i = 0; i < NB_COLONNES; i++) {
			setCase(ligne, i, new PieceCouleur(Piece.PION, couleur));
		}
	}

	private void setCase(int ligne, int colonne, PieceCouleur pieceCouleur) {
		Verify.verify(ligne >= 0);
		Verify.verify(ligne < NB_LIGNES);
		Verify.verify(colonne >= 0);
		Verify.verify(colonne < NB_COLONNES);
		tableau[ligne][colonne] = pieceCouleur;
	}

	public PieceCouleur getCase(int ligne, int colonne) {
		Verify.verify(ligne >= 0);
		Verify.verify(ligne < NB_LIGNES);
		Verify.verify(colonne >= 0);
		Verify.verify(colonne < NB_COLONNES);
		return tableau[ligne][colonne];
	}

	public void move(Position positionSrc, Position positionDest) {
		Verify.verifyNotNull(positionSrc);
		Verify.verifyNotNull(positionDest);
		Verify.verify(!positionSrc.equals(positionDest));

		PieceCouleur p = getCase(positionSrc.getLigne(), positionSrc.getColonne());
		Verify.verifyNotNull(p, "La case source est vide");
		setCase(positionSrc.getLigne(), positionSrc.getColonne(), null);
		setCase(positionDest.getLigne(), positionDest.getColonne(), p);
	}

	public void afficheConsole() {
		for (int ligne = 0; ligne < NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				PieceCouleur p = getCase(ligne, colonne);
				if (p == null) {
					System.out.print(' ');
				} else {
					System.out.print(p.getPiece().getNomCourt());
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public Stream<PieceCouleur> getStream() {
		List<PieceCouleur> list = new ArrayList<>();

		for (int ligne = 0; ligne < NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				PieceCouleur p = getCase(ligne, colonne);
				if (p != null) {
					list.add(p);
				}
			}
		}

		return list.stream();
	}

	public Stream<PieceCouleurPosition> getStreamPosition() {
		List<PieceCouleurPosition> list = new ArrayList<>();

		for (int ligne = 0; ligne < NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				PieceCouleur p = getCase(ligne, colonne);
				if (p != null) {
					PieceCouleurPosition p2 = new PieceCouleurPosition(p.getPiece(), p.getCouleur(), new Position(ligne, colonne));
					list.add(p2);
				}
			}
		}

		return list.stream();
	}

	public String getRepresentation() {
		StringBuilder str = new StringBuilder();

		for (int ligne = 0; ligne < NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				PieceCouleur p = getCase(ligne, colonne);
				if (p != null) {
					str.append(p.getCouleur().getNomCourt());
					str.append(p.getPiece().getNomCourt());
					str.append(ligne);
					str.append('.');
					str.append(colonne);
					str.append(';');
				}
			}
		}

		return str.toString();
	}

}
