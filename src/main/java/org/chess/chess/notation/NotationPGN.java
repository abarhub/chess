package org.chess.chess.notation;

import org.chess.chess.domain.DemiCoup;
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
		stringBuilder.append("[White \"Calistri, Tristan\"]\n");
		stringBuilder.append("[Black \"Bauduin, Etienne\"]\n");
		stringBuilder.append("[Result \"*\"]\n");
		stringBuilder.append("\n");

		List<DemiCoup> listeBlancs = partie.getListeCoupsBlancs();
		List<DemiCoup> listeNoirs = partie.getListeCoupsNoirs();

		if(CollectionUtils.isEmpty(listeBlancs)){

		} else {

			StringBuilder coup=new StringBuilder();

			for(int noCoup=0;noCoup<listeBlancs.size();noCoup++){

				coup.setLength(0);

				DemiCoup coupBlanc=listeBlancs.get(noCoup);

				coup.append(noCoup+1);
				coup.append('.');



				DemiCoup coupNoir=null;
			}
		}

		//return stringBuilder.toString();
		throw new NotImplementedException();
		//return null;
	}
}
