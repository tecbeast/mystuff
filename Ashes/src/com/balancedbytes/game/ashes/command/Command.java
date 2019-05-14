package com.balancedbytes.game.ashes.command;

import java.util.List;

import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.balancedbytes.game.ashes.json.JsonSerializationException;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public abstract class Command implements IJsonSerializable {
	
	protected static final String TYPE = "type";
	protected static final String PLAYER_NR = "playerNr";
	
	private int fPlayerNr;
	
	protected Command() {
		super();
	}
	
	public int getPlayerNr() {
		return fPlayerNr;
	}
	
	public void setPlayerNr(int playerNr) {
		fPlayerNr = playerNr;
	}
	
	public abstract CommandType getType();
	
	public abstract List<String> validate(Game game);
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(new JsonObject());
		json.add(TYPE, getType().toString());
		json.add(PLAYER_NR, getPlayerNr());
		return json.toJsonObject();
	}
	
	@Override
	public Command fromJson(JsonValue jsonValue) {
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		try {
			CommandType type = CommandType.valueOf(json.getString(TYPE));
			if (getType() != type) {
				throw new JsonSerializationException("Expected type " + getType());
			}
		} catch (IllegalArgumentException iae) {
			throw new JsonSerializationException("Unknown type");
		}		
		setPlayerNr(json.getInt(PLAYER_NR));
		return this;
	}

}
