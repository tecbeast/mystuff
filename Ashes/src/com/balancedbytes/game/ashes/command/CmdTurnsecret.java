package com.balancedbytes.game.ashes.command;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdTurnsecret extends Command {
	
	private static final String SECRET = "secret";
	
	private String fSecret;
	
	public CmdTurnsecret() {
		super();
	}
	
	public String getSecret() {
		return fSecret;
	}
	
	public void setSecret(String secret) {
		fSecret = secret;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.TURNSECRET;
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(SECRET, getSecret());
		return json.toJsonObject();
	}

	@Override
	public CmdTurnsecret fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setSecret(json.getString(SECRET));
		return this;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("turnsecret ").append(getSecret())
			.toString();
	}

}
