package com.balancedbytes.game.roborally.model.factory;

public class Floor extends FactoryElement {

	private int fVariant;
	
	public Floor(int variant) {
		fVariant = variant;
	}
	
	public int getVariant() {
		return fVariant;
	}
	
	@Override
	public FactoryElementType getType() {
		return FactoryElementType.FLOOR;
	}
	
	@Override
	public boolean isMainElement() {
		return true;
	}

}
