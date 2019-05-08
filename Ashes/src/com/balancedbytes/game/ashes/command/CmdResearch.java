package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

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
	
	private int fCount;
	private Improvement fImprovement;
	private int fPlanetNr;
	
	protected CmdResearch() {
		super();
	}
	
	public CmdResearch(int playerNr, int count, Improvement improvement, int planetNr) {
		setPlayerNr(playerNr);
		setCount(count);
		setImprovement(improvement);
		setPlanetNr(planetNr);
	}
	
	public int getCount() {
		return fCount;
	}
	
	protected void setCount(int count) {
		fCount = count;
	}

	public Improvement getImprovement() {
		return fImprovement;
	}
	
	protected void setImprovement(Improvement improvement) {
		fImprovement = improvement;
	}
	
	public int getPlanetNr() {
		return fPlanetNr;
	}
	
	protected void setPlanetNr(int planetNr) {
		fPlanetNr = planetNr;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.RESEARCH;
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
		json.add(IMPROVEMENT, AshesUtil.toString(getImprovement()));
		json.add(PLANET_NR, getPlanetNr());
		return json.toJsonObject();
	}

	@Override
	public CmdResearch fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setCount(json.getInt(COUNT));
		String imprString = json.getString(IMPROVEMENT);
		setImprovement(AshesUtil.isProvided(imprString) ? Improvement.valueOf(imprString) : null);
		setPlanetNr(json.getInt(PLANET_NR));
		return this;
	}

}
