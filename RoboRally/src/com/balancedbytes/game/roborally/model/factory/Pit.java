package com.balancedbytes.game.roborally.model.factory;

public class Pit extends FactoryElement {

	public Pit() {
		super();
	}
	
	@Override
	public FactoryElementType getType() {
		return FactoryElementType.PIT;
	}
	
	@Override
	public boolean isMainElement() {
		return true;
	}

}
