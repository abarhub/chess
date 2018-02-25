package org.chess.chess.dto;

import java.util.List;

public class PlateauDTO {

	private List<PieceDTO> listePieces;

	private String etatJeux;

	public List<PieceDTO> getListePieces() {
		return listePieces;
	}

	public void setListePieces(List<PieceDTO> listePieces) {
		this.listePieces = listePieces;
	}

	public String getEtatJeux() {
		return etatJeux;
	}

	public void setEtatJeux(String etatJeux) {
		this.etatJeux = etatJeux;
	}
}
