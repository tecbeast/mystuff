package com.balancedbytes.game.roborally.model.factory;

public class Wall extends FactoryElement {
	
	private int fSides;

	public Wall(int sides) {
		fSides = sides;
	}
	
	public int getSides() {
		return fSides;
	}
	
	@Override
	public FactoryElementType getType() {
		if (getSides() <= 1) {
			return FactoryElementType.WALL_1;
		} else if (getSides() == 2) {
			return FactoryElementType.WALL_2;
		} else {
			return FactoryElementType.WALL_3;
		}
	}
	
	@Override
	public boolean isMainElement() {
		return false;
	}

}
