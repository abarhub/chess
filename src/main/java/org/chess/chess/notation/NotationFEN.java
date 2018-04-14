package org.chess.chess.notation;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import org.chess.chess.domain.*;
import org.chess.chess.joueur.JoueurHazard;
import org.chess.chess.outils.PositionTools;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.chess.chess.domain.Plateau.NB_COLONNES;
import static org.chess.chess.domain.Plateau.NB_LIGNES;

@Service
public class NotationFEN implements INotation {


	public Partie createPlateau(String str) {

		Verify.verifyNotNull(str);
		Verify.verify(!str.isEmpty());

		List<PieceCouleurPosition> listePieces = new ArrayList<>();

		ListIterator<Character> iterator = Lists.charactersOf(str).listIterator();


		for (int ligne = NB_LIGNES - 1; ligne >= 0; ligne--) {
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

			if (ligne > 0) {
				char c = getChar(iterator);
				if (c != '/') {
					throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu=/)");
				}
			}

		}
		Plateau plateau = new Plateau(listePieces);
		return new Partie(plateau, new JoueurHazard(Couleur.Blanc), new JoueurHazard(Couleur.Noir), Couleur.Blanc);
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
					PositionTools.getPosition(ligne, colonne)));
		} else {
			listePieces.add(new PieceCouleurPosition(piece, Couleur.Noir,
					PositionTools.getPosition(ligne, colonne)));
		}
	}

	public String serialize(Partie partie) {

		Preconditions.checkNotNull(partie);
		Plateau plateau = partie.getPlateau();
		Preconditions.checkNotNull(plateau);

		StringBuilder str = new StringBuilder();

		for (int ligne = NB_LIGNES - 1; ligne >= 0; ligne--) {

			int nbCaseVide = 0;

			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				PieceCouleur piece = plateau.getCase(PositionTools.getPosition(ligne, colonne));

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

			if (ligne > 0) {
				str.append('/');
			}
		}

		return str.toString();
	}

}
