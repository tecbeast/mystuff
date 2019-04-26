package com.balancedbytes.game.roborally.model.factory;

import com.balancedbytes.game.roborally.model.Coordinate;

public class FactoryTile {
	
	private Coordinate fCoordinate;
	private FactoryElements fElements;
	
	public FactoryTile() {
		fCoordinate = new Coordinate();
		fElements = new FactoryElements();
	}
	
	public void setCoordinate(Coordinate coordinate) {
		fCoordinate = coordinate;
	}
	
	public Coordinate getCoordinate() {
		return fCoordinate;
	}
	
	public void setElements(FactoryElements elements) {
		fElements = elements;
	}
	
	public FactoryElements getElements() {
		return fElements;
	}

}
