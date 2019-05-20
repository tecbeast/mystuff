package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdPlayername extends Command {

	public static final int MAX_NAME_LENGTH = 32;

	private static final String NAME = "name";
	
	private String fName;
	
	public CmdPlayername() {
		super();
	}
	
	public String getName() {
		return fName;
	}
	
	public void setName(String name) {
		fName = name;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.PLAYERNAME;
	}
	
	@Override
	public List<String> validate(Game game) {
		List<String> messages = new ArrayList<String>();
		if (game != null) {
			if ((fName != null) && (fName.length() > MAX_NAME_LENGTH)) {
				messages.add("A player name cannot be longer than " + MAX_NAME_LENGTH + " characters.");
			}
		}
		return messages;
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
