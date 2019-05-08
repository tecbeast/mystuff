package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdPlanetname extends Command {
	
	private static final String PLANET_NR = "planetNr";
	private static final String NAME = "name";
	
	private int fPlanetNr;
	private String fName;
	
	protected CmdPlanetname() {
		super();
	}
	
	public CmdPlanetname(int playerNr, String name, int planetNr) {
		setPlayerNr(playerNr);
		setName(name);
		setPlanetNr(planetNr);
	}

	public int getPlanetNr() {
		return fPlanetNr;
	}
	
	protected void setPlanetNr(int planetNr) {
		fPlanetNr = planetNr;
	}
	
	public String getName() {
		return fName;
	}
	
	protected void setName(String name) {
		fName = name;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.PLANETNAME;
	}
	
	@Override
	public List<String> validate(Game game) {
		return new ArrayList<String>();
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(PLANET_NR, getPlanetNr());
		json.add(NAME, getName());
		return json.toJsonObject();
	}

	@Override
	public CmdPlanetname fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setPlanetNr(json.getInt(PLANET_NR));
		setName(json.getString(NAME));
		return this;
	}

}
