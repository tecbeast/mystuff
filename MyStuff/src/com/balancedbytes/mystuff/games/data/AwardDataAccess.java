package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.CountryCache;

public class AwardDataAccess {

	private static final String _SQL_FIND_ALL_AWARDS = 
		"SELECT * FROM awards ORDER BY name";
	private static final String _SQL_FIND_AWARD_BY_ID =
		"SELECT * FROM awards WHERE id = ?";
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
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                awards.add(processRow(rs, false));
            }
		}
        return awards;
    }

    public Award findAwardById(String id) throws SQLException {
    	Award award = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AWARD_BY_ID);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                award = processRow(rs, false);
            }
		}
        return award;
    }

    public Awards findAwardsByGameId(String gameId) throws SQLException {
    	Awards awards = new Awards();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AWARDS_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                awards.add(processRow(rs, true));
            }
		}
        return awards;
    }

    private Award processRow(ResultSet rs, boolean withYear) throws SQLException {
    	Award award = new Award();
    	award.setId(rs.getString("id"));
    	award.setName(rs.getString("name"));
    	award.setCountry(CountryCache.get(rs.getString("country_code")));
		award.setYear(withYear ? rs.getInt("year") : null);
        return award;
    }
    
}
