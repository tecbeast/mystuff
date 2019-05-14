package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdPlanetname extends Command {
	
	public static final int MAX_NAME_LENGTH = 32;
	
	private static final String PLANET_NR = "planetNr";
	private static final String PLANET_NAME = "planetName";
	private static final String NAME = "name";
	
	private int fPlanetNr;
	private String fPlanetName;
	private String fName;
	
	public CmdPlanetname() {
		super();
	}

	public int getPlanetNr() {
		return fPlanetNr;
	}
	
	public void setPlanetNr(int planetNr) {
		fPlanetNr = planetNr;
	}
	
	public String getPlanetName() {
		return fPlanetName;
	}
	
	public void setPlanetName(String planetName) {
		fPlanetName = planetName;
	}
	
	public String getName() {
		return fName;
	}
	
	public void setName(String name) {
		fName = name;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.PLANETNAME;
	}
	
	@Override
	public List<String> validate(Game game) {
		List<String> messages = new ArrayList<String>();
		if (game != null) {
			if (fPlanetName != null) {
				fPlanetNr = CommandValidationUtil.findPlanetNr(game, fPlanetName, messages);
			}
			if (fPlanetNr > 0) {
				fPlanetName = null;
				if (game.getPlanet(fPlanetNr).getPlayerNr() != getPlayerNr()) {
					messages.add("You cannot name a planet you do not control.");
				}
				if ((fName != null) && (fName.length() > MAX_NAME_LENGTH)) {
					messages.add("A planet name cannot be longer than " + MAX_NAME_LENGTH + " characters.");
				}
			}
		}
		return messages;
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(PLANET_NR, getPlanetNr());
		json.add(PLANET_NAME, getPlanetName());
		json.add(NAME, getName());
		return json.toJsonObject();
	}

	@Override
	public CmdPlanetname fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setPlanetNr(json.getInt(PLANET_NR));
		setPlanetName(json.getString(PLANET_NAME));
		setName(json.getString(NAME));
		return this;
	}

}
