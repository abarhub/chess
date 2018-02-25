package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.evaluateur.ShannonEval;
import org.chess.chess.joueur.Joueur;
import org.chess.chess.joueur.JoueurHazard;
import org.chess.chess.joueur.JoueurNegaMax;
import org.chess.chess.outils.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Moteur {

	public static final Logger LOGGER = LoggerFactory.getLogger(Moteur.class);

	private Plateau plateau;

	private Couleur joueurCourant;

	private Joueur joueurBlanc;

	private Joueur joueurNoir;

	//private Random random = new Random(System.currentTimeMillis());

	private EtatJeux etatJeux;

	@PostConstruct
	public void initialise() {
		LOGGER.info("initialisation du moteur ...");

		Plateau plateau;
		Couleur joueurCourant;
		Joueur joueurBlanc, joueurNoir;

		if (false) {
			plateau = new Plateau();
			plateau.initialise();
			plateau.afficheConsole();
		} else {
			String str;
			str = "NT0.0;NF0.2;ND0.3;NR0.4;NF0.5;NT0.7;NP1.0;NP1.1;NP1.2;NP1.3;NP1.4;NP1.5;NP1.6;NP1.7;NC2.0;BP3.5;BP5.1;BC5.7;BP6.0;NC6.6;BP6.7;BT7.0;BF7.2;BR7.4;BT7.7;";
			plateau = new Plateau(str);
			plateau.afficheConsole();
		}

		joueurCourant = Couleur.Blanc;

		joueurBlanc = new JoueurHazard(Couleur.Blanc);
		//joueurNoir = new JoueurHazard(Couleur.Noir);
		joueurNoir = new JoueurNegaMax(Couleur.Noir, 1, new ShannonEval());

		initialise(plateau, joueurCourant, joueurBlanc, joueurNoir);

		LOGGER.info("initialisation du moteur OK");
	}

	public void initialise(Plateau plateau, Couleur joueur, Joueur joueurBlanc, Joueur joueurNoir) {
		Verify.verifyNotNull(plateau);
		Verify.verifyNotNull(joueur);
		Verify.verifyNotNull(joueurBlanc);
		Verify.verifyNotNull(joueurNoir);

		this.plateau = plateau;
		this.joueurCourant = joueur;
		this.joueurBlanc = joueurBlanc;
		this.joueurNoir = joueurNoir;
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public void nextMove() {

		if (joueurCourant == Couleur.Blanc) {
			joueurBlanc.nextMove(this);
			joueurCourant = Couleur.Noir;
		} else {
			joueurNoir.nextMove(this);
			joueurCourant = Couleur.Blanc;
		}

		LOGGER.info("plateau={}", plateau.getRepresentation());

		etatJeux = calculEtatJeux();
		LOGGER.info("etat={}", etatJeux);

	}

	public Couleur couleurContraire(Couleur couleur) {
		Verify.verifyNotNull(couleur);
		if (couleur == Couleur.Blanc) {
			return Couleur.Noir;
		} else {
			return Couleur.Blanc;
		}
	}

	public List<Position> getMovablePieces(Couleur joueur) {
		Verify.verifyNotNull(joueur);
		List<Position> listePieces;
		List<Position> liste = listePieces(joueur);
		listePieces = new ArrayList<>();
		for (Position p : liste) {
			List<Position> liste2 = listMove(p, false);
			if (liste2 != null && !liste2.isEmpty()) {
				listePieces.add(p);
			}
		}
		return listePieces;
	}

	public List<Position> listePieces(Couleur couleur) {
		Verify.verifyNotNull(couleur);
		List<Position> liste = new ArrayList<>();
		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				PieceCouleur piece = plateau.getCase(i, j);
				if (piece != null && piece.getCouleur() == couleur) {
					liste.add(new Position(i, j));
				}
			}
		}
		return liste;
	}

	private boolean caseAttaque(Couleur couleur, int ligne, int colonne) {
		Verify.verifyNotNull(couleur);
		Check.checkLigneColonne(ligne, colonne);
		LOGGER.info("caseAttaque:debut({},{},{})", couleur, ligne, colonne);
		List<Position> pieces = listePieces(couleurContraire(couleur));

		LOGGER.info("pieces:{}", pieces);

		if (pieces != null) {
			Position positionRecherche = new Position(ligne, colonne);
			for (Position p : pieces) {
				if (p.getLigne() != ligne && p.getColonne() != colonne) {
					List<Position> liste = listMove(p, true);

					LOGGER.info("p:{} => listMove:{}", p, liste);

					if (liste != null) {
						if (liste.contains(positionRecherche)) {
							LOGGER.info("caseAttaque:fin=true");
							return true;
						}
					}
				}
			}
		}
		LOGGER.info("caseAttaque:fin=false");

		return false;
	}

	public List<Position> listMove(Position position, boolean tousMouvementRois) {
		return listMove(position.getLigne(), position.getColonne(), tousMouvementRois);
	}

	private List<Position> listMove(int ligne, int colonne, boolean tousMouvementRois) {
		Check.checkLigneColonne(ligne, colonne);
		List<Position> liste = new ArrayList<>();

		PieceCouleur piece = plateau.getCase(ligne, colonne);
		if (piece != null) {
			if (piece.getPiece() == Piece.PION) {
				int decalage, decalage2 = 0;
				if (piece.getCouleur() == Couleur.Blanc) {
					decalage = -1;
					if (ligne == 6) {
						decalage2 = -2;
					}
				} else {
					decalage = 1;
					if (ligne == 1) {
						decalage2 = 2;
					}
				}
				ajoutePositionPions(liste, ligne + decalage, colonne,
						piece.getCouleur(), false);
				ajoutePositionPions(liste, ligne + decalage, colonne - 1,
						piece.getCouleur(), true);
				ajoutePositionPions(liste, ligne + decalage, colonne + 1,
						piece.getCouleur(), true);
				if (decalage2 != 0) {
					ajoutePositionPions(liste, ligne + decalage2, colonne,
							piece.getCouleur(), false);
				}
			} else if (piece.getPiece() == Piece.CAVALIER) {
				ajoutePositionPiece(liste, ligne - 2, colonne - 1,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne - 2, colonne + 1,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne + 1, colonne - 2,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne - 1, colonne - 2,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne + 1, colonne + 2,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne - 1, colonne + 2,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne + 2, colonne - 1,
						piece.getCouleur());
				ajoutePositionPiece(liste, ligne + 2, colonne + 1,
						piece.getCouleur());
			} else if (piece.getPiece() == Piece.FOU) {
				for (int j = 0; j < 4; j++) {
					int decalageLigne, decalageColonne;
					if (j == 0) {
						decalageLigne = 1;
						decalageColonne = 1;
					} else if (j == 1) {
						decalageLigne = 1;
						decalageColonne = -1;
					} else if (j == 2) {
						decalageLigne = -1;
						decalageColonne = 1;
					} else {
						decalageLigne = -1;
						decalageColonne = -1;
					}
					ajouteDecalage(liste, ligne, colonne, decalageLigne, decalageColonne, piece.getCouleur());
				}
			} else if (piece.getPiece() == Piece.TOUR) {
				for (int j = 0; j < 4; j++) {
					int decalageLigne, decalageColonne;
					if (j == 0) {
						decalageLigne = 1;
						decalageColonne = 0;
					} else if (j == 1) {
						decalageLigne = 0;
						decalageColonne = 1;
					} else if (j == 2) {
						decalageLigne = -1;
						decalageColonne = 0;
					} else {
						decalageLigne = 0;
						decalageColonne = -1;
					}
					ajouteDecalage(liste, ligne, colonne, decalageLigne, decalageColonne, piece.getCouleur());
				}
			} else if (piece.getPiece() == Piece.REINE) {
				for (int j = 0; j < 8; j++) {
					int decalageLigne, decalageColonne;
					if (j == 0) {
						decalageLigne = 1;
						decalageColonne = 0;
					} else if (j == 1) {
						decalageLigne = 0;
						decalageColonne = 1;
					} else if (j == 2) {
						decalageLigne = -1;
						decalageColonne = 0;
					} else if (j == 3) {
						decalageLigne = 0;
						decalageColonne = -1;
					} else if (j == 4) {// diagonales
						decalageLigne = 1;
						decalageColonne = 1;
					} else if (j == 5) {
						decalageLigne = 1;
						decalageColonne = -1;
					} else if (j == 6) {
						decalageLigne = -1;
						decalageColonne = 1;
					} else {
						decalageLigne = -1;
						decalageColonne = -1;
					}
					ajouteDecalage(liste, ligne, colonne, decalageLigne, decalageColonne,
							piece.getCouleur());
				}
			} else if (piece.getPiece() == Piece.ROI) {
				for (int ligne2 = -1; ligne2 <= 1; ligne2++) {
					for (int colonne2 = -1; colonne2 <= 1; colonne2++) {
						if (!(ligne2 == 0 && colonne2 == 0)) {
							final int ligne3 = ligne + ligne2;
							final int colonne3 = colonne + colonne2;
							if (Check.isPositionValide(ligne3, colonne3)) {
								if (tousMouvementRois) {
									ajoutePositionRois(liste, ligne3, colonne3, joueurCourant);
								} else if (!caseAttaque(piece.getCouleur(), ligne3, colonne3)) {
									ajoutePositionRois(liste, ligne3, colonne3, joueurCourant);
								}
							}
						}
					}
				}
			}
		}

		return liste;
	}

	private void ajouteDecalage(List<Position> liste, int ligne, int colonne,
	                            int decalageLigne, int decalageColonne, Couleur couleur) {
		Verify.verifyNotNull(liste);
		Verify.verifyNotNull(couleur);
		for (int i = 1; i <= 8; i++) {
			boolean res = ajoutePositionPiece(liste, ligne + decalageLigne * i,
					colonne + decalageColonne * i,
					couleur);
			if (!res) {
				break;
			}
		}
	}

	private void ajoutePositionRois(List<Position> liste, int ligne, int colonne, Couleur couleur) {
		ajoutePosition(liste, ligne, colonne, couleur, true, false, true);
	}

	private void ajoutePositionPions(List<Position> liste, int ligne, int colonne,
	                                 Couleur couleur, boolean doitManger) {
		ajoutePosition(liste, ligne, colonne, couleur, false, doitManger, false);
	}

	private boolean ajoutePositionPiece(List<Position> liste, int ligne, int colonne,
	                                    Couleur couleur) {
		return ajoutePosition(liste, ligne, colonne, couleur, true, false, false);
	}

	private boolean ajoutePosition(List<Position> liste, int ligne, int colonne,
	                               Couleur couleur, boolean peutManger, boolean doitManger,
	                               boolean deplacementNonAttaquable) {
		Verify.verifyNotNull(liste);
		Verify.verifyNotNull(couleur);
		if (Check.isPositionValide(ligne, colonne)) {
//			if (deplacementNonAttaquable) {
//				if (caseAttaque(couleurContraire(couleur), ligne, colonne)) {
//					return false;
//				}
//			}
			if (doitManger) {
				PieceCouleur piece = plateau.getCase(ligne, colonne);
				if (piece != null && piece.getCouleur() != couleur) {
					liste.add(new Position(ligne, colonne));
					return true;
				}
			} else {
				PieceCouleur piece = plateau.getCase(ligne, colonne);
				if (piece == null) {
					liste.add(new Position(ligne, colonne));
					return true;
				} else if (peutManger && piece.getCouleur() != couleur) {
					liste.add(new Position(ligne, colonne));
					return false;
				}
			}
		}
		return false;
	}

	public EtatJeux calculEtatJeux() {
		List<PieceCouleurPosition> liste = plateau.getStreamPosition()
				.filter(x -> x.getPiece() == Piece.ROI
						//&& caseAttaque(couleurContraire(x.getCouleur()),
						//x.getPosition().getLigne(), x.getPosition().getColonne())
				)
				.collect(Collectors.toList());

		LOGGER.info("calculEtatJeux:liste={}", liste);

		for (PieceCouleurPosition p : liste) {
			if (caseAttaque(p.getCouleur(),
					p.getPosition().getLigne(), p.getPosition().getColonne())) {
				LOGGER.info("calculEtatJeux:attaque={}", p);
			} else {
				LOGGER.info("calculEtatJeux:pas attaque={}", p);
			}
		}

		if (liste == null || liste.isEmpty()) {
			return EtatJeux.EN_COURS;
		} else {
			PieceCouleurPosition p = liste.get(0);
			List<Position> liste2 = listMove(p.getPosition(), false);
			LOGGER.info("calculEtatJeux:liste2={}", liste2);
			if (liste2 == null || liste2.isEmpty()) {
				if (p.getCouleur() == Couleur.Blanc) {
					return EtatJeux.ECHECS_ET_MAT_BLANC;
				} else {
					return EtatJeux.ECHECS_ET_MAT_NOIR;
				}
			} else {
				if (p.getCouleur() == Couleur.Blanc) {
					return EtatJeux.ECHECS_BLANC;
				} else {
					return EtatJeux.ECHECS_NOIR;
				}
			}
		}
	}
}
