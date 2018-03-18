package org.chess.chess.domain;

import com.google.common.base.Verify;

import java.util.List;
import java.util.Map;

public class ListeMouvements {

	private Map<PieceCouleurPosition, List<Mouvement>> mapMouvements;

	public Map<PieceCouleurPosition, List<Mouvement>> getMapMouvements() {
		return mapMouvements;
	}

	public void setMapMouvements(Map<PieceCouleurPosition, List<Mouvement>> mapMouvements) {
		this.mapMouvements = mapMouvements;
	}

	public List<Mouvement> getMouvements(Position position) {
		Verify.verifyNotNull(position);

		List<Mouvement> liste = null;

		if (mapMouvements != null) {

			for (Map.Entry<PieceCouleurPosition, List<Mouvement>> res : mapMouvements.entrySet()) {
				if (res.getKey().getPosition().equals(position)) {
					return res.getValue();
				}
			}
		}

		return liste;
	}

	@Override
	public String toString() {
		return "ListeMouvements{" +
				"mapMouvements=" + mapMouvements +
				'}';
	}

	public boolean caseAttaque(int ligne, int colonne, Couleur couleur) {

		if (mapMouvements != null) {

			Position position = new Position(ligne, colonne);

			for (Map.Entry<PieceCouleurPosition, List<Mouvement>> res : mapMouvements.entrySet()) {
				if (res.getKey().getCouleur() != couleur) {
					for (Mouvement mouvement : res.getValue()) {
						if (mouvement.getPosition().equals(position)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}
}
