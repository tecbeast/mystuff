package com.balancedbytes.game.ashes.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Categories for Gross National Product
 */
public enum Category {

	PLANETS,
	FUEL_PLANTS,
	ORE_PLANTS,
	RARE_PLANTS,
	FIGHTER_YARDS,
	TRANSPORTER_YARDS,
	PLANETARY_DEFENSE_UNITS,
	STOCK_PILES,
	FIGHTERS,
	TRANSPORTERS,
	GROSS_INDUSTRIAL_PRODUCT,
	POLITICAL_POINTS;
	
	public static Map<Category, Integer> buildEmptyMap() {
		Map<Category, Integer> map = new HashMap<Category, Integer>();
		for (Category category : values()) {
			map.put(category, 0);
		}
		return map;
	}
	
}
