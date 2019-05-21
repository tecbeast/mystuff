package com.balancedbytes.game.ashes.model;

public enum Improvement {

	PRODUCTION_RATE("PR"),
	FIGHTER_MORALE("FM"),
	TRANSPORTER_MORALE("TM");
	
	private String fShorthand;
	
	private Improvement(String shorthand) {
		fShorthand = shorthand;
	}
	
	public String getShorthand() {
		return fShorthand;
	}
	
}
