package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import com.balancedbytes.game.ashes.model.User;

public class UserDataAccess {

	private DbManager fDbManager;
	
	private static final String SQL_FIND_USER_BY_ID =
		"SELECT * FROM users WHERE id = ?";
	private static final String SQL_CREATE_USER =
		"INSERT INTO users"
		+ " (id, name, email, registered, last_processed, games_joined, games_finished, games_won)"
		+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE_USER =
		"UPDATE users"
		+ " SET name = ?, email = ?, last_processed = ?, games_joined = ?, games_finished = ?, games_won = ?"
		+ " WHERE id = ?";
	private static final String SQL_DELETE_USER =
		"DELETE FROM users WHERE id = ?";
	
	protected UserDataAccess(DbManager dbManager) {
		fDbManager = dbManager;
	}
	
	public User findUserById(String id) throws SQLException {
		User user = null;
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_USER_BY_ID);
			ps.setString(1, id);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	        	user = processRow(rs);
	        }
			c.commit();
		}
		return user;
	}

	public boolean createUser(User user) throws SQLException {
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_CREATE_USER);
			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			Date registered = (user.getRegistered() != null) ? new Date(user.getRegistered().getTime()) : null;
			ps.setDate(4, registered);
			Date lastProcessed = (user.getLastProcessed() != null) ? new Date(user.getLastProcessed().getTime()) : null;
			ps.setDate(5, lastProcessed);
			ps.setInt(6, user.getGamesJoined());
			ps.setInt(7, user.getGamesFinished());
			ps.setInt(8, user.getGamesWon());
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			return success;
		}
	}

	public boolean updateUser(User user) throws SQLException {
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_UPDATE_USER);
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			Date lastProcessed = (user.getLastProcessed() != null) ? new Date(user.getLastProcessed().getTime()) : null;
			ps.setDate(3, lastProcessed);			
			ps.setInt(4, user.getGamesJoined());
			ps.setInt(5, user.getGamesFinished());
			ps.setInt(6, user.getGamesWon());
			ps.setString(7, user.getId());
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			return success;
		}
	}

	public boolean deleteAuthor(String id) throws SQLException {
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_DELETE_USER);
			ps.setString(1, id);
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			return success;
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