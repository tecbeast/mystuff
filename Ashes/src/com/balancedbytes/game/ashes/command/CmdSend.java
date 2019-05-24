package com.balancedbytes.game.ashes.command;

import java.util.HashSet;
import java.util.Set;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.FleetList;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Planet;
import com.balancedbytes.game.ashes.model.Unit;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdSend extends Command {
	
	private static final String COUNT = "count";
	private static final String UNIT = "unit";
	private static final String FROM_PLANET_NR = "fromPlanetNr";
	private static final String FROM_PLANET_NAME = "fromPlanetName";
	private static final String TO_PLANET_NR = "toPlanetNr";
	private static final String TO_PLANET_NAME = "toPlanetName";
	
	private static final Set<Unit> CARGO_UNITS = new HashSet<Unit>();
	static {
		CARGO_UNITS.add(Unit.CARGO_0);
		CARGO_UNITS.add(Unit.CARGO_1);
		CARGO_UNITS.add(Unit.CARGO_2);
		CARGO_UNITS.add(Unit.CARGO_3);
		CARGO_UNITS.add(Unit.CARGO_4);
		CARGO_UNITS.add(Unit.CARGO_5);
		CARGO_UNITS.add(Unit.CARGO_6);
		CARGO_UNITS.add(Unit.CARGO_7);
		CARGO_UNITS.add(Unit.CARGO_8);
		CARGO_UNITS.add(Unit.CARGO_9);
	}
	
	private int fCount;
	private Unit fUnit;
	private int fFromPlanetNr;
	private String fFromPlanetName;
	private int fToPlanetNr;
	private String fToPlanetName;
	
	public CmdSend() {
		super();
	}
	
	public int getCount() {
		return fCount;
	}
	
	public void setCount(int count) {
		fCount = count;
	}
	
	public Unit getUnit() {
		return fUnit;
	}
	
	public void setUnit(Unit unit) {
		fUnit = unit;
	}
	
	public int getFromPlanetNr() {
		return fFromPlanetNr;
	}
	
	public void setFromPlanetNr(int fromPlanetNr) {
		fFromPlanetNr = fromPlanetNr;
	}
	
	public String getFromPlanetName() {
		return fFromPlanetName;
	}
	
	public void setFromPlanetName(String fromPlanetName) {
		fFromPlanetName = fromPlanetName;
	}
	
	public int getToPlanetNr() {
		return fToPlanetNr;
	}

	public void setToPlanetNr(int toPlanetNr) {
		fToPlanetNr = toPlanetNr;
	}
	
	public String getToPlanetName() {
		return fToPlanetName;
	}
	
	public void setToPlanetName(String toPlanetName) {
		fToPlanetName = toPlanetName;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.SEND;
	}
	
	public boolean isCargo() {
		return CARGO_UNITS.contains(getUnit());
	}
	
	@Override
	public void validate(Game game, ValidationResult result) {
		if ((game == null) || (result == null)) {
			return;
		}
		if (fFromPlanetName != null) {
			fFromPlanetNr = ValidationUtil.findPlanetNr(game, fFromPlanetName);
			if (fFromPlanetNr == 0) {
				result.add("Unknown planet \"" + fFromPlanetNr + "\".");
				return;
			}
		}
		fFromPlanetName = null;
		if ((fFromPlanetNr < 1) || (fFromPlanetNr > Game.NR_OF_PLANETS)) {
			result.add("Unknown planet \"" + fFromPlanetNr + "\".");
			return;
		}
		if (fToPlanetName != null) {
			fToPlanetNr = ValidationUtil.findPlanetNr(game, fToPlanetName);
			if (fToPlanetNr == 0) {
				result.add("Unknown planet \"" + fToPlanetNr + "\".");
				return;
			}
		}
		fToPlanetName = null;
		if ((fToPlanetNr < 1) || (fToPlanetNr > Game.NR_OF_PLANETS)) {
			result.add("Unknown planet \"" + fToPlanetNr + "\".");
			return;
		}
		Planet fromPlanet = game.getPlanet(fFromPlanetNr);
		FleetList fleets = fromPlanet.findFleetsForPlayerNr(getPlayerNr());
		if (fleets.totalShips() == 0) {
			result.add("You have no fleet to send from " + fromPlanet.getName() + " (" + fromPlanet.getNumber() + ").");
		}
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(COUNT, getCount());
		json.add(UNIT, AshesUtil.print(getUnit()));
		json.add(FROM_PLANET_NR, getFromPlanetNr());
		json.add(FROM_PLANET_NAME, getFromPlanetName());
		json.add(TO_PLANET_NR, getToPlanetNr());
		json.add(TO_PLANET_NAME, getToPlanetName());
		return json.toJsonObject();
	}

	@Override
	public CmdSend fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setCount(json.getInt(COUNT));
		String unitString = json.getString(UNIT);
		setUnit(AshesUtil.provided(unitString) ? Unit.valueOf(unitString) : null);
		setFromPlanetNr(json.getInt(FROM_PLANET_NR));
		setFromPlanetName(json.getString(FROM_PLANET_NAME));
		setToPlanetNr(json.getInt(TO_PLANET_NR));
		setToPlanetName(json.getString(TO_PLANET_NAME));
		return this;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("send ").append(getCount()).append(" ")
			.append((getUnit() != null) ? getUnit().getShorthand() : "?")
			.append(" from ").append(getFromPlanetNr())
			.append(" to ").append(getToPlanetNr())
			.toString();
	}

}
