package org.chess.chess.notation;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.joueur.JoueurHazard;
import org.chess.chess.outils.Check;
import org.chess.chess.outils.PositionTools;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.chess.chess.outils.IteratorPlateau.getIterablePlateau;

@Service
public class NotationCustom implements INotation {

	public Partie createPlateau(String str) {

		Preconditions.checkNotNull(str);
		Preconditions.checkArgument(!str.isEmpty());

		List<PieceCouleurPosition> listePieces = new ArrayList<>();

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

				PieceCouleurPosition pieceCouleur = new PieceCouleurPosition(piece, couleur, PositionTools.getPosition(ligne, colonne));
				Verify.verify(!contient(listePieces, pieceCouleur.getPosition()));
				listePieces.add(pieceCouleur);

				pos += 6;
			}
		}

		Plateau plateau = new Plateau(listePieces);

		return new Partie(plateau, //new JoueurHazard(Couleur.Blanc), new JoueurHazard(Couleur.Noir),
				Couleur.Blanc);
	}

	@Override
	public String serialize(Partie partie) {

		Preconditions.checkNotNull(partie);
		Plateau plateau = partie.getPlateau();
		Preconditions.checkNotNull(plateau);

		StringBuilder str = new StringBuilder();

		for (Position position : getIterablePlateau()) {
			PieceCouleur p = plateau.getCase(position);
			if (p != null) {
				str.append(p.getCouleur().getNomCourt());
				str.append(p.getPiece().getNomCourt());
				str.append(position.getRangee().getText());
				str.append('.');
				str.append(position.getColonne().getText());
				str.append(';');
			}
		}

		return str.toString();
	}

	private boolean contient(List<PieceCouleurPosition> listePieces, Position position) {
		return listePieces.stream()
				.anyMatch(x -> x.getPosition().equals(position));
	}
}
