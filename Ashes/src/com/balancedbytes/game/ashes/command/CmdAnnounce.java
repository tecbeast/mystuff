package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdAnnounce extends Command {
	
	private static final String TEXT = "text";
	
	private String fText;
	
	protected CmdAnnounce() {
		super();
	}
	
	public CmdAnnounce(int playerNr, String text) {
		setPlayerNr(playerNr);
		setText(text);
	}

	public String getText() {
		return fText;
	}
	
	protected void setText(String text) {
		fText = text;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.ANNOUNCE;
	}
	
	@Override
	public List<String> validate(Game game) {
		return new ArrayList<String>();
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(TEXT, getText());
		return json.toJsonObject();
	}

	@Override
	public CmdAnnounce fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setText(json.getString(TEXT));
		return this;
	}
	
}
