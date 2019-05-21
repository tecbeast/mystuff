package com.balancedbytes.game.ashes.model;

public enum Unit {
	
	PLANETARY_DEFENSE_UNIT(1, "PDU"),
	FUEL_PLANT(2, "FP"),
	ORE_PLANT(3, "OP"),
	RARE_PLANT(4, "RP"),
	FIGHTER_YARD(5, "FY"),
	TRANSPORTER_YARD(6, "TY"),
	
	CARGO_0(7,  "C0"),  // 8 A
	CARGO_1(8,  "C1"),  // 1 PDU
	CARGO_2(9,  "C2"),  // 4 FP
	CARGO_3(10, "C3"),  // 5 OP
	CARGO_4(11, "C4"),  // 2 RP
	CARGO_5(12, "C5"),  // 40 FUEL
	CARGO_6(13, "C6"),  // 50 ORE
	CARGO_7(14, "C7"),  // 20 RARE
	CARGO_8(15, "C8"),  // 1 FY
	CARGO_9(16, "C9"),  // 1 TY
	
	FIGHTER(17, "FI"),
	TRANSPORTER(18, "TR");
	
	private int fOrder;
	private String fShorthand;
	
	private Unit(int order, String shorthand) {
		fOrder = order;
		fShorthand = shorthand;
	}
	
	public int getOrder() {
		return fOrder;
	}
	
	public String getShorthand() {
		return fShorthand;
	}
	
}
