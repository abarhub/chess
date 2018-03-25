package org.chess.chess.notation;

import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import org.chess.chess.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.chess.chess.domain.Plateau.NB_COLONNES;
import static org.chess.chess.domain.Plateau.NB_LIGNES;

@Service
public class NotationFEN implements INotation {


	public Plateau createPlateau(String str) {

		Verify.verifyNotNull(str);
		Verify.verify(!str.isEmpty());

		List<PieceCouleurPosition> listePieces = new ArrayList<>();

		//if (notation == NotationEnum.FEN) {
		//tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];

		//if (str != null && !str.isEmpty()) {
		//int pos = 0;
		//}
//		} else {
//			throw new IllegalArgumentException("Type de notation non géré : " + notation);
//		}

		ListIterator<Character> iterator = Lists.charactersOf(str).listIterator();


		for (int ligne = 0; ligne < NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				char c = getChar(iterator);
				if (estPiece(c, Piece.ROI)) {
					ajouteCase(listePieces, c, Piece.ROI, ligne, colonne);
				} else if (estPiece(c, Piece.REINE)) {
					ajouteCase(listePieces, c, Piece.REINE, ligne, colonne);
				} else if (estPiece(c, Piece.FOU)) {
					ajouteCase(listePieces, c, Piece.FOU, ligne, colonne);
				} else if (estPiece(c, Piece.CAVALIER)) {
					ajouteCase(listePieces, c, Piece.CAVALIER, ligne, colonne);
				} else if (estPiece(c, Piece.TOUR)) {
					ajouteCase(listePieces, c, Piece.TOUR, ligne, colonne);
				} else if (estPiece(c, Piece.PION)) {
					ajouteCase(listePieces, c, Piece.PION, ligne, colonne);
				} else if (Character.isDigit(c)) {
					int nbCasesVides = c - '0';
					if (nbCasesVides == 0 || nbCasesVides > 8) {
						throw new IllegalArgumentException("Le nombre de cases vide est invalide à la position : " + iterator.previousIndex());
					}
					colonne += nbCasesVides - 1;
				} else {
					throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex());
				}
			}

			if (ligne < 7) {
				char c = getChar(iterator);
				if (c != '/') {
					throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu=/)");
				}
			}

		}

		return new Plateau(listePieces);
	}

	private char getChar(ListIterator<Character> iterator) {
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			throw new IllegalArgumentException("Un caractere est attendu à la position : " + iterator.nextIndex());
		}
	}

	private boolean estPiece(char c, Piece p) {
		Verify.verifyNotNull(p);

		return Character.toUpperCase(c) == Character.toUpperCase(p.getNomCourtAnglais());
	}

	private boolean estBlanc(char c) {
		return Character.isUpperCase(c);
	}

	private void ajouteCase(List<PieceCouleurPosition> listePieces,
	                        char c, Piece piece, int ligne, int colonne) {
		if (estBlanc(c)) {
			listePieces.add(new PieceCouleurPosition(piece, Couleur.Blanc,
					new Position(ligne, colonne)));
		} else {
			listePieces.add(new PieceCouleurPosition(piece, Couleur.Noir,
					new Position(ligne, colonne)));
		}
	}

	public String serialize(Plateau plateau) {

		StringBuilder str = new StringBuilder();

		for (int ligne = 0; ligne < NB_LIGNES; ligne++) {

			int nbCaseVide = 0;

			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				PieceCouleur piece = plateau.getCase(new Position(ligne, colonne));

				if (piece == null) {
					nbCaseVide++;
				} else {
					if (nbCaseVide > 0) {
						str.append(nbCaseVide);
						nbCaseVide = 0;
					}

					if (piece.getCouleur() == Couleur.Blanc) {
						str.append(Character.toUpperCase(piece.getPiece().getNomCourtAnglais()));
					} else {
						str.append(Character.toLowerCase(piece.getPiece().getNomCourtAnglais()));
					}
				}

			}
			if (nbCaseVide > 0) {
				str.append(nbCaseVide);
			}

			if (ligne < 7) {
				str.append('/');
			}
		}

		return str.toString();
	}

}
