package com.balancedbytes.game.roborally.model;

import com.balancedbytes.game.roborally.json.IJsonWritable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

public enum Orientation implements IJsonWritable {
	
	NORTH("N"),
	EAST("E"),
	SOUTH("S"),
	WEST("W");
	
	private String fJsonString;
	
	private Orientation(String jsonString) {
		fJsonString = jsonString;
	}
	
	@Override
	public JsonValue toJson() {
		return Json.value(fJsonString);
	}
	
	public static Orientation findForJsonString(String jsonString) {
		if ((jsonString != null) && (jsonString.length() >= 1)) {
			String singleUppercaseLetter = jsonString.substring(0, 1).toUpperCase();
			for (Orientation element : Orientation.values()) {
				if (element.fJsonString.equals(singleUppercaseLetter)) {
					return element;
				}
			}
		}
		return null;
	}

}
