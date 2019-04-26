package com.balancedbytes.game.roborally.model.factory;

public class Laser extends FactoryElement {
	
	private int fShots;

	public Laser(int shots) {
		fShots = shots;
	}
	
	public int getShots() {
		return fShots;
	}
	
	@Override
	public FactoryElementType getType() {
		if (getShots() <= 1) {
			return FactoryElementType.LASER_1;
		} else if (getShots() == 2) {
			return FactoryElementType.LASER_2;
		} else {
			return FactoryElementType.LASER_3;
		}
	}
	
	@Override
	public boolean isMainElement() {
		return false;
	}

}
