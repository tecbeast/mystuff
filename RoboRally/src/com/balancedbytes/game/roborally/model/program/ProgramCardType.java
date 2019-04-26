package com.balancedbytes.game.roborally.model.program;

import com.balancedbytes.game.roborally.json.IJsonWritable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

public enum ProgramCardType implements IJsonWritable {
	
	BACKUP("backup"),
	MOVE_1("move1"),
	MOVE_2("move2"),
	MOVE_3("move3"),
	TURN_LEFT("turnLeft"),
	TURN_RIGHT("turnRight"),
	U_TURN("uTurn");
	
	private String fJsonString;
	
	private ProgramCardType(String jsonString) {
		fJsonString = jsonString;
	}
	
	@Override
	public JsonValue toJson() {
		return Json.value(fJsonString);
	}
	
	public static ProgramCardType fromJson(JsonValue jsonValue) {
		if ((jsonValue != null) && jsonValue.isString()) {
			for (ProgramCardType element : ProgramCardType.values()) {
				if (element.fJsonString.equalsIgnoreCase(jsonValue.asString())) {
					return element;
				}
			}
		}
		return null;
	}

}
