package org.chess.chess.notation;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.chess.chess.domain.*;
import org.chess.chess.dto.DemiCoupDTO;
import org.chess.chess.dto.ListeDemiCoupDTO;
import org.chess.chess.moteur.CalculMouvementsService;
import org.chess.chess.moteur.Moteur;
import org.chess.chess.moteur.MouvementService;
import org.chess.chess.service.InformationPartieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotationPGN implements INotation {

	public static final Logger LOGGER = LoggerFactory.getLogger(NotationPGN.class);

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	@Autowired
	private CalculMouvementsService calculMouvementsService;

	@Autowired
	private MouvementService mouvementService;

	@Autowired
	private InformationPartieService informationPartieService;

	@Override
	public Partie createPlateau(String str) {
		throw new NotImplementedException();
		//return null;
	}

	@Override
	public String serialize(Partie partie) {
		Preconditions.checkNotNull(partie);
		StringBuilder stringBuilder = new StringBuilder();

		if (partie.getInformationPartie().getDate() != null) {

		}

		stringBuilder.append("[Event \"");
		stringBuilder.append(convStr(partie.getInformationPartie().getEvent()));
		stringBuilder.append("\"]\n");
		stringBuilder.append("[Site \"");
		stringBuilder.append(convStr(partie.getInformationPartie().getSite()));
		stringBuilder.append("\"]\n");
		stringBuilder.append("[Date \"");
		stringBuilder.append(convDate(partie.getInformationPartie().getDate()));
		stringBuilder.append("\"]\n");
		stringBuilder.append("[Round \"");
		stringBuilder.append(convStr(partie.getInformationPartie().getRound()));
		stringBuilder.append("\"]\n");
		stringBuilder.append("[White \"");
		stringBuilder.append(convStr(partie.getInformationPartie().getJoueurBlanc()));
		stringBuilder.append("\"]\n");
		stringBuilder.append("[Black \"");
		stringBuilder.append(convStr(partie.getInformationPartie().getJoueurNoir()));
		stringBuilder.append("\"]\n");
		stringBuilder.append("[Result \"*\"]\n");
		stringBuilder.append("\n");

		List<DemiCoup> listeBlancs = partie.getListeCoupsBlancs();
		List<DemiCoup> listeNoirs = partie.getListeCoupsNoirs();

		if (CollectionUtils.isEmpty(listeBlancs)) {

		} else {

			StringBuilder coup = new StringBuilder();

			Plateau plateau = new Plateau();
			plateau.initialise();

			Partie partie2 = new Partie(plateau, Couleur.Blanc, informationPartieService.createInformationPartie());

			for (int noCoup = 0; noCoup < listeBlancs.size(); noCoup++) {

				coup.setLength(0);

				Partie partieResultat;

				DemiCoup coupBlanc = listeBlancs.get(noCoup);

				partieResultat = checkCoupValide(partie2, coupBlanc, Couleur.Blanc);

				coup.append(noCoup + 1);
				coup.append('.');

				coup.append(deplacement(coupBlanc, partie2, Couleur.Blanc));

				if (noCoup < listeNoirs.size()) {
					DemiCoup coupNoir = listeNoirs.get(noCoup);

					partie2 = partieResultat;

					partieResultat = checkCoupValide(partie2, coupNoir, Couleur.Noir);

					coup.append(' ');

					coup.append(deplacement(coupNoir, partie2, Couleur.Noir));
				} else {
					coup.append('\n');
					stringBuilder.append(coup);
					break;
				}

				coup.append('\n');
				stringBuilder.append(coup);

				partie2 = partieResultat;
			}

		}

		return stringBuilder.toString();
	}

	private String convDate(LocalDate date) {
		if (date == null) {
			return "????.??.??";
		} else {
			return date.format(formatter);
		}
	}

	private String convStr(String s) {
		if (s == null || s.trim().isEmpty()) {
			return "?";
		} else {
			return s;
		}
	}

	private Partie checkCoupValide(Partie partie, DemiCoup coup, Couleur joueur) {
		Partie partieResultat;

		Preconditions.checkNotNull(partie);
		Preconditions.checkNotNull(coup);
		Preconditions.checkNotNull(joueur);

		LOGGER.debug("checkCoupValide : {}", coup);

		partieResultat = new Partie(partie);

		try {

			if (coup instanceof DemiCoupDeplacement) {
				DemiCoupDeplacement demiCoupDeplacement = (DemiCoupDeplacement) coup;
				PieceCouleur p = partie.getPlateau().getCase(demiCoupDeplacement.getSrc());
				Verify.verify(p != null);
				Verify.verify(p.getCouleur() == joueur);

				PieceCouleur pDest = partie.getPlateau().getCase(demiCoupDeplacement.getDest());
				if (pDest != null) {
					Verify.verifyNotNull(pDest.getCouleur());
					Verify.verify(pDest.getCouleur() != joueur);
				}

				List<Mouvement> liste = calculMouvementsService.getMouvements(partie.getPlateau(), new PieceCouleurPosition(p.getPiece(),
						p.getCouleur(), demiCoupDeplacement.getSrc()));

				Verify.verifyNotNull(liste);
				Verify.verify(!liste.isEmpty());

				boolean deplacementAutorise = liste.stream().anyMatch(x -> x.getPosition().equals(demiCoupDeplacement.getDest()));
				if (!deplacementAutorise) {
					LOGGER.error("Erreur de deplacement PGN :");
					LOGGER.error("partie : \n{}", partie.getPlateau().getRepresentation2());
					LOGGER.error("mouvement : {}", coup);
					LOGGER.error("deplacements autorises : {}", liste);
				}
				Verify.verify(deplacementAutorise);

				partieResultat.setMove(demiCoupDeplacement.getSrc(),
						demiCoupDeplacement.getDest());

			} else {
				// TODO: a implementer
				throw new NotImplementedException();
			}

		} catch (Exception e) {
			LOGGER.error("Erreur de deplacement PGN : ");
			LOGGER.error("partie : \n{}", partie.getPlateau().getRepresentation2());
			LOGGER.error("mouvement : {}", coup);
			throw e;
		}

		return partieResultat;
	}

	private StringBuilder deplacement(DemiCoup demiCoup, Partie partie, Couleur couleur) {
		Preconditions.checkNotNull(demiCoup);
		Preconditions.checkNotNull(partie);
		Preconditions.checkNotNull(couleur);

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
						if (demiCoup2.isMangePiece()) {
							str.append('x');
						}
						str.append(demiCoup2.getDest());
					}
					break;
				default:
					str.append(demiCoup2.getPiece().getNomCourtAnglais());
					if (demiCoup2.isMangePiece()) {
						str.append('x');
					}

					boolean ajouteColonne = false;
					boolean ajouteRangeColonne = false;

					List<Position> listePieces = mouvementService.listePieces(partie.getPlateau(),
							new PieceCouleur(demiCoup2.getPiece(), couleur));

					if (listePieces != null) {
						listePieces = listePieces.stream()
								.filter(x -> !x.equals(demiCoup2.getSrc()))
								.collect(Collectors.toList());

						if (!CollectionUtils.isEmpty(listePieces)) {

							// ne prendre que les pieces qui attaque la même position
							listePieces = listePieces.stream()
									.filter(x -> attaque(partie.getPlateau(), x, demiCoup2.getDest()))
									.collect(Collectors.toList());

							if (!CollectionUtils.isEmpty(listePieces)) {
								boolean res = listePieces.stream()
										.anyMatch(x -> x.getColonne().equals(demiCoup2.getSrc().getColonne()));

								if (res) {
									ajouteRangeColonne = true;
								} else {
									ajouteColonne = true;
								}
							}
						}
					}

					if (ajouteRangeColonne) {
						str.append(demiCoup2.getSrc());
					} else if (ajouteColonne) {
						str.append(demiCoup2.getSrc().getColonne().getText());
					} else {
						str.append(demiCoup2.getDest());
					}
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

	private boolean attaque(Plateau plateau, Position positionPiece, Position positionAttaque) {
		PieceCouleur pieceCouleur = plateau.getCase(positionPiece);
		Verify.verifyNotNull(pieceCouleur);
		List<Mouvement> liste = calculMouvementsService.getMouvements(plateau, new PieceCouleurPosition(pieceCouleur.getPiece(),
				pieceCouleur.getCouleur(), positionPiece));

		return liste.stream().anyMatch(x -> x.getPosition().equals(positionAttaque));
	}

	public ListeDemiCoupDTO listeCoups(Moteur moteur) {
		ListeDemiCoupDTO listeDemiCoupDTO = new ListeDemiCoupDTO();

		List<DemiCoupDTO> liste = new ArrayList<>();
		listeDemiCoupDTO.setList(liste);

		Partie partie = moteur.getPartie();

		List<DemiCoup> coupBlancs = partie.getListeCoupsBlancs();
		List<DemiCoup> coupNoirs = partie.getListeCoupsNoirs();

		Plateau plateau = new Plateau();
		plateau.initialise();

		Partie partie2 = new Partie(plateau, Couleur.Blanc, new InformationPartie());


		for (int i = 0; i < coupBlancs.size(); i++) {
			int noCoup = i + 1;

			// blancs

			DemiCoup demiCoup = coupBlancs.get(i);

			DemiCoupDTO demiCoupDTO = new DemiCoupDTO();
			demiCoupDTO.setBlanc(true);
			demiCoupDTO.setNoCoup(noCoup);

			StringBuilder str = deplacement(demiCoup, partie2, Couleur.Blanc);

			demiCoupDTO.setDeplacement(str.toString());

			liste.add(demiCoupDTO);

			partie2.setMove(demiCoup);

			// noirs

			if (i < coupNoirs.size()) {

				demiCoup = coupNoirs.get(i);

				demiCoupDTO = new DemiCoupDTO();
				demiCoupDTO.setBlanc(false);
				demiCoupDTO.setNoCoup(noCoup);

				str = deplacement(demiCoup, partie2, Couleur.Noir);

				demiCoupDTO.setDeplacement(str.toString());

				liste.add(demiCoupDTO);

				partie2.setMove(demiCoup);
			}
		}

		return listeDemiCoupDTO;
	}
}
