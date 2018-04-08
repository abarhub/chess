package org.chess.chess.domain;

import com.google.common.base.Verify;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListeMouvements {

	private boolean roiBlancEchecs;
	private boolean roiNoirEchecs;
	private boolean roiBlancEchecsMat;
	private boolean roiNoirEchecsMat;

	private Map<PieceCouleurPosition, List<Mouvement>> mapMouvements;

	public Map<PieceCouleurPosition, List<Mouvement>> getMapMouvements() {
		return mapMouvements;
	}

	public void setMapMouvements(Map<PieceCouleurPosition, List<Mouvement>> mapMouvements) {
		this.mapMouvements = mapMouvements;
	}

	public boolean isRoiBlancEchecs() {
		return roiBlancEchecs;
	}

	public void setRoiBlancEchecs(boolean roiBlancEchecs) {
		this.roiBlancEchecs = roiBlancEchecs;
	}

	public boolean isRoiNoirEchecs() {
		return roiNoirEchecs;
	}

	public void setRoiNoirEchecs(boolean roiNoirEchecs) {
		this.roiNoirEchecs = roiNoirEchecs;
	}

	public boolean isRoiBlancEchecsMat() {
		return roiBlancEchecsMat;
	}

	public void setRoiBlancEchecsMat(boolean roiBlancEchecsMat) {
		this.roiBlancEchecsMat = roiBlancEchecsMat;
	}

	public boolean isRoiNoirEchecsMat() {
		return roiNoirEchecsMat;
	}

	public void setRoiNoirEchecsMat(boolean roiNoirEchecsMat) {
		this.roiNoirEchecsMat = roiNoirEchecsMat;
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

	public List<Mouvement> getMouvements(PieceCouleur pieceCouleur) {
		Verify.verifyNotNull(pieceCouleur);

		List<Mouvement> liste = null;

		if (mapMouvements != null) {

			for (Map.Entry<PieceCouleurPosition, List<Mouvement>> res : mapMouvements.entrySet()) {
				if (res.getKey().getPiece() == pieceCouleur.getPiece()
						&& res.getKey().getCouleur() == pieceCouleur.getCouleur()) {
					return res.getValue();
				}
			}

		}

		return liste;
	}

	@Override
	public String toString() {
		return "ListeMouvements{" +
				"roiBlancEchecs=" + roiBlancEchecs +
				", roiNoirEchecs=" + roiNoirEchecs +
				", roiBlancEchecsMat=" + roiBlancEchecsMat +
				", roiNoirEchecsMat=" + roiNoirEchecsMat +
				", mapMouvements=" + mapMouvements +
				'}';
	}

	public boolean caseAttaque(Position position, Couleur couleur) {
		Verify.verifyNotNull(position);

		if (mapMouvements != null) {

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ListeMouvements)) return false;
		ListeMouvements that = (ListeMouvements) o;
		return roiBlancEchecs == that.roiBlancEchecs &&
				roiNoirEchecs == that.roiNoirEchecs &&
				roiBlancEchecsMat == that.roiBlancEchecsMat &&
				roiNoirEchecsMat == that.roiNoirEchecsMat &&
				Objects.equals(mapMouvements, that.mapMouvements);
	}

	@Override
	public int hashCode() {

		return Objects.hash(roiBlancEchecs, roiNoirEchecs, roiBlancEchecsMat, roiNoirEchecsMat, mapMouvements);
	}

}
