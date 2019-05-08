package com.balancedbytes.game.ashes.model;

import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

/**
 *
 */
public class Cargo implements IJsonSerializable {

	private int[] fCargos;
	
	/**
	 *
	 */
	protected Cargo() {
		this(0, 0);
	}
	
	/**
	 *
	 */
	public Cargo(int type, int quantity) {
		fCargos = new int[10];
		set(type, quantity);
	}

	/**
	 * Adds another fleet to this one.
	 */
	public Cargo add(Cargo anotherCargo) {
		if (anotherCargo != null) {
			for (int i = 0; i < fCargos.length; i++) {
				fCargos[i] = anotherCargo.get(i);
			}
		}
		return this;
	}

	/**
	 *
	 */
	public void clearCargo() {
		for (int i = 0; i < fCargos.length; i++) {
			fCargos[i] = 0;
		}
	}

	/**
	 *
	 */
	public int get(int type) {
		if ((type >= 0) && (type < fCargos.length)) {
			return fCargos[type];
		} else {
			return 0;
		}
	}

	/**
	 *
	 */
	protected Cargo set(int type, int count) {
		if ((type >= 0) && (type < fCargos.length)) {
			if (count > 0) {
				fCargos[type] = count;
			} else {
				fCargos[type] = 0;
			}
		}
		return this;
	}
	
	/**
	 * 
	 */
	public int total() {
		int total = 0;
		for (int i = 0; i < fCargos.length; i++) {
			total += fCargos[i];
		}
		return total;
	}
	
	@Override
	public JsonArray toJson() {
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < fCargos.length; i++) {
			jsonArray.add(get(i));
		}
		return jsonArray;
	}
	
	@Override
	public Cargo fromJson(JsonValue jsonValue) {
		JsonArray jsonArray = jsonValue.asArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			set(i, jsonArray.get(i).asInt());
		}
		return this;
	}

}