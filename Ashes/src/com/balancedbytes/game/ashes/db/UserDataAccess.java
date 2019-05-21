package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import com.balancedbytes.game.ashes.model.User;

public class UserDataAccess {

	private DbManager fDbManager;
	
	private static final String SQL_FIND_BY_NAME =
		"SELECT * FROM users WHERE name = ?";
	private static final String SQL_CREATE =
		"INSERT INTO users"
		+ " (name, real_name, email, registered, last_processed, games_joined, games_finished, games_won)"
		+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE =
		"UPDATE users"
		+ " real_name = ?, email = ?, last_processed = ?, games_joined = ?, games_finished = ?, games_won = ?"
		+ " WHERE id = ?";
	private static final String SQL_DELETE =
		"DELETE FROM users WHERE id = ?";
	
	protected UserDataAccess(DbManager dbManager) {
		fDbManager = dbManager;
	}
	
	public User findByName(String name) throws SQLException {
		if (name == null) {
			return null;
		}
		User user = null;
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_NAME);
			ps.setString(1, name);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	        	user = processRow(rs);
	        }
			c.commit();
		}
		return user;
	}

	public boolean create(User user) throws SQLException {
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_CREATE);
			ps.setString(1, user.getName());
			ps.setString(2, user.getRealName());
			ps.setString(3, user.getEmail());
			ps.setDate(4, new Date(System.currentTimeMillis()));
			ps.setDate(5, new Date(System.currentTimeMillis()));
			ps.setInt(6, user.getGamesJoined());
			ps.setInt(7, user.getGamesFinished());
			ps.setInt(8, user.getGamesWon());
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			return success;
		}
	}

	public boolean update(User user) throws SQLException {
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_UPDATE);
			ps.setString(1, user.getRealName());
			ps.setString(2, user.getEmail());
			Date lastProcessed = (user.getLastProcessed() != null) ? new Date(user.getLastProcessed().getTime()) : null;
			ps.setDate(3, lastProcessed);			
			ps.setInt(4, user.getGamesJoined());
			ps.setInt(5, user.getGamesFinished());
			ps.setInt(6, user.getGamesWon());
			ps.setLong(7, user.getId());
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			return success;
		}
	}

	public boolean delete(long id) throws SQLException {
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_DELETE);
			ps.setLong(1, id);
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			return success;
		}
	}

	protected User processRow(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setName(rs.getString("name"));
		user.setRealName(rs.getString("real_name"));
		user.setEmail(rs.getString("email"));
		user.setRegistered(rs.getDate("registered"));
		user.setLastProcessed(rs.getDate("last_processed"));
		user.setGamesJoined(rs.getInt("games_joined"));
		user.setGamesFinished(rs.getInt("games_finished"));
		user.setGamesWon(rs.getInt("games_won"));
		return user;
	}

}