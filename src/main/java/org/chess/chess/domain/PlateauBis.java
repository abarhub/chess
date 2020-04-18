package org.chess.chess.domain;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;

public class PlateauBis extends Plateau {

    //private List<ImmutablePair<PieceCouleurPosition,PieceCouleurPosition>> listeMouvements=new ArrayList<>();
    private List<Position> mvtSrc=new ArrayList<>();
    private List<Position> mvtDest=new ArrayList<>();
    private List<PieceCouleur> pieceSrc=new ArrayList<>();
    private List<PieceCouleur> pieceDest=new ArrayList<>();

    public PlateauBis(Plateau plateau) {
        super(plateau);
    }

    public void move(Position positionSrc, Position positionDest){
        var src=getCase(positionSrc);
        var dest=getCase(positionDest);
        mvtSrc.add(positionSrc);
        mvtDest.add(positionDest);
        pieceSrc.add(src);
        pieceDest.add(dest);
        super.move(positionSrc,positionDest);
    }

    public void undo(){
        Preconditions.checkState(!mvtSrc.isEmpty());
        final int pos=mvtSrc.size()-1;
        Position positionSrc=mvtSrc.get(pos);
        Position positionDest=mvtDest.get(pos);
        PieceCouleur src=pieceSrc.get(pos);
        PieceCouleur dest=pieceDest.get(pos);
        setCase(positionSrc, src);
        setCase(positionDest, dest);
    }

}
