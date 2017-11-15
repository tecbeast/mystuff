package com.balancedbytes.mystuff.games.data.filter;

import java.util.SortedMap;
import java.util.TreeMap;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.rest.RestDataFilter;

public class AwardDataFilter extends RestDataFilter {

	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean isEmpty() {
		return !MyStuffUtil.isProvided(name);
	}
	
	@Override
	public SortedMap<String, Object> toSortedMap() {
		SortedMap<String, Object> map = new TreeMap<String, Object>();
		if (MyStuffUtil.isProvided(name)) {
			map.put("name", name);
		}
		return map;
	}

}
