package com.balancedbytes.game.ashes.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class PlayerList implements IJsonSerializable, Iterable<Player> {
	
	private List<Player> fPlayers;
	
	public PlayerList() {
		fPlayers = new ArrayList<Player>();
	}
	
	public void add(Player player) {
		if (player == null) {
			return;
		}
		fPlayers.add(player);
	}
	
	public void clear() {
		fPlayers.clear();
	}
	
	public int size() {
		return fPlayers.size();
	}
	
	public Player get(int index) {
		if ((index < 0) || index >= size()) {
			return null;
		}
		return fPlayers.get(index);
	}
	
	@Override
	public Iterator<Player> iterator() {
		return fPlayers.iterator();
	}
	
	@Override
	public JsonArray toJson() {
		JsonArray jsonArray = new JsonArray();
		for (Player player : fPlayers) {
			jsonArray.add(player.toJson());
		}
		return jsonArray;
	}

	@Override
	public PlayerList fromJson(JsonValue jsonValue) {
		clear();
		JsonArray jsonArray = jsonValue.asArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			add(new Player().fromJson(jsonArray.get(i)));
		}
		return this;
	}

}
