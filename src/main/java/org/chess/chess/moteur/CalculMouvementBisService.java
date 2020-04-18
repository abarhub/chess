package org.chess.chess.moteur;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.lang3.time.StopWatch;
import org.chess.chess.domain.*;
import org.chess.chess.outils.IteratorPlateau;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class CalculMouvementBisService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementsService.class);

    private CalculMouvementBaseService calculMouvementBaseService=new CalculMouvementBaseService();

    private List<Duration> dureeListeMouvement=new ArrayList<>();

    private List<Duration> dureeListeMouvement2=new ArrayList<>();

    private List<Duration> dureeSuppressionEchecs=new ArrayList<>();

    private List<Duration> dureeTotal=new ArrayList<>();

    private org.springframework.util.StopWatch stopWatch=new org.springframework.util.StopWatch();
    private StopWatch stopWatch2=new StopWatch();
    private StopWatch stopWatchListeDeplacement=new StopWatch();
    private StopWatch stopWatchSupprEchecs=new StopWatch();
    private StopWatch stopWatchGenereDeplacement=new StopWatch();

    public ListeMouvements calculMouvements(Partie partie) {
        Preconditions.checkNotNull(partie);
        return calcul(partie.getPlateau(), partie.getJoueurCourant());
    }

    public ListeMouvements calcul(Plateau plateau, Couleur joueurCourant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);

        Instant debut=Instant.now();
        stopWatch.start();
        if(stopWatch2.isStopped()) {
            stopWatch2.start();
        } else {
            stopWatch2.resume();
        }
        ListeMouvements resultat;

        //PlateauBis plateau=new PlateauBis(plateau2);

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

        dureeTotal.add(Duration.between(debut,Instant.now()));
        stopWatch.stop();
        //stopWatch2.stop();
        stopWatch2.suspend();
        return resultat;
    }

    private ListeMouvements rechercheMouvementStoperEchecRoi(IPlateau plateau, Couleur joueurCourant, Position positionRoi) {
        ListeMouvements listeMouvement = getPieceJoueur(plateau, joueurCourant);

        suppressionMouvementMiseEnEchecsRoi(plateau,listeMouvement,positionRoi,joueurCourant);

        return listeMouvement;
    }

    private void suppressionMouvementMiseEnEchecsRoi(IPlateau plateau, ListeMouvements listeMouvement,
                                                     Position positionRoi, Couleur joueurCourant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(listeMouvement);
        Preconditions.checkNotNull(positionRoi);
        Preconditions.checkNotNull(joueurCourant);

        Instant debut=Instant.now();
        start(stopWatchSupprEchecs);

        if(false) {
            Map<PieceCouleurPosition, List<Mouvement>> map = listeMouvement.getMapMouvements();
            var iter2 = map.entrySet().iterator();
            while (iter2.hasNext()) {
                var tmp = iter2.next();
                if (tmp.getKey().getPiece() != Piece.ROI) {
                    if (mvtAVerifier(tmp.getKey().getPosition(), positionRoi, plateau)) {
                        var iter = tmp.getValue().iterator();
                        Verify.verify(tmp.getKey().getCouleur() == joueurCourant);
                        while (iter.hasNext()) {
                            var mouvement = iter.next();

                            if (plateau instanceof PlateauBis) {
                                PlateauBis plateauApresModification = (PlateauBis) plateau;
                                plateauApresModification.move(tmp.getKey().getPosition(), mouvement.getPosition());

                                if (roiAttaqueApresDeplacement(plateauApresModification, positionRoi, joueurAdversaire(joueurCourant))) {
                                    iter.remove();
                                }

                                plateauApresModification.undo();
                            } else {
                                IPlateau plateauApresModification = new Plateau((Plateau) plateau);
                                plateauApresModification.move(tmp.getKey().getPosition(), mouvement.getPosition());

                                if (roiAttaqueApresDeplacement(plateauApresModification, positionRoi, joueurAdversaire(joueurCourant))) {
                                    iter.remove();
                                }
                            }

                        }
                    }
                }
                if (tmp.getValue().isEmpty()) {
                    iter2.remove();
                }
            }
        } else {
            boolean tab[][]=new boolean[3][3];
            CasesATester casesATester;
            casesATester = analyseCases(plateau, positionRoi, joueurCourant, tab);
            if(casesATester!=null){
                Map<PieceCouleurPosition, List<Mouvement>> map = listeMouvement.getMapMouvements();
                var iter2 = map.entrySet().iterator();
                while (iter2.hasNext()) {
                    var tmp = iter2.next();
                    if (tmp.getKey().getPiece() != Piece.ROI) {
                        if (mvtAVerifier2(tmp.getKey().getPosition(), positionRoi, plateau, casesATester)) {
                            var iter = tmp.getValue().iterator();

                            Verify.verify(tmp.getKey().getCouleur() == joueurCourant);
                            while (iter.hasNext()) {
                                var mouvement = iter.next();

                                if (plateau instanceof PlateauBis) {
                                    PlateauBis plateauApresModification = (PlateauBis) plateau;
                                    plateauApresModification.move(tmp.getKey().getPosition(), mouvement.getPosition());

                                    if (roiAttaqueApresDeplacement(plateauApresModification, positionRoi, joueurAdversaire(joueurCourant))) {
                                        iter.remove();
                                    }

                                    plateauApresModification.undo();
                                } else {
                                    IPlateau plateauApresModification = new Plateau((Plateau) plateau);
                                    plateauApresModification.move(tmp.getKey().getPosition(), mouvement.getPosition());

                                    if (roiAttaqueApresDeplacement(plateauApresModification, positionRoi, joueurAdversaire(joueurCourant))) {
                                        iter.remove();
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

        dureeSuppressionEchecs.add(Duration.between(debut,Instant.now()));
        stop(stopWatchSupprEchecs);
    }

    private CasesATester analyseCases(IPlateau plateau, Position positionRoi, Couleur joueurCourant,
                                      boolean[][] tab) {
        CasesATester casesATester=new CasesATester();
        casesATester.tab=tab;
        boolean trouve=false;
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i!=0&&j!=0){
                    RangeeEnum r=RangeeEnum.get(positionRoi.getRangee().getNo()+i);
                    ColonneEnum c=ColonneEnum.get(positionRoi.getColonne().getNo()+j);
                    if(r!=null&&c!=null) {
                        Position p = new Position(r,c);
                        PieceCouleur p2 = plateau.getCase(p);
                        if(p2==null ||p2.getCouleur()==joueurCourant){
                            tab[i+1][j+1]=true;
                            trouve=true;
                            if(i==-1&&j==-1){
                                casesATester.gaucheBas=true;
                            } else if(i==0&&j==-1){
                                casesATester.centreBas=true;
                            } else if(i==1&&j==-1){
                                casesATester.droiteBas=true;
                            } else if(i==-1&&j==0){
                                casesATester.gaucheMillieux=true;
                            } else if(i==1&&j==0){
                                casesATester.droiteMillieux=true;
                            } else if(i==-1&&j==1){
                                casesATester.gaucheHaut=true;
                            } else if(i==0&&j==1){
                                casesATester.centreHaut=true;
                            } else if(i==1&&j==1){
                                casesATester.droiteHaute=true;
                            }
                        }
                    }
                }
            }
        }
        if(trouve) {
            return casesATester;
        } else {
            return null;
        }
    }

    private boolean mvtAVerifier(Position positionSrc, Position positionRoi, IPlateau plateau) {
        int x,y;
        if(positionSrc.getRangee()==positionRoi.getRangee()){
            // ils sont sur la même ligne
            int min=Math.min(positionSrc.getColonne().getNo(), positionRoi.getColonne().getNo());
            int max=Math.max(positionSrc.getColonne().getNo(), positionRoi.getColonne().getNo());
            if(min+1==max){
                return true;
            } else {
                for (int i = min+1; i < max; i++) {
                    PieceCouleur tmp = plateau.getCase(new Position(positionRoi.getRangee(), ColonneEnum.get(i)));
                    if(tmp!=null){
                        return false;
                    }
                }
                // il n'y a pas de piece entre les deux
                return true;
            }
        } else if(positionSrc.getColonne()==positionRoi.getColonne()){
            // ils sont sur la même colonne
            int min=Math.min(positionSrc.getRangee().getNo(), positionRoi.getRangee().getNo());
            int max=Math.max(positionSrc.getRangee().getNo(), positionRoi.getRangee().getNo());
            if(min+1==max){
                return true;
            } else {
                for (int i = min+1; i < max; i++) {
                    PieceCouleur tmp = plateau.getCase(new Position(RangeeEnum.get(i), positionRoi.getColonne()));
                    if(tmp!=null){
                        return false;
                    }
                }
                // il n'y a pas de piece entre les deux
                return true;
            }
        }
        x=positionRoi.getRangee().getNo()-positionSrc.getRangee().getNo();
        y=positionRoi.getColonne().getNo()-positionSrc.getColonne().getNo();
        if(Math.abs(x)==Math.abs(y)){
            // ils sont sur la même diagonale
            return true;
        } else {
            return false;
        }
    }

    private boolean mvtAVerifier2(Position positionSrc, Position positionRoi, IPlateau plateau, CasesATester tab) {
        int x,y;
//        if(positionSrc.getRangee()==positionRoi.getRangee()){
//            // ils sont sur la même ligne
//            int min=Math.min(positionSrc.getColonne().getNo(), positionRoi.getColonne().getNo());
//            int max=Math.max(positionSrc.getColonne().getNo(), positionRoi.getColonne().getNo());
//            if(min+1==max){
//                return true;
//            } else {
//                for (int i = min+1; i < max; i++) {
//                    PieceCouleur tmp = plateau.getCase(new Position(positionRoi.getRangee(), ColonneEnum.get(i)));
//                    if(tmp!=null){
//                        return false;
//                    }
//                }
//                // il n'y a pas de piece entre les deux
//                return true;
//            }
//        } else if(positionSrc.getColonne()==positionRoi.getColonne()){
//            // ils sont sur la même colonne
//            int min=Math.min(positionSrc.getRangee().getNo(), positionRoi.getRangee().getNo());
//            int max=Math.max(positionSrc.getRangee().getNo(), positionRoi.getRangee().getNo());
//            if(min+1==max){
//                return true;
//            } else {
//                for (int i = min+1; i < max; i++) {
//                    PieceCouleur tmp = plateau.getCase(new Position(RangeeEnum.get(i), positionRoi.getColonne()));
//                    if(tmp!=null){
//                        return false;
//                    }
//                }
//                // il n'y a pas de piece entre les deux
//                return true;
//            }
//        }
        x=positionRoi.getRangee().getNo()-positionSrc.getRangee().getNo();
        y=positionRoi.getColonne().getNo()-positionSrc.getColonne().getNo();
        if(x==0){
            // ils sont sur la même ligne
            return tab.gaucheMillieux || tab.droiteMillieux;
        } else if(y==0) {
            // ils sont sur la même colonne
            return tab.centreHaut || tab.centreBas;
        } else if(Math.abs(x)==Math.abs(y)){
            // ils sont sur la même diagonale
            return tab.gaucheHaut||tab.gaucheBas||tab.droiteHaute||tab.droiteBas;
        } else {
            return false;
        }
    }

    private boolean roiAttaqueApresDeplacement(IPlateau plateau, Position positionRoi, Couleur couleurAttaquant){
        if(false){
            return caseAttaquee(plateau,positionRoi,couleurAttaquant,false);
        } else {
            //Couleur couleurAttaquant=joueurAdversaire(joueurCourant);
            return plateau.getStreamPosition()
                    .filter(x -> x.getCouleur() == couleurAttaquant)
                    .filter(x->x.getPiece()==Piece.FOU || x.getPiece()==Piece.TOUR || x.getPiece()==Piece.REINE)
                    .map(pos -> calculMouvementBaseService.getMouvements(plateau, pos))
                    .anyMatch(x -> x.contains(positionRoi));
        }
    }

    private Position rechercheRoi(IPlateau plateau, Couleur joueurCourant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueurCourant);
        List<Position> liste = getPositionsPieces(plateau, joueurCourant, Piece.ROI);
        Verify.verifyNotNull(liste);
        Verify.verify(liste.size() == 1);
        return liste.get(0);
    }

    private boolean roiEnEchecs(IPlateau plateau, Position positionRoi, Couleur joueurCourant){
        if(true) {
            return caseAttaquee(plateau, positionRoi, joueurAdversaire(joueurCourant), true);
        } else {
            Couleur couleurAttaquant=joueurAdversaire(joueurCourant);
            return plateau.getStreamPosition()
                    .filter(x -> x.getCouleur() == couleurAttaquant)
                    .filter(x->x.getPiece()==Piece.FOU || x.getPiece()==Piece.TOUR || x.getPiece()==Piece.REINE)
                    .map(pos -> calculMouvementBaseService.getMouvements(plateau, pos))
                    .anyMatch(x -> x.contains(positionRoi));
        }
    }

    public Couleur joueurAdversaire(Couleur couleur) {
        Preconditions.checkNotNull(couleur);
        if (couleur == Couleur.Blanc) {
            return Couleur.Noir;
        } else {
            return Couleur.Blanc;
        }
    }

    public ListeMouvements getPieceJoueur(IPlateau plateau, Couleur joueur) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(joueur);

        Instant debut=Instant.now();
        start(stopWatchListeDeplacement);
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

        dureeListeMouvement.add(Duration.between(debut,Instant.now()));
        stop(stopWatchListeDeplacement);
        return res;
    }

    private List<Mouvement> ajoute(IPlateau plateau, PieceCouleurPosition pos) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(pos);

        Instant debut=Instant.now();
        start(stopWatchGenereDeplacement);
        List<Mouvement> liste = calculMouvementBaseService.getMouvements(plateau, pos);

        dureeListeMouvement2.add(Duration.between(debut,Instant.now()));
        stop(stopWatchGenereDeplacement);
        return liste;
    }

    public boolean caseAttaquee(IPlateau plateau, Position position, Couleur couleurAttaquant, boolean testAttaqueEnPassant) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(couleurAttaquant);

        return plateau.getStreamPosition()
                .filter(x -> x.getCouleur() == couleurAttaquant)
                .map(pos -> calculMouvementBaseService.getMouvements(plateau, pos))
                .anyMatch(x -> x.contains(position));
    }

    public Optional<PieceCouleur> getPiece(IPlateau plateau, Position position) {
        Preconditions.checkNotNull(plateau);
        Preconditions.checkNotNull(position);
        PieceCouleur piece = plateau.getCase(position);
        return Optional.ofNullable(piece);
    }

    public List<Position> getPositionsPieces(IPlateau plateau, Couleur joueur, Piece piece) {
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

    public List<Duration> getDureeListeMouvement() {
        return dureeListeMouvement;
    }

    public List<Duration> getDureeListeMouvement2() {
        return dureeListeMouvement2;
    }

    public List<Duration> getDureeSuppressionEchecs() {
        return dureeSuppressionEchecs;
    }

    public List<Duration> getDureeTotal() {
        return dureeTotal;
    }

    public org.springframework.util.StopWatch getStopWatch() {
        return stopWatch;
    }

    public void setStopWatch(org.springframework.util.StopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    public StopWatch getStopWatch2() {
        return stopWatch2;
    }

    public void setStopWatch2(StopWatch stopWatch2) {
        this.stopWatch2 = stopWatch2;
    }

    private void start(StopWatch stopWatch){
        if(stopWatch.isStopped()){
            stopWatch.start();
        } else {
            stopWatch.resume();
        }
    }

    private void stop(StopWatch stopWatch){
        stopWatch.suspend();
    }

    public StopWatch getStopWatchListeDeplacement() {
        return stopWatchListeDeplacement;
    }

    public StopWatch getStopWatchSupprEchecs() {
        return stopWatchSupprEchecs;
    }

    public StopWatch getStopWatchGenereDeplacement() {
        return stopWatchGenereDeplacement;
    }

    class CasesATester {
        boolean gaucheHaut, gaucheMillieux,gaucheBas, centreHaut,centreBas,
                droiteHaute, droiteMillieux,droiteBas;
        boolean tab[][];

    }
}
