package com.balancedbytes.mystuff.games.data;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.balancedbytes.mystuff.games.Country;

public class CountryCache {

	private static Map<String, Country> countryByCode = new HashMap<>();
	private static CountryDataAccess countryDataAccess = new CountryDataAccess();
	
	private static void add(Country country) {
		if ((country == null) || (country.getCode() == null)) {
			return;
		}
		countryByCode.put(country.getCode(), country);
	}
	
	public static void init() throws SQLException {
		countryByCode.clear();
		List<Country> countries = countryDataAccess.findAllCountries();
		for (Country country : countries) {
			add(country);
		}
	}
	
	public static Country get(String countryCode) {
		return countryByCode.get(countryCode);
	}

}
