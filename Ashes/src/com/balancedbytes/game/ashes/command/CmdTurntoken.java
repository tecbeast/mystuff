package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdTurntoken extends Command {
	
	private static final String TOKEN = "token";
	
	private String fToken;
	
	protected CmdTurntoken() {
		super();
	}
	
	public CmdTurntoken(int playerNr, String token) {
		setPlayerNr(playerNr);
		setToken(token);
	}
	
	public String getToken() {
		return fToken;
	}
	
	protected void setToken(String token) {
		fToken = token;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.TURNTOKEN;
	}
	
	@Override
	public List<String> validate(Game game) {
		return new ArrayList<String>();
	}
	
	@Override
	public boolean execute(Game game) {
		return false;
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(TOKEN, getToken());
		return json.getJsonObject();
	}

	@Override
	public CmdTurntoken fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setToken(json.getString(TOKEN));
		return this;
	}

}
