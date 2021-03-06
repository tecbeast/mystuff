package com.balancedbytes.game.ashes.command;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdHomeplanet extends Command {
	
	private static final String PLANET_NR = "planetNr";
	private static final String PLANET_NAME = "planetName";
	
	private int fPlanetNr;
	private String fPlanetName;
	
	public CmdHomeplanet() {
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
	
	@Override
	public CommandType getType() {
		return CommandType.HOMEPLANET;
	}
	
	@Override
	public void validate(Game game, ValidationResult result) {
		if ((game == null) || (result == null)) {
			return;
		}
		if (fPlanetName != null) {
			fPlanetNr = ValidationUtil.findPlanetNr(game, fPlanetName);
			if (fPlanetNr == 0) {
				result.add("Unknown planet \"" + fPlanetName + "\".");
				return;
			}
		}
		fPlanetName = null;
		if ((fPlanetNr < 1) || (fPlanetNr > Game.NR_OF_PLANETS)) {
			result.add("Unknown planet \"" + fPlanetNr + "\".");
			return;
		}
		if (game.getPlanet(fPlanetNr).getPlayerNr() != getPlayerNr()) {
			result.add("You cannot set your home on a planet you do not control.");
		}
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(PLANET_NR, getPlanetNr());
		json.add(PLANET_NAME, getPlanetName());
		return json.toJsonObject();
	}

	@Override
	public CmdHomeplanet fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setPlanetNr(json.getInt(PLANET_NR));
		setPlanetName(json.getString(PLANET_NAME));
		return this;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("homeplanet on ").append(getPlanetNr())
			.toString();
	}

}
