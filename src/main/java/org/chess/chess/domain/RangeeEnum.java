package org.chess.chess.domain;

public enum RangeeEnum {

	RANGEE1(1),
	RANGEE2(2),
	RANGEE3(3),
	RANGEE4(4),
	RANGEE5(5),
	RANGEE6(6),
	RANGEE7(7),
	RANGEE8(8);

	private final int no;

	RangeeEnum(int no) {
		this.no = no;
	}

	public static RangeeEnum get(int no) {
		for (RangeeEnum rangee : RangeeEnum.values()) {
			if (rangee.getNo() == no) {
				return rangee;
			}
		}
		return null;
	}

	public int getNo() {
		return no;
	}

	public String getText() {
		return "" + no;
	}
}
