package org.chess.chess.moteur;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.outils.IteratorPlateau;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class CalculMouvementBisService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementsService.class);

    private CalculMouvementBaseService calculMouvementBaseService=new CalculMouvementBaseService();

    public ListeMouvements calculMouvements(Partie partie) {
        Preconditions.checkNotNull(partie);
        return calcul(partie.getPlateau(), partie.getJoueurCourant());
    }

    public ListeMouvements calcul(Plateau plateau, Couleur joueurCourant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);

        ListeMouvements resultat=new ListeMouvements();

        // recherche du roi du joueur courant
        Position positionRoi = rechercheRoi(plateau, joueurCourant);

        // vérification si le roi est en echec
        boolean roiEnEchec = roiEnEchecs(plateau, positionRoi, joueurAdversaire(joueurCourant));

        if (roiEnEchec) {
            // si echec, recherche des coup pour stoper echec

            ListeMouvements listeMouvement=rechercheMouvementStoperEchecRoi(plateau, joueurCourant, positionRoi);

            resultat=listeMouvement;
        } else {
            // si pas echec, recherche des coups possibles
            ListeMouvements listeMouvement = getPieceJoueur(plateau, joueurCourant);

            // pour chaque coup possible, verification si cela met le roi en echecs
            // la mise en echec, ne peut être fait que par tour (ligne, colonne), dame (ligne, colonne diagonale), fou (diagonale)
            suppressionMouvementMiseEnEchecsRoi(plateau, listeMouvement, positionRoi, joueurCourant);

            resultat=listeMouvement;
        }

        return resultat;
    }

    private ListeMouvements rechercheMouvementStoperEchecRoi(Plateau plateau, Couleur joueurCourant, Position positionRoi) {
        ListeMouvements listeMouvement = getPieceJoueur(plateau, joueurCourant);

        suppressionMouvementMiseEnEchecsRoi(plateau,listeMouvement,positionRoi,joueurCourant);

        return listeMouvement;
    }

    private void suppressionMouvementMiseEnEchecsRoi(Plateau plateau, ListeMouvements listeMouvement, Position positionRoi, Couleur joueurCourant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(listeMouvement);
        Preconditions.checkNotNull(positionRoi);
        Preconditions.checkNotNull(joueurCourant);

        Map<PieceCouleurPosition, List<Mouvement>> map = listeMouvement.getMapMouvements();
        var iter2=map.entrySet().iterator();
        while(iter2.hasNext()){
            var tmp=iter2.next();
            if(tmp.getKey().getPiece()!=Piece.ROI){
                var iter=tmp.getValue().iterator();
                Verify.verify(tmp.getKey().getCouleur()==joueurCourant);

                while(iter.hasNext()) {
                    var mouvement=iter.next();
                    Plateau plateauApresModification = new Plateau(plateau);
                    plateauApresModification.move(tmp.getKey().getPosition(), mouvement.getPosition());

                    if(caseAttaquee(plateauApresModification,positionRoi,joueurAdversaire(joueurCourant),true)){
                        iter.remove();
                    }
                }
            }
            if(tmp.getValue().isEmpty()){
                iter2.remove();
            }
        }

    }

    private Position rechercheRoi(Plateau plateau, Couleur joueurCourant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);
        List<Position> liste = getPositionsPieces(plateau, joueurCourant, Piece.ROI);
        Verify.verifyNotNull(liste);
        Verify.verify(liste.size() == 1);
        return liste.get(0);
    }

    private boolean roiEnEchecs(Plateau plateau, Position positionRoi, Couleur joueurCourant){
        return caseAttaquee(plateau, positionRoi, joueurAdversaire(joueurCourant), true);
    }

    public Couleur joueurAdversaire(Couleur couleur) {
        Preconditions.checkNotNull(couleur);
        if (couleur == Couleur.Blanc) {
            return Couleur.Noir;
        } else {
            return Couleur.Blanc;
        }
    }

    public ListeMouvements getPieceJoueur(Plateau plateau, Couleur joueur) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueur);

        ListeMouvements res = new ListeMouvements();

        Map<PieceCouleurPosition, List<Mouvement>> map = new HashMap<>();
        plateau.getStreamPosition()
                .filter(x -> x.getCouleur() == joueur)
                .forEach(pos -> {
                    List<Mouvement> liste2 = ajoute(plateau, pos);
                    if(!CollectionUtils.isEmpty(liste2)){
                        map.put(pos, liste2);
                    }
                });
        res.setMapMouvements(map);
        return res;
    }

    private List<Mouvement> ajoute(Plateau plateau, PieceCouleurPosition pos) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(pos);
        List<Mouvement> liste = calculMouvementBaseService.getMouvements(plateau, pos);
//        ListeMouvements liste2=new ListeMouvements();
//        Map<PieceCouleurPosition, List<Mouvement>> mapMouvements=new HashMap<>();
//        liste2.setMapMouvements(mapMouvements);
//        for(Mouvement m:liste){
//            PieceCouleurPosition p=new PieceCouleurPosition(pos.getPiece(),pos.getCouleur(),m.getPosition());
//            liste2.add(p);
//        }
        return liste;
    }

    public boolean caseAttaquee(Plateau plateau, Position position, Couleur couleurAttaquant, boolean testAttaqueEnPassant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(couleurAttaquant);

        return plateau.getStreamPosition()
                .filter(x -> x.getCouleur() == couleurAttaquant)
                .map(pos -> calculMouvementBaseService.getMouvements(plateau, pos))
                .anyMatch(x -> x.contains(position));
    }

    public Optional<PieceCouleur> getPiece(Plateau plateau, Position position) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        PieceCouleur piece = plateau.getCase(position);
        return Optional.ofNullable(piece);
    }

    public List<Position> getPositionsPieces(Plateau plateau, Couleur joueur, Piece piece) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueur);
        Preconditions.checkNotNull(piece);
        List<Position> liste = new ArrayList<>();
        for (Position pos : IteratorPlateau.getIterablePlateau()) {
            if (pos != null) {
                Optional<PieceCouleur> pieceOpt = getPiece(plateau, pos);
                if (pieceOpt.isPresent()) {
                    PieceCouleur p = pieceOpt.get();
                    if (p.getCouleur() == joueur && p.getPiece() == piece) {
                        liste.add(pos);
                    }
                }
            }
        }
        return liste;
    }
}
