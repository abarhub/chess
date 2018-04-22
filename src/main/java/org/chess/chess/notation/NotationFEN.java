package org.chess.chess.notation;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import org.chess.chess.domain.*;
import org.chess.chess.outils.PositionTools;
import org.chess.chess.service.InformationPartieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.chess.chess.domain.Plateau.NB_COLONNES;
import static org.chess.chess.domain.Plateau.NB_LIGNES;

@Service
public class NotationFEN implements INotation {

	@Autowired
	private InformationPartieService informationPartieService;

	public Partie createPlateau(String str) {

		Verify.verifyNotNull(str);
		Verify.verify(!str.isEmpty());

		Couleur joueurCourant = Couleur.Blanc;

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

		boolean roqueNoirDame = false;
		boolean roqueNoirRoi = false;
		boolean roqueBlancDame = false;
		boolean roqueBlancRoi = false;
		boolean priseEnPassant = false;
		int nbDemiCoup = 0;
		int nbCoup = 0;

		if (iterator.hasNext()) {
			// la couleur qui a le trait
			char c = getChar(iterator);
			if (c == ' ') {
				if (iterator.hasNext()) {
					c = getChar(iterator);
					if (Character.toUpperCase(c) == 'W') {
						joueurCourant = Couleur.Blanc;
					} else if (Character.toUpperCase(c) == 'N') {
						joueurCourant = Couleur.Noir;
					} else {
						throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu=w/n)");
					}
					// les roques possibles
					if (iterator.hasNext()) {
						c = getChar(iterator);
						if (c == ' ') {
							if (iterator.hasNext()) {
								c = getChar(iterator);
								if (c == '-') {

								} else {
									int nbCaractere = 0;
									do {
										if (c == 'K') {
											roqueBlancRoi = true;
										} else if (c == 'Q') {
											roqueNoirDame = true;
										} else if (c == 'k') {
											roqueNoirRoi = true;
										} else if (c == 'q') {
											roqueBlancDame = true;
										} else if (c == ' ') {
											iterator.previous();
											break;
										} else {
											throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu=KQkq)");
										}
										nbCaractere++;
									} while (iterator.hasNext() && nbCaractere < 4);
								}
							}
						} else {
							throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu= )");
						}
					}
					// prise en passant
					if (iterator.hasNext()) {
						c = getChar(iterator);
						if (c == ' ') {
							c = getChar(iterator);
							if (c == '-') {
								priseEnPassant = true;
							} else {
								throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere non gere)");
							}
						} else {
							throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu= )");
						}
					}
					// nombre de demi-coup depuis derniere capture
					if (iterator.hasNext()) {
						c = getChar(iterator);
						if (c == ' ') {
							int n = getNumber(iterator);
							nbDemiCoup = n;
						} else {
							throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu= )");
						}
					}
					// nombre de coup
					if (iterator.hasNext()) {
						c = getChar(iterator);
						if (c == ' ') {
							int n = getNumber(iterator);
							nbCoup = n;
						} else {
							throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu= )");
						}
					}
				}
			}
		}

		ConfigurationPartie configurationPartie = new ConfigurationPartie(joueurCourant);
		configurationPartie.setRoqueBlancRoi(roqueBlancRoi);
		configurationPartie.setRoqueBlancDame(roqueBlancDame);
		configurationPartie.setRoqueNoirRoi(roqueNoirRoi);
		configurationPartie.setRoqueNoirDame(roqueNoirDame);
		configurationPartie.setNbDemiCoupSansCapture(nbDemiCoup);
		configurationPartie.setNbCoup(nbCoup);

		Plateau plateau = new Plateau(listePieces);
		return new Partie(plateau, joueurCourant, informationPartieService.createInformationPartie(), configurationPartie);
	}

	private int getNumber(ListIterator<Character> iterator) {
		char c;
		int n = 0;
		if (iterator.hasNext()) {
			c = getChar(iterator);
			if (Character.isDigit(c)) {
				n = c - '0';
				if (iterator.hasNext()) {
					do {
						c = getChar(iterator);
						n = n * 10 + c - '0';
					} while (Character.isDigit(c) && iterator.hasNext());
					if (Character.isDigit(c)) {
						iterator.previous();
					}
				}
			} else {
				throw new IllegalArgumentException("Caractere '" + c + "' invalide à la position : " + iterator.previousIndex() + " (caractere attendu=0-9)");
			}
		}
		return n;
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
