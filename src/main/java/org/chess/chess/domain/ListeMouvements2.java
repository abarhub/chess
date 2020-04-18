package org.chess.chess.domain;

import java.util.List;
import java.util.Map;

public class ListeMouvements2 {

    private Map<PieceCouleurPosition, List<IMouvement>> mapMouvements;

    public Map<PieceCouleurPosition, List<IMouvement>> getMapMouvements() {
        return mapMouvements;
    }

    public void setMapMouvements(Map<PieceCouleurPosition, List<IMouvement>> mapMouvements) {
        this.mapMouvements = mapMouvements;
    }
}
