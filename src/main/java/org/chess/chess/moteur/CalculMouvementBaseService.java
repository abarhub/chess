package org.chess.chess.moteur;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.outils.PositionTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CalculMouvementBaseService {


    public List<Mouvement> getMouvements(IPlateau plateau, PieceCouleurPosition piece) {
        List<Mouvement> list;
        list = null;
        switch (piece.getPiece()) {
            case PION:
                list = calculPion(piece, plateau);
                break;
            case CAVALIER:
                list = calculCavalier(piece, plateau);
                break;
            case FOU:
                list = calculFou(piece, plateau);
                break;
            case TOUR:
                list = calculTour(piece, plateau);
                break;
            case REINE:
                list = calculReine(piece, plateau);
                break;
            case ROI:
                list = calculRoi(piece, plateau);
                break;
        }
        return list;
    }

    private List<Mouvement> calculRoi(PieceCouleurPosition piece, IPlateau plateau) {

        Verify.verifyNotNull(piece);
        Verify.verify(piece.getPiece() == Piece.ROI);

        List<Mouvement> mouvements = new ArrayList<>();

        for (int ligne2 = -1; ligne2 <= 1; ligne2++) {
            for (int colonne2 = -1; colonne2 <= 1; colonne2++) {
                if (!(ligne2 == 0 && colonne2 == 0)) {
                    Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), ligne2, colonne2);
                    if (optPosition.isPresent()) {
                        ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
//						if (tousMouvementRois) {
//							ajoutePositionRois(plateau, liste, ligne3, colonne3, joueurCourant);
//						} else if (!caseAttaque(plateau, couleurContraire(piece.getCouleur()), ligne3, colonne3)) {
//							ajoutePositionRois(plateau, liste, ligne3, colonne3, joueurCourant);
//						}
                    }
                }
            }
        }

        return mouvements;
    }

    private List<Mouvement> calculReine(PieceCouleurPosition piece, IPlateau plateau) {

        Verify.verifyNotNull(piece);
        Verify.verify(piece.getPiece() == Piece.REINE);

        List<Mouvement> mouvements = new ArrayList<>();

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
            ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
        }

        return mouvements;
    }

    private List<Mouvement> calculTour(PieceCouleurPosition piece, IPlateau plateau) {

        Verify.verifyNotNull(piece);
        Verify.verify(piece.getPiece() == Piece.TOUR);

        List<Mouvement> mouvements = new ArrayList<>();

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
            ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
        }

        return mouvements;
    }

    private List<Mouvement> calculFou(PieceCouleurPosition piece, IPlateau plateau) {
        Verify.verifyNotNull(piece);
        Verify.verify(piece.getPiece() == Piece.FOU);

        List<Mouvement> mouvements = new ArrayList<>();

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
            ajouteDecalage(mouvements, piece.getPosition(), decalageLigne, decalageColonne, piece, plateau);
        }

        return mouvements;
    }


    private void ajouteDecalage(List<Mouvement> mouvements, Position position,
                                int decalageLigne, int decalageColonne,
                                PieceCouleurPosition piece, IPlateau plateau) {
        Verify.verifyNotNull(mouvements);
        Verify.verifyNotNull(piece);
        Verify.verifyNotNull(plateau);
        for (int i = 1; i <= 8; i++) {
            boolean res = false;
            Optional<Position> optPosition = PositionTools.getPosition(position, decalageLigne * i, decalageColonne * i);
            if (optPosition.isPresent()) {
                res = ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
            }
            if (!res) {
                break;
            }
        }
    }

    private List<Mouvement> calculCavalier(PieceCouleurPosition piece, IPlateau plateau) {
        Verify.verifyNotNull(piece);
        Verify.verify(piece.getPiece() == Piece.CAVALIER);

        List<Mouvement> mouvements = new ArrayList<>();

        Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), -2, -1);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), -2, 1);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), 1, -2);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), -1, -2);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), 1, 2);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), -1, 2);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), 2, -1);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), 2, 1);
        if (optPosition.isPresent()) {
            ajoutePositionPiece(mouvements, optPosition.get(), piece, plateau);
        }

        return mouvements;
    }

    private boolean ajoutePositionPiece(List<Mouvement> mouvements, Position position,
                                        PieceCouleurPosition piece, IPlateau plateau) {
        Verify.verifyNotNull(mouvements);
        Verify.verifyNotNull(piece);
        Verify.verifyNotNull(plateau);

        PieceCouleur caseCible = plateau.getCase(position);
        if (caseCible == null) {
            Mouvement mouvement = new Mouvement(position, false);
            mouvements.add(mouvement);
            return true;
        } else if (caseCible.getCouleur() != piece.getCouleur()) {
            Mouvement mouvement = new Mouvement(position, true);
            mouvements.add(mouvement);
            return false;
        }

        return false;
    }

    private List<Mouvement> calculPion(PieceCouleurPosition piece, IPlateau plateau) {
        Verify.verifyNotNull(piece);
        Verify.verify(piece.getPiece() == Piece.PION);

        List<Mouvement> mouvements = new ArrayList<>();

        int decalage, decalage2 = 0;
        if (piece.getCouleur() == Couleur.Blanc) {
            decalage = 1;
            if (piece.getPosition().getRangee() == RangeeEnum.RANGEE2) {
                decalage2 = 2;
            }
        } else {
            decalage = -1;
            if (piece.getPosition().getRangee() == RangeeEnum.RANGEE7) {
                decalage2 = -2;
            }
        }
        Optional<Position> optPosition = PositionTools.getPosition(piece.getPosition(), decalage, 0);
        if (optPosition.isPresent()) {
            ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, false);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), decalage, -1);
        if (optPosition.isPresent()) {
            ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, true);
        }
        optPosition = PositionTools.getPosition(piece.getPosition(), decalage, 1);
        if (optPosition.isPresent()) {
            ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, true);
        }
        if (decalage2 != 0) {
            PieceCouleur caseIntermediaire = null;
            if (decalage2 > 0) {
                optPosition = PositionTools.getPosition(piece.getPosition(), decalage2 - 1, 0);
                if (optPosition.isPresent()) {
                    caseIntermediaire = plateau.getCase(optPosition.get());
                }
            } else {
                optPosition = PositionTools.getPosition(piece.getPosition(), decalage2 + 1, 0);
                if (optPosition.isPresent()) {
                    caseIntermediaire = plateau.getCase(optPosition.get());
                }
            }
            if (caseIntermediaire == null) {
                optPosition = PositionTools.getPosition(piece.getPosition(), decalage2, 0);
                if (optPosition.isPresent()) {
                    ajoutePositionPions(mouvements, optPosition.get(), piece, plateau, false);
                }
            }
        }

        return mouvements;
    }

    private void ajoutePositionPions(List<Mouvement> mouvements, Position position,
                                     PieceCouleurPosition piece, IPlateau plateau, boolean mangePiece) {
        Verify.verifyNotNull(mouvements);
        Verify.verifyNotNull(piece);
        Verify.verifyNotNull(plateau);
        Verify.verifyNotNull(position);

        PieceCouleur caseCible = plateau.getCase(position);
        if (mangePiece) {
            if (caseCible != null && caseCible.getCouleur() != piece.getCouleur()) {
                Mouvement mouvement = new Mouvement(position, true);
                mouvements.add(mouvement);
            }
        } else {
            if (caseCible == null) {
                Mouvement mouvement = new Mouvement(position, false);
                mouvements.add(mouvement);
            }
        }
    }

}
