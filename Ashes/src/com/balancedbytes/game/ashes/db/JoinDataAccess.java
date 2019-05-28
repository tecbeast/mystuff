package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.model.Join;

public class JoinDataAccess {
	
	private static final String SQL_FIND_ALL =
		"SELECT * FROM joins";
	private static final String SQL_FIND_BY_USER_NAME =
		"SELECT * FROM joins WHERE user_name = ?";
	private static final String SQL_FIND_BY_GAME_NAME =
		"SELECT * FROM joins WHERE game_name = ?";
	private static final String SQL_CREATE =
		"INSERT INTO joins"
		+ " (user_name, game_name, home_planets, joined)"
		+ " VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE =
		"UPDATE joins"
		+ " SET user_name = ?, game_name = ?, home_planets = ?, joined = ?"
		+ " WHERE id = ?";
	private static final String SQL_DELETE =
		"DELETE FROM joins WHERE id = ?";

	private DbManager fDbManager;
	
	protected JoinDataAccess(DbManager dbManager) {
		fDbManager = dbManager;
	}
	
	public List<Join> findAll() throws SQLException {
		List<Join> joins = new ArrayList<Join>();
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_ALL);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	        	joins.add(processRow(rs));
	        }
		}
		return joins;
	}
	
	public Join findByUserName(String userName) throws SQLException {
		Join join = null;
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_USER_NAME);
			ps.setString(1, userName);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	        	join = processRow(rs);
	        }
		}
		return join;
	}

	public List<Join> findByGameName(String gameName) throws SQLException {
		List<Join> joins = new ArrayList<Join>();
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_GAME_NAME);
			ps.setString(1, gameName);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	        	joins.add(processRow(rs));
	        }
		}
		return joins;
	}

	public boolean create(Join join) throws SQLException {
		if (join == null) {
			return false;
		}
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_CREATE, new String[] { "id" });
			ps.setString(1, join.getUserName());
			ps.setString(2, join.getGameName());
			ps.setString(3, AshesUtil.join(join.getHomePlanets(), ","));
			ps.setTimestamp(4, (join.getJoined() != null) ? new Timestamp(join.getJoined().getTime()) : null);
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	join.setId(rs.getLong(1));
            }
			return success;
		}
	}
	
	public boolean update(Join join) throws SQLException {
		if (join == null) {
			return false;
		}
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_UPDATE);
			ps.setString(1, join.getUserName());
			ps.setString(2, join.getGameName());
			ps.setString(3, AshesUtil.join(join.getHomePlanets(), ","));
			ps.setTimestamp(4, (join.getJoined() != null) ? new Timestamp(join.getJoined().getTime()) : null);
			ps.setLong(5, join.getId());
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

	protected Join processRow(ResultSet rs) throws SQLException {
		Join join = new Join();
		join.setId(rs.getLong("id"));
		join.setUserName(rs.getString("user_name"));
		join.setGameName(rs.getString("game_name"));
		String homePlanets = rs.getString("home_planets");
		join.setHomePlanets(AshesUtil.splitIntegerList(homePlanets, ","));
		join.setJoined(rs.getTimestamp("joined"));
		return join;
	}

}