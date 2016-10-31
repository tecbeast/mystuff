package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.CountryCache;

public class AuthorDataAccess extends RestDataAccess<Author> {

	private static final String _SQL_FIND_ALL_AUTHORS = 
		"SELECT * FROM authors ORDER BY last_name,first_name";
	private static final String _SQL_FIND_AUTHOR_BY_ID =
		"SELECT * FROM authors WHERE id = ?";
	private static final String _SQL_FIND_AUTHORS_BY_NAME =
		"SELECT * FROM authors "
		+ " WHERE UPPER(last_name) LIKE ? OR UPPER(first_name) LIKE ?"
		+ " ORDER BY last_name,first_name";
	private static final String _SQL_FIND_AUTHORS_BY_GAME_ID =
		"SELECT game_authors.game_id, authors.*"
		+ " FROM game_authors LEFT JOIN authors"
		+ " ON game_authors.author_id = authors.id"
		+ " WHERE game_authors.game_id = ?"
		+ " ORDER BY authors.last_name,authors.first_name";
	private static final String _SQL_CREATE_AUTHOR =
		"INSERT INTO authors"
		+ " (last_name, first_name, country_code)"
		+ " VALUES (?, ?, ?)";
	private static final String _SQL_UPDATE_AUTHOR =
		"UPDATE authors"
		+ " SET last_name = ?, first_name = ?, country_code = ?"
		+ " WHERE id = ?";
	private static final String _SQL_DELETE_AUTHOR =
		"DELETE FROM authors WHERE id = ?";

    public Authors findAllAuthors() throws SQLException {
    	Authors authors = new Authors();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_AUTHORS);
            processResultSet(ps.executeQuery(), authors);
		}
        return authors;
    }

    public Author findAuthorById(String id) throws SQLException {
    	Author author = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AUTHOR_BY_ID);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            author = processResultSet(ps.executeQuery());
		}
        return author;
    }
    
    public Authors findAuthorsByName(String name) throws SQLException {
    	Authors authors = new Authors();
    	if (name == null) {
    		return authors;
    	}
    	String pattern = name.trim().toUpperCase();
    	if (pattern.length() == 0) {
    		return authors;
    	}
    	pattern = new StringBuilder().append("%").append(pattern).append("%").toString();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AUTHORS_BY_NAME);
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            processResultSet(ps.executeQuery(), authors);
		}
        return authors;
    }

    public Authors findAuthorsByGameId(String gameId) throws SQLException {
    	Authors authors = new Authors();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AUTHORS_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            processResultSet(ps.executeQuery(), authors);
		}
        return authors;
    }
    
    public void createAuthor(Author author) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_CREATE_AUTHOR, new String[] { "id" });
            ps.setString(1, author.getLastName());
            ps.setString(2, author.getFirstName());
            ps.setString(3, author.getCountry().getCode());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	author.setId(rs.getString(1));
            }
        }
    }

    public boolean updateAuthor(Author author) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_UPDATE_AUTHOR);
            ps.setString(1, author.getLastName());
            ps.setString(2, author.getFirstName());
            ps.setString(3, author.getCountry().getCode());
            ps.setLong(4, MyStuffUtil.parseLong(author.getId()));
            return (ps.executeUpdate() == 1);
        }
    }

    public boolean deleteAuthor(String id) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_DELETE_AUTHOR);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            return (ps.executeUpdate() == 1);
        }
    }
    
    @Override
    protected Author processRow(ResultSet rs) throws SQLException {
    	Author author = new Author();
    	author.setId(rs.getString("id"));
    	author.setFirstName(rs.getString("first_name"));
    	author.setLastName(rs.getString("last_name"));
    	author.setCountry(CountryCache.get(rs.getString("country_code")));
        return author;
    }
    
}
