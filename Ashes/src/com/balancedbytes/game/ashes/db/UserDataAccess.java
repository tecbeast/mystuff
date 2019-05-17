package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.balancedbytes.game.ashes.model.User;

public class UserDataAccess {

	private static final String SQL_FIND_USER_BY_ID =
		"SELECT * FROM users WHERE id = ?";
	private static final String SQL_CREATE_USER =
		"INSERT INTO users"
		+ " (last_name, first_name, country_code)"
		+ " VALUES (?, ?, ?)";
	private static final String SQL_UPDATE_USER =
		"UPDATE users"
		+ " SET last_name = ?, first_name = ?, country_code = ?"
		+ " WHERE id = ?";
	private static final String SQL_DELETE_USER =
		"DELETE FROM users WHERE id = ?";
	
	public User findUserById(String id) throws SQLException {
		User user = null;
		try (Connection c = ConnectionHelper.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_USER_BY_ID);
			ps.setString(1, id);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	        	user = processRow(rs);
	        }
		}
		return user;
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
		try (Connection c = ConnectionHelper.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_AUTHORS_FILTERED);
			ps.setString(1, namePattern);
			ps.setString(2, namePattern);
			processResultSet(ps.executeQuery(), authors, paging);
		}
		return authors;
	}

	public Authors findAuthorsByGameId(String gameId, RestDataPaging paging) throws SQLException {
		Authors authors = new Authors();
		try (Connection c = ConnectionHelper.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_AUTHORS_BY_GAME_ID);
			ps.setLong(1, MyStuffUtil.parseLong(gameId));
			processResultSet(ps.executeQuery(), authors, paging);
		}
		return authors;
	}

	public void createAuthor(Author author) throws SQLException {
		try (Connection c = ConnectionHelper.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_CREATE_AUTHOR, new String[] { "id" });
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
			PreparedStatement ps = c.prepareStatement(SQL_UPDATE_AUTHOR);
			ps.setString(1, author.getLastName());
			ps.setString(2, author.getFirstName());
			ps.setString(3, author.getCountry().getCode());
			ps.setLong(4, MyStuffUtil.parseLong(author.getId()));
			return (ps.executeUpdate() == 1);
		}
	}

	public boolean deleteAuthor(String id) throws SQLException {
		try (Connection c = ConnectionHelper.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_DELETE_AUTHOR);
			ps.setLong(1, MyStuffUtil.parseLong(id));
			return (ps.executeUpdate() == 1);
		}
	}

	protected User processRow(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setRegistered(rs.getDate("registered"));
		user.setLastProcessed(rs.getDate("last_processed"));
		user.setGamesJoined(rs.getInt("games_joined"));
		user.setGamesFinished(rs.getInt("games_finished"));
		user.setGamesWon(rs.getInt("games_won"));
		return user;
	}

}