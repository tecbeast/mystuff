package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import com.balancedbytes.game.ashes.model.User;

public class UserDataAccess {

	private DbManager fDbManager;
	
	private static final String SQL_FIND_BY_USER_NAME =
		"SELECT * FROM users WHERE user_name = ?";
	private static final String SQL_CREATE =
		"INSERT INTO users"
		+ " (user_name, real_name, email, secret, registered, last_processed, games_joined, games_finished, games_won)"
		+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE =
		"UPDATE users"
		+ " real_name = ?, email = ?, secret = ?, last_processed = ?, games_joined = ?, games_finished = ?, games_won = ?"
		+ " WHERE id = ?";
	private static final String SQL_DELETE =
		"DELETE FROM users WHERE id = ?";
	
	protected UserDataAccess(DbManager dbManager) {
		fDbManager = dbManager;
	}
	
	public User findByUserName(String userName) throws SQLException {
		if (userName == null) {
			return null;
		}
		User user = null;
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_USER_NAME);
			ps.setString(1, userName);
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
			PreparedStatement ps = c.prepareStatement(SQL_CREATE, new String[] { "id" });
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getRealName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getSecret());
			ps.setDate(5, new Date(System.currentTimeMillis()));
			ps.setDate(6, new Date(System.currentTimeMillis()));
			ps.setInt(7, user.getGamesJoined());
			ps.setInt(8, user.getGamesFinished());
			ps.setInt(9, user.getGamesWon());
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	user.setId(rs.getLong(1));
            }
			return success;
		}
	}

	public boolean update(User user) throws SQLException {
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_UPDATE);
			ps.setString(1, user.getRealName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getSecret());
			Date lastProcessed = (user.getLastProcessed() != null) ? new Date(user.getLastProcessed().getTime()) : null;
			ps.setDate(4, lastProcessed);			
			ps.setInt(5, user.getGamesJoined());
			ps.setInt(6, user.getGamesFinished());
			ps.setInt(7, user.getGamesWon());
			ps.setLong(8, user.getId());
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
		user.setUserName(rs.getString("user_name"));
		user.setRealName(rs.getString("real_name"));
		user.setEmail(rs.getString("email"));
		user.setSecret(rs.getString("secret"));
		user.setRegistered(rs.getDate("registered"));
		user.setLastProcessed(rs.getDate("last_processed"));
		user.setGamesJoined(rs.getInt("games_joined"));
		user.setGamesFinished(rs.getInt("games_finished"));
		user.setGamesWon(rs.getInt("games_won"));
		return user;
	}

}