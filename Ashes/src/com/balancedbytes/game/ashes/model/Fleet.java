package com.balancedbytes.game.ashes.model;

import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 *
 */
public class Fleet implements IJsonSerializable {

	private static final String PLAYER_NR = "playerNr";
	private static final String TRANSPORTERS = "transporters";
	private static final String FIGHTERS = "fighters";
	private static final String CARGO = "cargo";

	private int fPlayerNr;
	private int fTransporters;
	private int fFighters;
	private Cargo fCargo;

	/**
	 *
	 */
	protected Fleet() {
		this(0, 0, 0, null);
	}

	/**
	 *
	 */
	public Fleet(int playerNr, int fighters, int transporters) {
		this(playerNr, fighters, transporters, null);
	}

	/**
	 *
	 */
	public Fleet(Cargo cargo) {
		this(0, 0, 0, cargo);
	}

	/**
	 * 
	 */
	private Fleet(int playerNr, int fighters, int transporters, Cargo cargo) {
		setPlayerNr(playerNr);
		setFighters(fighters);
		setTransporters(transporters);
		setCargo((cargo != null) ? cargo : new Cargo());
	}

	/**
	 * Adds another fleet to this one without checking player number.
	 */
	public void add(Fleet anotherFleet) {
		fFighters += anotherFleet.getFighters();
		fTransporters += anotherFleet.getTransporters();
		fCargo.add(anotherFleet.getCargo());
	}
	
	public int getPlayerNr() {
		return fPlayerNr;
	}
	
	protected void setPlayerNr(int playerNr) {
		fPlayerNr = playerNr;
	}
	
	public int getFighters() {
		return fFighters;
	}
	
	public void setFighters(int fighters) {
		fFighters = 0;
		if (fighters > 0) {
			fFighters = fighters;
		}
	}
	
	public int getTransporters() {
		return fTransporters;
	}
	
	public void setTransporters(int transporters) {
		fTransporters = 0;
		if (transporters > 0) {
			fTransporters = transporters;
		}
	}
	
	public Cargo getCargo() {
		return fCargo;
	}
	
	public void addCargo(Cargo cargo) {
		fCargo.add(cargo);
	}
	
	protected void setCargo(Cargo cargo) {
		fCargo = cargo;
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(new JsonObject());
		json.add(PLAYER_NR, getPlayerNr());
		json.add(FIGHTERS, getFighters());
		json.add(TRANSPORTERS, getTransporters());
		json.add(CARGO, getCargo().toJson());
		return json.toJsonObject();
	}
	
	@Override
	public Fleet fromJson(JsonValue jsonValue) {
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setPlayerNr(json.getInt(PLAYER_NR));
		setFighters(json.getInt(FIGHTERS));
		setTransporters(json.getInt(TRANSPORTERS));
		setCargo(new Cargo().fromJson(json.getArray(CARGO)));
		return this;
	}
  
}