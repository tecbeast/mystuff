package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.games.CountryCache;
import com.balancedbytes.mystuff.games.Publisher;

public class PublisherDataAccess {

	private static final String _SQL_FIND_ALL = 
		"SELECT * FROM publishers";
	private static final String _SQL_FIND_BY_ID =
		"SELECT * FROM publishers WHERE id = ?";
	private static final String _SQL_FIND_ALL_WITH_GAME_ID =
		"SELECT game_publishers.game_id, publishers.*"
		+ " FROM game_publishers LEFT JOIN publishers"
		+ " ON game_publishers.publisher_id = publishers.id"
		+ " WHERE game_publishers.game_id = ?"
		+ " ORDER BY publishers.name";

    public List<Publisher> findAll() throws SQLException {
    	List<Publisher> publishers = new ArrayList<>();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                publishers.add(processRow(rs));
            }
		} finally {
			// connection will be automatically closed by try-with-resource
		}
        return publishers;
    }

    public Publisher findById(long id) throws SQLException {
    	Publisher publisher = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_BY_ID);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                publisher = processRow(rs);
            }
		} finally {
			// connection will be automatically closed by try-with-resource
		}
        return publisher;
    }

    public List<Publisher> findAllWithGameId(long gameId) throws SQLException {
    	List<Publisher> publishers = new ArrayList<>();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_WITH_GAME_ID);
            ps.setLong(1, gameId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                publishers.add(processRow(rs));
            }
		} finally {
			// connection will be automatically closed by try-with-resource
		}
        return publishers;
    }

    private Publisher processRow(ResultSet rs) throws SQLException {
    	Publisher publisher = new Publisher();
    	publisher.setId(rs.getLong("id"));
    	publisher.setName(rs.getString("name"));
    	publisher.setCountry(CountryCache.get(rs.getString("country_code")));
        return publisher;
    }
    
}
