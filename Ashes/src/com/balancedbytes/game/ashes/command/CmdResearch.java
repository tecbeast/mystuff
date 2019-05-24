package com.balancedbytes.game.ashes.command;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Improvement;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdResearch extends Command {
	
	private static final String COUNT = "count";
	private static final String IMPROVEMENT = "improvement";
	private static final String PLANET_NR = "planetNr";
	private static final String PLANET_NAME = "planetName";
	
	private int fCount;
	private Improvement fImprovement;
	private int fPlanetNr;
	private String fPlanetName;
	
	public CmdResearch() {
		super();
	}

	public int getCount() {
		return fCount;
	}
	
	public void setCount(int count) {
		fCount = count;
	}

	public Improvement getImprovement() {
		return fImprovement;
	}
	
	public void setImprovement(Improvement improvement) {
		fImprovement = improvement;
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
		return CommandType.RESEARCH;
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
			}
		}
		fPlanetName = null;
		if ((fPlanetNr < 1) || (fPlanetNr > Game.NR_OF_PLANETS)) {
			result.add("Unknown planet \"" + fPlanetNr + "\".");
			return;
		}
		if (game.getPlanet(fPlanetNr).getPlayerNr() != getPlayerNr()) {
			result.add("You cannot research on a planet you do not control.");
		}
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(COUNT, getCount());
		json.add(IMPROVEMENT, AshesUtil.print(getImprovement()));
		json.add(PLANET_NR, getPlanetNr());
		json.add(PLANET_NAME, getPlanetName());
		return json.toJsonObject();
	}

	@Override
	public CmdResearch fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setCount(json.getInt(COUNT));
		String imprString = json.getString(IMPROVEMENT);
		setImprovement(AshesUtil.provided(imprString) ? Improvement.valueOf(imprString) : null);
		setPlanetNr(json.getInt(PLANET_NR));
		setPlanetName(json.getString(PLANET_NAME));
		return this;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("research ")
			.append((getImprovement() != null) ? getImprovement().getShorthand() : "?")
			.append(" on ").append(getPlanetNr())
			.toString();
	}

}
