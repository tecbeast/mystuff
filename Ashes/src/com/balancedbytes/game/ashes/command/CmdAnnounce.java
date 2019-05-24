package com.balancedbytes.game.ashes.command;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdAnnounce extends Command {

	private static final String TEXT = "text";
	
	private String fText;
	
	public CmdAnnounce() {
		super();
	}
	
	public String getText() {
		return fText;
	}
	
	public void setText(String text) {
		fText = text;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.ANNOUNCE;
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
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("announce ")
			.append('"').append(AshesUtil.print(getText())).append('"')
			.toString();
	}

}
