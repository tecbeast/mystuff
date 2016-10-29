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
	private static final String _SQL_CREATE_AWARD =
		"INSERT INTO awards"
		+ " (name, country_code)"
		+ " VALUES (?, ?)";
	private static final String _SQL_DELETE_AWARD =
		"DELETE FROM awards WHERE id = ?";

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
    
    public void createAward(Award award) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_CREATE_AWARD, new String[] { "id" });
            ps.setString(1, award.getName());
            ps.setString(2, award.getCountry().getCode());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	award.setId(rs.getString(1));
            }
        }
    }
    
    public boolean deleteAward(String id) throws SQLException {
    	int count = 0;
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_DELETE_AWARD);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            count = ps.executeUpdate();
        }
        return (count == 1);
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
