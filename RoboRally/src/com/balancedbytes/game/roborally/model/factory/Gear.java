package com.balancedbytes.game.roborally.model.factory;

public class Gear extends FactoryElement {
	
	private boolean fClockwise;

	public Gear(boolean clockwise) {
		fClockwise = clockwise;
	}

	public boolean isClockwise() {
		return fClockwise;
	}
	
	@Override
	public FactoryElementType getType() {
		if (isClockwise()) {
			return FactoryElementType.GEAR_RIGHT;
		} else {
			return FactoryElementType.GEAR_LEFT;
		}
	}
	
	@Override
	public boolean isMainElement() {
		return true;
	}

}
