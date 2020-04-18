package org.chess.chess.domain;

public class MouvementSimple implements IMouvement {

    private final Position position;
    private final boolean attaque;

    public MouvementSimple(Position position, boolean attaque) {
        this.position = position;
        this.attaque = attaque;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isAttaque() {
        return attaque;
    }
}
