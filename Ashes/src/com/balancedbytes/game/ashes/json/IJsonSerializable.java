package com.balancedbytes.game.ashes.json;

import com.eclipsesource.json.JsonValue;

public interface IJsonSerializable {
	
	public JsonValue toJson();
	
	public IJsonSerializable fromJson(JsonValue jsonValue);

}
