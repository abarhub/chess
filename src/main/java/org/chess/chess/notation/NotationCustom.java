package org.chess.chess.notation;

import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.outils.Check;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.chess.chess.domain.Plateau.NB_COLONNES;
import static org.chess.chess.domain.Plateau.NB_LIGNES;

@Service
public class NotationCustom implements INotation {

	public Plateau createPlateau(String str) {

		Verify.verifyNotNull(str);
		Verify.verify(!str.isEmpty());

		List<PieceCouleurPosition> listePieces = new ArrayList<>();

		//tableau = new PieceCouleur[NB_LIGNES][NB_COLONNES];

		if (str != null && !str.isEmpty()) {
			int pos = 0;
			while (pos < str.length()) {

				Couleur couleur = null;
				Piece piece = null;
				char c = str.charAt(pos);
				couleur = Couleur.getValue(c);
				Verify.verifyNotNull(couleur);
				char p = str.charAt(pos + 1);
				piece = Piece.getValue(p);
				Verify.verifyNotNull(piece);

				int ligne = str.charAt(pos + 2) - '0';
				Verify.verify(str.charAt(pos + 3) == '.');
				int colonne = str.charAt(pos + 4) - '0';
				Verify.verify(str.charAt(pos + 5) == ';');
				Check.isPositionValide(ligne, colonne);

				PieceCouleurPosition pieceCouleur = new PieceCouleurPosition(piece, couleur, new Position(ligne, colonne));
				Verify.verify(!contient(listePieces, pieceCouleur.getPosition()));
				listePieces.add(pieceCouleur);

				pos += 6;
			}
		}

		return new Plateau(listePieces);
	}

	@Override
	public String serialize(Plateau plateau) {
		StringBuilder str = new StringBuilder();

		for (int ligne = 0; ligne < NB_LIGNES; ligne++) {
			for (int colonne = 0; colonne < NB_COLONNES; colonne++) {
				PieceCouleur p = plateau.getCase(ligne, colonne);
				if (p != null) {
					str.append(p.getCouleur().getNomCourt());
					str.append(p.getPiece().getNomCourt());
					str.append(ligne);
					str.append('.');
					str.append(colonne);
					str.append(';');
				}
			}
		}

		return str.toString();
	}

	private boolean contient(List<PieceCouleurPosition> listePieces, Position position) {
		return listePieces.stream()
				.anyMatch(x -> x.getPosition().equals(position));
	}
}
