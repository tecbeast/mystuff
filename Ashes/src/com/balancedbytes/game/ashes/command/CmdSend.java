package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Unit;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdSend extends Command {
	
	private static final String COUNT = "count";
	private static final String UNIT = "unit";
	private static final String FROM_PLANET_NR = "fromPlanetNr";
	private static final String TO_PLANET_NR = "toPlanetNr";
	
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
	private int fToPlanetNr;
	
	protected CmdSend() {
		super();
	}
	
	public CmdSend(int playerNr, int count, Unit unit, int fromPlanetNr, int toPlanetNr) {
		setPlayerNr(playerNr);
		setCount(count);
		setUnit(unit);
		setFromPlanetNr(fromPlanetNr);
		setToPlanetNr(toPlanetNr);
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
	
	public int getFromPlanetNr() {
		return fFromPlanetNr;
	}
	
	protected void setFromPlanetNr(int fromPlanetNr) {
		fFromPlanetNr = fromPlanetNr;
	}
	
	public int getToPlanetNr() {
		return fToPlanetNr;
	}

	protected void setToPlanetNr(int toPlanetNr) {
		fToPlanetNr = toPlanetNr;
	}	
	
	@Override
	public CommandType getType() {
		return CommandType.SEND;
	}
	
	public boolean isCargo() {
		return CARGO_UNITS.contains(getUnit());
	}
	
	@Override
	public List<String> validate(Game game) {
		// TODO: check if player owns planet
		return new ArrayList<String>();
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(COUNT, getCount());
		json.add(UNIT, AshesUtil.toString(getUnit()));
		json.add(FROM_PLANET_NR, getFromPlanetNr());
		json.add(TO_PLANET_NR, getToPlanetNr());
		return json.toJsonObject();
	}

	@Override
	public CmdSend fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setCount(json.getInt(COUNT));
		String unitString = json.getString(UNIT);
		setUnit(AshesUtil.isProvided(unitString) ? Unit.valueOf(unitString) : null);
		setFromPlanetNr(json.getInt(FROM_PLANET_NR));
		setToPlanetNr(json.getInt(TO_PLANET_NR));
		return this;
	}

}
