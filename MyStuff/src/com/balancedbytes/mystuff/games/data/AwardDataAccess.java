package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;

public class AwardDataAccess extends RestDataAccess<Award> {

	private static final String _SQL_FIND_ALL_AWARDS = 
		"SELECT * FROM awards ORDER BY name";
	private static final String _SQL_FIND_AWARD_BY_ID =
		"SELECT * FROM awards WHERE id = ?";
	private static final String _SQL_FIND_AWARDS_FILTERED =
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
	private static final String _SQL_UPDATE_AWARD =
		"UPDATE awards"
		+ " SET name = ?, country_code = ?"
		+ " WHERE id = ?";
	private static final String _SQL_DELETE_AWARD =
		"DELETE FROM awards WHERE id = ?";

    public Awards findAllAwards(RestDataPaging paging) throws SQLException {
    	Awards awards = new Awards();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_AWARDS);
            processResultSet(ps.executeQuery(), awards, paging);
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
    
    public Awards findAwardsFiltered(AwardDataFilter filter, RestDataPaging paging) throws SQLException {
    	if ((filter == null) || filter.isEmpty()) {
    		return findAllAwards(paging);
    	}
    	Awards awards = new Awards();
    	String namePattern = filter.getName().trim().toUpperCase();
    	if (namePattern.length() == 0) {
    		return awards;
    	}
    	namePattern = new StringBuilder().append("%").append(namePattern).append("%").toString();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AWARDS_FILTERED);
            ps.setString(1, namePattern);
            processResultSet(ps.executeQuery(), awards, paging);
		}
        return awards;
    }

    public Awards findAwardsByGameId(String gameId, RestDataPaging paging) throws SQLException {
    	Awards awards = new Awards();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AWARDS_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            processResultSet(ps.executeQuery(), awards, paging);
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

    public boolean updateAward(Award award) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_UPDATE_AWARD);
            ps.setString(1, award.getName());
            ps.setString(2, award.getCountry().getCode());
            ps.setLong(3, MyStuffUtil.parseLong(award.getId()));
            return (ps.executeUpdate() == 1);
        }
    }

    public boolean deleteAward(String id) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_DELETE_AWARD);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            return (ps.executeUpdate() == 1);
        }
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
