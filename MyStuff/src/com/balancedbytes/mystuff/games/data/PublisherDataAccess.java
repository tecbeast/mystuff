package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;

public class PublisherDataAccess extends RestDataAccess<Publisher> {

	private static final String _SQL_FIND_ALL_PUBLISHERS = 
		"SELECT * FROM publishers ORDER BY name";
	private static final String _SQL_FIND_PUBLISHER_BY_ID =
		"SELECT * FROM publishers WHERE id = ?";
	private static final String _SQL_FIND_PUBLISHERS_FILTERED =
		"SELECT * FROM publishers "
		+ " WHERE UPPER(name) LIKE ?"
		+ " ORDER BY name";
	private static final String _SQL_FIND_PUBLISHERS_BY_GAME_ID =
		"SELECT game_publishers.game_id, publishers.*"
		+ " FROM game_publishers LEFT JOIN publishers"
		+ " ON game_publishers.publisher_id = publishers.id"
		+ " WHERE game_publishers.game_id = ?"
		+ " ORDER BY publishers.name";
	private static final String _SQL_CREATE_PUBLISHER =
		"INSERT INTO publishers"
		+ " (name, country_code)"
		+ " VALUES (?, ?)";
	private static final String _SQL_UPDATE_PUBLISHER =
		"UPDATE publishers"
		+ " SET name = ?, country_code = ?"
		+ " WHERE id = ?";
	private static final String _SQL_DELETE_PUBLISHER =
		"DELETE FROM publishers WHERE id = ?";

    public Publishers findAllPublishers(RestDataPaging paging) throws SQLException {
    	Publishers publishers = new Publishers();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_PUBLISHERS);
            processResultSet(ps.executeQuery(), publishers, paging);
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
    
    public Publishers findPublishersFiltered(PublisherDataFilter filter, RestDataPaging paging) throws SQLException {
    	if ((filter == null) || filter.isEmpty()) {
    		return findAllPublishers(paging);
    	}
    	Publishers publishers = new Publishers();
    	String namePattern = filter.getName().trim().toUpperCase();
    	if (namePattern.length() == 0) {
    		return publishers;
    	}
    	namePattern = new StringBuilder().append("%").append(namePattern).append("%").toString();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_PUBLISHERS_FILTERED);
            ps.setString(1, namePattern);
            processResultSet(ps.executeQuery(), publishers, paging);
		}
        return publishers;
    }

    public Publishers findPublishersByGameId(String gameId, RestDataPaging paging) throws SQLException {
    	Publishers publishers = new Publishers();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_PUBLISHERS_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            processResultSet(ps.executeQuery(), publishers, paging);
		}
        return publishers;
    }

    public void createPublisher(Publisher publisher) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_CREATE_PUBLISHER, new String[] { "id" });
            ps.setString(1, publisher.getName());
            ps.setString(2, publisher.getCountry().getCode());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	publisher.setId(rs.getString(1));
            }
        }
    }
    
    public boolean updatePublisher(Publisher publisher) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_UPDATE_PUBLISHER);
            ps.setString(1, publisher.getName());
            ps.setString(2, publisher.getCountry().getCode());
            ps.setLong(3, MyStuffUtil.parseLong(publisher.getId()));
            return (ps.executeUpdate() == 1);
        }
    }
    
    public boolean deletePublisher(String id) throws SQLException {
    	int count = 0;
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_DELETE_PUBLISHER);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            count = ps.executeUpdate();
        }
        return (count == 1);
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
