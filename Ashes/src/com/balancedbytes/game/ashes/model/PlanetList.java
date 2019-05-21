package com.balancedbytes.game.ashes.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class PlanetList implements IJsonSerializable, Iterable<Planet> {
	
	private List<Planet> fPlanets;
	
	public PlanetList() {
		fPlanets = new ArrayList<Planet>();
	}
	
	public void add(Planet planet) {
		if (planet == null) {
			return;
		}
		fPlanets.add(planet);
	}
	
	public void clear() {
		fPlanets.clear();
	}
	
	public int size() {
		return fPlanets.size();
	}
	
	public Planet get(int index) {
		if ((index < 0) || index >= size()) {
			return null;
		}
		return fPlanets.get(index);
	}
	
	@Override
	public Iterator<Planet> iterator() {
		return fPlanets.iterator();
	}
	
	@Override
	public JsonArray toJson() {
		JsonArray jsonArray = new JsonArray();
		for (Planet planet : fPlanets) {
			jsonArray.add(planet.toJson());
		}
		return jsonArray;
	}

	@Override
	public PlanetList fromJson(JsonValue jsonValue) {
		clear();
		JsonArray jsonArray = jsonValue.asArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			add(new Planet().fromJson(jsonArray.get(i)));
		}
		return this;
	}

}
