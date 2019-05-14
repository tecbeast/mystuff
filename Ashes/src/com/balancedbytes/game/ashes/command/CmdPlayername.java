package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdPlayername extends Command {
	
	private static final String NAME = "name";
	
	private String fName;
	
	protected CmdPlayername() {
		super();
	}
	
	public CmdPlayername(int playerNr, String newName) {
		setPlayerNr(playerNr);
		setName(newName);
	}

	public String getName() {
		return fName;
	}
	
	protected void setName(String name) {
		fName = name;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.PLAYERNAME;
	}
	
	@Override
	public List<String> validate(Game game) {
		return new ArrayList<String>();
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(NAME, getName());
		return json.toJsonObject();
	}

	@Override
	public CmdPlayername fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setName(json.getString(NAME));
		return this;
	}

}
