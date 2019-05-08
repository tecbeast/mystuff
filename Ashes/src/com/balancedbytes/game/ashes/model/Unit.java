package com.balancedbytes.game.ashes.model;

public enum Unit {
	
	PLANETARY_DEFENSE_UNIT(1),
	FUEL_PLANT(2),
	ORE_PLANT(3),
	RARE_PLANT(4),
	FIGHTER_YARD(5),
	TRANSPORTER_YARD(6),
	
	CARGO_0(7),   // 8 A
	CARGO_1(8),   // 1 PDU
	CARGO_2(9),   // 4 FP
	CARGO_3(10),  // 5 OP
	CARGO_4(11),  // 2 RP
	CARGO_5(12),  // 40 FUEL
	CARGO_6(13),  // 50 ORE
	CARGO_7(14),  // 20 RARE
	CARGO_8(15),  // 1 FY
	CARGO_9(16),  // 1 TY
	
	FIGHTER(17),
	TRANSPORTER(18);
	
	private int fOrder;
	
	private Unit(int order) {
		fOrder = order;
	}
	
	public int getOrder() {
		return fOrder;
	}
	
}
