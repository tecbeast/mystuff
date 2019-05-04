package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Unit;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdSend extends Command {
	
	private static final String COUNT = "count";
	private static final String UNIT = "unit";
	private static final String ON_PLANET_NR = "onPlanetNr";
	private static final String TO_PLANET_NR = "toPlanetNr";
	
	private int fCount;
	private Unit fUnit;
	private int fOnPlanetNr;
	private int fToPlanetNr;
	
	protected CmdSend() {
		super();
	}
	
	public CmdSend(int playerNr, int count, Unit unit, int toPlanetNr, int onPlanetNr) {
		setPlayerNr(playerNr);
		setCount(count);
		setUnit(unit);
		setToPlanetNr(toPlanetNr);
		setOnPlanetNr(onPlanetNr);
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
	
	public int getOnPlanetNr() {
		return fOnPlanetNr;
	}
	
	protected void setOnPlanetNr(int onPlanetNr) {
		fOnPlanetNr = onPlanetNr;
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
	
	@Override
	public List<String> validate(Game game) {
		// TODO: check if player owns planet
		return new ArrayList<String>();
	}
	
	@Override
	public boolean execute(Game game) {
		return false;
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(COUNT, getCount());
		json.add(UNIT, AshesUtil.toString(getUnit()));
		json.add(ON_PLANET_NR, getOnPlanetNr());
		json.add(TO_PLANET_NR, getToPlanetNr());
		return json.getJsonObject();
	}

	@Override
	public CmdSend fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setCount(json.getInt(COUNT));
		String unitString = json.getString(UNIT);
		setUnit(AshesUtil.isProvided(unitString) ? Unit.valueOf(unitString) : null);
		setOnPlanetNr(json.getInt(ON_PLANET_NR));
		setToPlanetNr(json.getInt(TO_PLANET_NR));
		return this;
	}

}
