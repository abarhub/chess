package org.chess.chess.dto;

import org.chess.chess.domain.DemiCoup;

import java.util.List;

public class ListeDemiCoupDTO {

	private List<DemiCoupDTO> list;

	public List<DemiCoupDTO> getList() {
		return list;
	}

	public void setList(List<DemiCoupDTO> list) {
		this.list = list;
	}
}
