package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Unit;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdBuild extends Command {
	
	private static final String COUNT = "count";
	private static final String UNIT = "unit";
	private static final String PLANET_NR = "planetNr";
	
	private int fCount;
	private Unit fUnit;
	private int fPlanetNr;
	
	protected CmdBuild() {
		super();
	}
	
	public CmdBuild(int playerNr, int count, Unit unit, int planetNr) {
		setPlayerNr(playerNr);
		setCount(count);
		setUnit(unit);
		setPlanetNr(planetNr);
	}
	
	public int getCount() {
		return fCount;
	}
	
	protected void setCount(int count) {
		fCount = count;
	}
	
	public Unit getUnit() {
		return fUnit;
	}
	
	protected void setUnit(Unit unit) {
		fUnit = unit;
	}
	
	public int getPlanetNr() {
		return fPlanetNr;
	}
	
	protected void setPlanetNr(int planetNr) {
		fPlanetNr = planetNr;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.BUILD;
	}
	
	@Override
	public List<String> validate(Game game) {
		List<String> messages = new ArrayList<String>();
		if (game != null) {
			if (game.getPlanet(fPlanetNr).getPlayerNr() != getPlayerNr()) {
				messages.add("You cannot build on a planet you do not control.");
			}
		}
		return messages;
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(COUNT, getCount());
		json.add(UNIT, AshesUtil.toString(getUnit()));
		json.add(PLANET_NR, getPlanetNr());
		return json.toJsonObject();
	}

	@Override
	public CmdBuild fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setCount(json.getInt(COUNT));
		String unitString = json.getString(UNIT);
		setUnit(AshesUtil.isProvided(unitString) ? Unit.valueOf(unitString) : null);
		setPlanetNr(json.getInt(PLANET_NR));
		return this;
	}

}
