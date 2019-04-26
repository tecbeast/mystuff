package com.balancedbytes.game.roborally.json;

import com.eclipsesource.json.JsonValue;

public interface IJsonReadable {

	Object fromJson(JsonValue jsonValue);
	
}
