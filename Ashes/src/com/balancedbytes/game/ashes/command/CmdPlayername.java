package com.balancedbytes.game.ashes.command;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdPlayername extends Command {

	public static final int MAX_NAME_LENGTH = 32;

	private static final String PLAYER_NAME = "playerName";
	
	private String fName;
	
	public CmdPlayername() {
		super();
	}
	
	public String getPlayerName() {
		return fName;
	}
	
	public void setPlayerName(String name) {
		fName = name;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.PLAYERNAME;
	}
	
	@Override
	public void validate(Game game, ValidationResult result) {
		if ((game == null) || (result == null)) {
			return;
		}
		if ((fName != null) && (fName.length() > MAX_NAME_LENGTH)) {
			result.add("A playername cannot be longer than " + MAX_NAME_LENGTH + " characters.");
		}
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(PLAYER_NAME, getPlayerName());
		return json.toJsonObject();
	}

	@Override
	public CmdPlayername fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setPlayerName(json.getString(PLAYER_NAME));
		return this;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("playername ")
			.append('"').append(AshesUtil.toString(getPlayerName())).append('"')
			.toString();
	}

}
