package com.balancedbytes.game.ashes.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Categories for Gross National Product
 */
public enum GnpCategory {

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
	
	public static Map<GnpCategory, Integer> buildEmptyMap() {
		Map<GnpCategory, Integer> map = new HashMap<GnpCategory, Integer>();
		for (GnpCategory category : values()) {
			map.put(category, 0);
		}
		return map;
	}
	
}
