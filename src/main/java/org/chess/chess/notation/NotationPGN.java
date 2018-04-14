package org.chess.chess.notation;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.chess.chess.domain.DemiCoup;
import org.chess.chess.domain.DemiCoupDeplacement;
import org.chess.chess.domain.Partie;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class NotationPGN implements INotation {


	@Override
	public Partie createPlateau(String str) {
		throw new NotImplementedException();
		//return null;
	}

	@Override
	public String serialize(Partie partie) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("[Event \"?\"]\n");
		stringBuilder.append("[Site \"?\"]\n");
		stringBuilder.append("[Date \"????.??.??\"]\n");
		stringBuilder.append("[Round \"?\"]\n");
		stringBuilder.append("[White \"?\"]\n");
		stringBuilder.append("[Black \"?\"]\n");
		stringBuilder.append("[Result \"*\"]\n");
		stringBuilder.append("\n");

		List<DemiCoup> listeBlancs = partie.getListeCoupsBlancs();
		List<DemiCoup> listeNoirs = partie.getListeCoupsNoirs();

		if (CollectionUtils.isEmpty(listeBlancs)) {

		} else {

			StringBuilder coup = new StringBuilder();

			for (int noCoup = 0; noCoup < listeBlancs.size(); noCoup++) {

				coup.setLength(0);

				DemiCoup coupBlanc = listeBlancs.get(noCoup);

				coup.append(noCoup + 1);
				coup.append('.');

				coup.append(deplacement(coupBlanc));

				if (noCoup < listeNoirs.size()) {
					DemiCoup coupNoir = listeNoirs.get(noCoup);

					coup.append(' ');

					coup.append(deplacement(coupNoir));
				} else {
					coup.append('\n');
					stringBuilder.append(coup);
					break;
				}

				coup.append('\n');
				stringBuilder.append(coup);
			}

		}

		return stringBuilder.toString();
		//throw new NotImplementedException();
		//return null;
	}

	private StringBuilder deplacement(DemiCoup demiCoup) {
		Preconditions.checkNotNull(demiCoup);

		StringBuilder str = new StringBuilder();


		if (demiCoup instanceof DemiCoupDeplacement) {
			DemiCoupDeplacement demiCoup2 = (DemiCoupDeplacement) demiCoup;

			switch (demiCoup2.getPiece()) {
				case PION:
					if (demiCoup2.getDest().getColonne() == demiCoup2.getSrc().getColonne()) {
						str.append(demiCoup2.getDest());
					} else {
						Verify.verify(demiCoup2.isMangePiece());
						str.append(demiCoup2.getSrc().getColonne().getText());
						str.append('x');
						str.append(demiCoup2.getDest());
					}
					break;
				default:
					str.append(demiCoup2.getPiece().getNomCourtAnglais());
					if (demiCoup2.isMangePiece()) {
						str.append('x');
					}
					str.append(demiCoup2.getDest());
					break;
			}

			if (demiCoup.isEchec()) {
				str.append('+');
			} else if (demiCoup.isEchecEtMat()) {
				str.append("++");
			}

		} else {
			throw new NotImplementedException();
		}

		return str;
	}
}
