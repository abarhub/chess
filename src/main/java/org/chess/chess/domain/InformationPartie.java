package org.chess.chess.domain;

import java.time.LocalDate;

public class InformationPartie {

	private String event;
	private String site;
	private LocalDate date;
	private String round;
	private String joueurBlanc;
	private String joueurNoir;

	public InformationPartie() {

	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getJoueurBlanc() {
		return joueurBlanc;
	}

	public void setJoueurBlanc(String joueurBlanc) {
		this.joueurBlanc = joueurBlanc;
	}

	public String getJoueurNoir() {
		return joueurNoir;
	}

	public void setJoueurNoir(String joueurNoir) {
		this.joueurNoir = joueurNoir;
	}
}
