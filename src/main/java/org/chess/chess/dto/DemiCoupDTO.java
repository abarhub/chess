package org.chess.chess.dto;

public class DemiCoupDTO {

	private String deplacement;
	private int noCoup;
	private boolean blanc;

	public String getDeplacement() {
		return deplacement;
	}

	public void setDeplacement(String deplacement) {
		this.deplacement = deplacement;
	}

	public int getNoCoup() {
		return noCoup;
	}

	public void setNoCoup(int noCoup) {
		this.noCoup = noCoup;
	}

	public boolean isBlanc() {
		return blanc;
	}

	public void setBlanc(boolean blanc) {
		this.blanc = blanc;
	}
}
