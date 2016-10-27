package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.CountryCache;

public class AwardDataAccess extends RestDataAccess<Award> {

	private static final String _SQL_FIND_ALL_AWARDS = 
		"SELECT * FROM awards ORDER BY name";
	private static final String _SQL_FIND_AWARD_BY_ID =
		"SELECT * FROM awards WHERE id = ?";
	private static final String _SQL_FIND_AWARDS_BY_NAME =
		"SELECT * FROM awards "
		+ " WHERE UPPER(name) LIKE ?"
		+ " ORDER BY name";
	private static final String _SQL_FIND_AWARDS_BY_GAME_ID =
		"SELECT game_awards.game_id, awards.*, game_awards.year"
		+ " FROM game_awards LEFT JOIN awards"
		+ " ON game_awards.award_id = awards.id"
		+ " WHERE game_awards.game_id = ?"
		+ " ORDER BY awards.name";
	
    public Awards findAllAwards() throws SQLException {
    	Awards awards = new Awards();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_AWARDS);
            processResultSet(ps.executeQuery(), awards);
		}
        return awards;
    }

    public Award findAwardById(String id) throws SQLException {
    	Award award = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AWARD_BY_ID);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            award = processResultSet(ps.executeQuery());
		}
        return award;
    }
    
    public Awards findAwardsByName(String name) throws SQLException {
    	Awards awards = new Awards();
    	if (name == null) {
    		return awards;
    	}
    	String pattern = name.trim().toUpperCase();
    	if (pattern.length() == 0) {
    		return awards;
    	}
    	pattern = new StringBuilder().append("%").append(pattern).append("%").toString();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AWARDS_BY_NAME);
            ps.setString(1, pattern);
            processResultSet(ps.executeQuery(), awards);
		}
        return awards;
    }

    public Awards findAwardsByGameId(String gameId) throws SQLException {
    	Awards awards = new Awards();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AWARDS_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            processResultSet(ps.executeQuery(), awards);
		}
        return awards;
    }

    @Override
    protected Award processRow(ResultSet rs) throws SQLException {
    	Award award = new Award();
    	award.setId(rs.getString("id"));
    	award.setName(rs.getString("name"));
    	award.setCountry(CountryCache.get(rs.getString("country_code")));
    	if (rs.getMetaData().getColumnCount() > 3) {
    		award.setYear(rs.getInt("year"));
    	}
        return award;
    }
    
}
