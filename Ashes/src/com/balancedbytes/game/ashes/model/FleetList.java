package com.balancedbytes.game.ashes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

/**
 *
 */
public class FleetList implements IJsonSerializable {

	private List<Fleet> fFleets = null;

	/**
	 *
	 */
	public FleetList() {
		fFleets = new ArrayList<Fleet>();
	}
	
	/**
	 * 
	 */
	public void clear() {
		fFleets.clear();
	}
	
	/**
	 *
	 */
	public int size() {
		return fFleets.size();
	}
	
	/**
	 * 
	 */
	public Fleet get(int index) {
		if ((index >= 0) && (index < fFleets.size())) { 
			return fFleets.get(index);
		}
		return null;
	}
	
	/**
	 * 
	 */
	public List<Fleet> toList() {
		return Collections.unmodifiableList(fFleets);
	}

	/**
	 * Adds a Fleet to this list.
	 * Checks for player number and adds to according fleet.
	 */
	public FleetList add(Fleet anotherFleet) {
		if (anotherFleet != null) {
			Fleet fleet = forPlayerNr(anotherFleet.getPlayerNr());
			if (fleet != null) {
				fleet.add(anotherFleet);
			} else {
				fFleets.add(anotherFleet);
			}
		}
		return this;
	}

	/**
	 * Adds the elements of a given FleetSet to this.
	 */
	public FleetList add(FleetList anotherSet) {
		if (anotherSet != null) {
			for (Fleet anotherFleet : anotherSet.fFleets) {
				add(anotherFleet);
			}
		}
		return this;
	}

	/**
	 * Gets Fleet of given player from this list.
	 */
	public Fleet forPlayerNr(int playerNr) {
		for (Fleet fleet : fFleets) {
			if (fleet.getPlayerNr() == playerNr) {
				return fleet;
			}
		}
		return null;
	}

	/**
	 * Returns number of fighters for this list.
	 */
	public int totalFighters() {
		int result = 0;
		for (Fleet fleet : fFleets) {
			result += fleet.getFighters();
		}
		return result;
	}

	/**
	 * Returns number of transporters for this list.
	 */
	public int totalTransporters() {
		int result = 0;
		for (Fleet fleet : fFleets) {
			result += fleet.getTransporters();
		}
		return result;
	}
	
	/**
	 * 
	 */
	public int totalShips() {
		int result = 0;
		for (Fleet fleet : fFleets) {
			result += fleet.getTransporters() + fleet.getFighters();
		}
		return result;
	}
	
	/**
	 * 
	 */
	public Cargo getCargo() {
		Cargo cargo = new Cargo();
		for (Fleet fleet : fFleets) {
			cargo.add(fleet.getCargo());
		}
		return cargo;
	}
	
	/**
	 * 
	 */
	public void removeTransporters() {
		for (Fleet fleet : fFleets) {
			fleet.setTransporters(0);
		}
	}

	/**
	 * Removes a Fleet from this set.
	 */
	public boolean remove(Fleet fleet) {
		return fFleets.remove(fleet);
	}

	/**
	 * Randomizes list order.
	 */
	public void shuffle() {
		Collections.shuffle(fFleets);
	}
	
	@Override
	public JsonArray toJson() {
		JsonArray jsonArray = new JsonArray();
		for (Fleet fleet : fFleets) {
			jsonArray.add(fleet.toJson());
		}
		return jsonArray;
	}
	
	@Override
	public FleetList fromJson(JsonValue jsonValue) {
		clear();
		JsonArray jsonArray = jsonValue.asArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			add(new Fleet().fromJson(jsonArray.get(i)));
		}
		return this;
	}

}
