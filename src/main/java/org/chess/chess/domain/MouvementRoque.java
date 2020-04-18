package org.chess.chess.domain;

public class MouvementRoque implements IMouvement {

    private final Position position;
    private final boolean roqueCoteRoi;
    private final Position positionTourSrc;
    private final Position positionTourDest;

    public MouvementRoque(Position position, boolean roqueCoteRoi, Position positionTourSrc, Position positionTourDest) {
        this.position = position;
        this.roqueCoteRoi = roqueCoteRoi;
        this.positionTourSrc = positionTourSrc;
        this.positionTourDest = positionTourDest;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isAttaque() {
        return false;
    }

    public boolean isRoqueCoteRoi() {
        return roqueCoteRoi;
    }

    public Position getPositionTourSrc() {
        return positionTourSrc;
    }

    public Position getPositionTourDest() {
        return positionTourDest;
    }
}
