package com.balancedbytes.mystuff.games;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.balancedbytes.mystuff.games.data.CountryDataAccess;

public class CountryCache {

	private static final CountryCache _INSTANCE = new CountryCache();
	
	private Map<String, Country> countryByCode;
	private CountryDataAccess countryDataAccess;
	
	private CountryCache() {
		countryByCode = new HashMap<>();
		countryDataAccess = new CountryDataAccess();
	}
	
	private void initInternal() throws SQLException {
		countryByCode.clear();
		List<Country> countries = countryDataAccess.findAll();
		for (Country country : countries) {
			addInternal(country);
		}
	}
	
	private void addInternal(Country country) {
		if ((country == null) || (country.getCode() == null)) {
			return;
		}
		countryByCode.put(country.getCode(), country);
	}
	
	private Country getInternal(String countryCode) {
		return countryByCode.get(countryCode);
	}
	
	public static void init() throws SQLException {
		_INSTANCE.initInternal();
	}
	
	public static Country get(String countryCode) {
		return _INSTANCE.getInternal(countryCode);
	}

}
