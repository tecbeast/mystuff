package com.balancedbytes.mystuff.games.data.filter;

import java.util.SortedMap;
import java.util.TreeMap;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.rest.RestDataFilter;

public class GameDataFilter extends RestDataFilter {

	private String title;
	private int minPlayers;
	private int maxPlayers;
	private int year;
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getMinPlayers() {
		return this.minPlayers;
	}
	
	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}
	
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public boolean isEmpty() {
		return !MyStuffUtil.isProvided(this.title);
	}
	
	@Override
	public SortedMap<String, Object> toSortedMap() {
		SortedMap<String, Object> map = new TreeMap<String, Object>();
		if (MyStuffUtil.isProvided(this.title)) {
			map.put("name", this.title);
		}
		if (this.minPlayers > 0) {
			map.put("minPlayers", this.minPlayers);
		}
		if (this.maxPlayers > 0) {
			map.put("maxPlayers", this.maxPlayers);
		}
		if (this.year > 0) {
			map.put("year", this.year);
		}
		return map;
	}

}
