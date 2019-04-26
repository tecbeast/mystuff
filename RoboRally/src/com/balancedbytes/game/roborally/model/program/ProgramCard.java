package com.balancedbytes.game.roborally.model.program;

public abstract class ProgramCard {
	
	private ProgramCardType fType;
	private int fSpeed;
	
	public ProgramCard(ProgramCardType type, int speed) {
		fType = type;
		fSpeed = speed;
	}
	
	public ProgramCardType getType() {
		return fType;
	}
	
	public int getSpeed() {
		return fSpeed;
	}

}
