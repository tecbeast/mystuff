package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;

public class AuthorDataAccess extends RestDataAccess<Author> {

	private static final String _SQL_FIND_ALL_AUTHORS = 
		"SELECT * FROM authors ORDER BY last_name,first_name";
	private static final String _SQL_FIND_AUTHOR_BY_ID =
		"SELECT * FROM authors WHERE id = ?";
	private static final String _SQL_FIND_AUTHORS_FILTERED =
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
	
    public Authors findAllAuthors(RestDataPaging paging) throws SQLException {
    	Authors authors = new Authors();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_AUTHORS);
            processResultSet(ps.executeQuery(), authors, paging);
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
    
    public Authors findAuthorsFiltered(AuthorDataFilter filter, RestDataPaging paging) throws SQLException {
    	if ((filter == null) || filter.isEmpty()) {
    		return findAllAuthors(paging);
    	}
    	Authors authors = new Authors();
    	String namePattern = filter.getName().trim().toUpperCase();
    	if (namePattern.length() == 0) {
    		return authors;
    	}
    	namePattern = new StringBuilder().append("%").append(namePattern).append("%").toString();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AUTHORS_FILTERED);
            ps.setString(1, namePattern);
            ps.setString(2, namePattern);
            processResultSet(ps.executeQuery(), authors, paging);
		}
        return authors;
    }

    public Authors findAuthorsByGameId(String gameId, RestDataPaging paging) throws SQLException {
    	Authors authors = new Authors();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_AUTHORS_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            processResultSet(ps.executeQuery(), authors, paging);
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
