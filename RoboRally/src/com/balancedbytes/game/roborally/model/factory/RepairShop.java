package com.balancedbytes.game.roborally.model.factory;

public class RepairShop extends FactoryElement {
	
	private int fValue;

	public RepairShop(int value) {
		fValue = value;
	}
	
	public int getValue() {
		return fValue;
	}
	
	@Override
	public FactoryElementType getType() {
		if (getValue() > 1) {
			return FactoryElementType.REPAIR_SHOP_2;
		} else  {
			return FactoryElementType.REPAIR_SHOP_1;
		}
	}
	
	@Override
	public boolean isMainElement() {
		return true;
	}

}
