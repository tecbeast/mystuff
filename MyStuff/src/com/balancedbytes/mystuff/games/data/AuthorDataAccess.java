package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.CountryCache;

public class AuthorDataAccess {

	private static final String _SQL_FIND_ALL = 
		"SELECT * FROM authors";
	private static final String _SQL_FIND_BY_ID =
		"SELECT * FROM authors WHERE id = ?";
	private static final String _SQL_FIND_ALL_WITH_GAME_ID =
		"SELECT game_authors.game_id, authors.*"
		+ " FROM game_authors LEFT JOIN authors"
		+ " ON game_authors.author_id = authors.id"
		+ " WHERE game_authors.game_id = ?"
		+ " ORDER BY authors.last_name";

    public List<Author> findAll() throws SQLException {
    	List<Author> authors = new ArrayList<>();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                authors.add(processRow(rs));
            }
		} finally {
			// connection will be automatically closed by try-with-resource
		}
        return authors;
    }

    public Author findById(long id) throws SQLException {
    	Author author = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_BY_ID);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                author = processRow(rs);
            }
		} finally {
			// connection will be automatically closed by try-with-resource
		}
        return author;
    }

    public List<Author> findAllWithGameId(long gameId) throws SQLException {
    	List<Author> authors = new ArrayList<>();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_WITH_GAME_ID);
            ps.setLong(1, gameId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                authors.add(processRow(rs));
            }
		} finally {
			// connection will be automatically closed by try-with-resource
		}
        return authors;
    }

    private Author processRow(ResultSet rs) throws SQLException {
    	Author author = new Author();
    	author.setId(rs.getLong("id"));
    	author.setFirstName(rs.getString("first_name"));
    	author.setLastName(rs.getString("last_name"));
    	author.setCountry(CountryCache.get(rs.getString("country_code")));
        return author;
    }
    
}
