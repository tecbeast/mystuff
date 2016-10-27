package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.games.CountryCache;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;

public class PublisherDataAccess extends RestDataAccess<Publisher> {

	private static final String _SQL_FIND_ALL_PUBLISHERS = 
		"SELECT * FROM publishers ORDER BY name";
	private static final String _SQL_FIND_PUBLISHER_BY_ID =
		"SELECT * FROM publishers WHERE id = ?";
	private static final String _SQL_FIND_PUBLISHERS_BY_NAME =
		"SELECT * FROM publishers "
		+ " WHERE UPPER(name) LIKE ?"
		+ " ORDER BY name";
	private static final String _SQL_FIND_PUBLISHERS_BY_GAME_ID =
		"SELECT game_publishers.game_id, publishers.*"
		+ " FROM game_publishers LEFT JOIN publishers"
		+ " ON game_publishers.publisher_id = publishers.id"
		+ " WHERE game_publishers.game_id = ?"
		+ " ORDER BY publishers.name";

    public Publishers findAllPublishers() throws SQLException {
    	Publishers publishers = new Publishers();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_PUBLISHERS);
            processResultSet(ps.executeQuery(), publishers);
		}
        return publishers;
    }

    public Publisher findPublisherById(String id) throws SQLException {
    	Publisher publisher = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_PUBLISHER_BY_ID);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            publisher = processResultSet(ps.executeQuery());
		}
        return publisher;
    }
    
    public Publishers findPublishersByName(String name) throws SQLException {
    	Publishers publishers = new Publishers();
    	if (name == null) {
    		return publishers;
    	}
    	String pattern = name.trim().toUpperCase();
    	if (pattern.length() == 0) {
    		return publishers;
    	}
    	pattern = new StringBuilder().append("%").append(pattern).append("%").toString();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_PUBLISHERS_BY_NAME);
            ps.setString(1, pattern);
            processResultSet(ps.executeQuery(), publishers);
		}
        return publishers;
    }

    public Publishers findPublishersByGameId(String gameId) throws SQLException {
    	Publishers publishers = new Publishers();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_PUBLISHERS_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            processResultSet(ps.executeQuery(), publishers);
		}
        return publishers;
    }

    @Override
    protected Publisher processRow(ResultSet rs) throws SQLException {
    	Publisher publisher = new Publisher();
    	publisher.setId(rs.getString("id"));
    	publisher.setName(rs.getString("name"));
    	publisher.setCountry(CountryCache.get(rs.getString("country_code")));
        return publisher;
    }
    
}
