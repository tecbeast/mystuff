package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.games.Country;

public class CountryDataAccess {

	private static final String _SQL_FIND_ALL_COUNTRIES = 
		"SELECT * FROM countries";

    public List<Country> findAllCountries() throws SQLException {
    	List<Country> countries = new ArrayList<>();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_COUNTRIES);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                countries.add(processRow(rs));
            }
		}
        return countries;
    }

    private Country processRow(ResultSet rs) throws SQLException {
    	Country country = new Country();
    	country.setCode(rs.getString("code"));
    	country.setName(rs.getString("name"));
        return country;
    }
    
}
