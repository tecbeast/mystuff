package com.balancedbytes.mystuff.games.data;

import java.util.SortedMap;
import java.util.TreeMap;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataFilter;

public class GameDataFilter extends RestDataFilter {

	private String name;
	private int minPlayers;
	private int maxPlayers;
	private int year;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMinPlayers() {
		return minPlayers;
	}
	
	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public boolean isEmpty() {
		return !MyStuffUtil.isProvided(name);
	}
	
	@Override
	protected SortedMap<String, Object> toSortedMap() {
		SortedMap<String, Object> map = new TreeMap<String, Object>();
		if (MyStuffUtil.isProvided(name)) {
			map.put("name", name);
		}
		if (minPlayers > 0) {
			map.put("minPlayers", minPlayers);
		}
		if (maxPlayers > 0) {
			map.put("maxPlayers", maxPlayers);
		}
		if (year > 0) {
			map.put("year", year);
		}
		return map;
	}

}
