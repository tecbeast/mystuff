package com.balancedbytes.game.roborally.model.factory;

public class ConveyorBelt extends FactoryElement {
	
	private int fSpeed;

	public ConveyorBelt(int speed) {
		fSpeed = speed;
	}
	
	public int getSpeed() {
		return fSpeed;
	}
	
	@Override
	public FactoryElementType getType() {
		if (getSpeed() <= 1) {
			return FactoryElementType.CONVEYOR_BELT_1;
		} else {
			return FactoryElementType.CONVEYOR_BELT_2;
		}
	}
	
	@Override
	public boolean isMainElement() {
		return true;
	}

}
