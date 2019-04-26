package com.balancedbytes.game.roborally.model.factory;

import com.balancedbytes.game.roborally.json.IJsonWritable;
import com.balancedbytes.game.roborally.model.Orientation;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

public abstract class FactoryElement implements IJsonWritable {
	
	public static final String JSON_SEPARATOR = "-";

	private Orientation fOrientation;
	
	public FactoryElement() {
		super();
	}
	
	public FactoryElement setOrientation(Orientation orientation) {
		fOrientation = orientation;
		return this;
	}
		
	public Orientation getOrientation() {
		return fOrientation;
	}
	
	@Override
	public JsonValue toJson() {
		StringBuilder jsonString = new StringBuilder();
		jsonString.append(getType().toJson().asString());
		if (getOrientation() != null) {
			jsonString.append(JSON_SEPARATOR);
			jsonString.append(getOrientation().toJson().asString());
		}
		return Json.value(jsonString.toString());
	}

	public abstract FactoryElementType getType();

	public abstract boolean isMainElement();

}
