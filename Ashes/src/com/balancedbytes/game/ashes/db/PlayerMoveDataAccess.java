package com.balancedbytes.game.ashes.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.model.PlayerMove;
import com.eclipsesource.json.Json;

public class PlayerMoveDataAccess {

	private static final String CHARSET = "UTF-8";
	
	private static final String SQL_FIND_BY_GAME_NR_PLAYER_NR_TURN =
		"SELECT * FROM player_moves WHERE game_nr = ? AND player_nr = ? AND turn = ?";
	private static final String SQL_CREATE =
		"INSERT INTO player_moves"
		+ " (game_nr, player_nr, turn, deadline, received, user_name, turn_secret, command_list)"
		+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE =
		"UPDATE player_moves"
		+ " SET deadline = ?, received = ?, user_name = ?, turn_secret = ?, command_list = ?"
		+ " WHERE id = ?";
	private static final String SQL_DELETE =
		"DELETE FROM player_moves WHERE id = ?";

	private DbManager fDbManager;
	
	protected PlayerMoveDataAccess(DbManager dbManager) {
		fDbManager = dbManager;
	}
	
	public PlayerMove findByGameNrPlayerNrTurn(int gameNr, int playerNr, int turn) throws SQLException {
		PlayerMove playerMove = null;
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_GAME_NR_PLAYER_NR_TURN);
			ps.setInt(1, gameNr);
			ps.setInt(2, playerNr);
			ps.setInt(3, turn);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	        	playerMove = processRow(rs);
	        }
			c.commit();
		}
		return playerMove;
	}
	
	public boolean create(PlayerMove move) throws SQLException {
		if (move == null) {
			return false;
		}
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_CREATE, new String[] { "id" });
			ps.setInt(1, move.getGameNr());
			ps.setInt(2, move.getPlayerNr());
			ps.setInt(3, move.getTurn());
			ps.setTimestamp(4, (move.getDeadline() != null) ? new Timestamp(move.getDeadline().getTime()) : null);
			ps.setTimestamp(5, (move.getReceived() != null) ? new Timestamp(move.getReceived().getTime()) : null);
			ps.setString(6, move.getUserName());
			ps.setString(7, move.getTurnSecret());
			ps.setBlob(8, createCommandListBlob(c, move));
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	move.setId(rs.getLong(1));
            }
			return success;
		}
	}
	
	public boolean update(PlayerMove move) throws SQLException {
		if (move == null) {
			return false;
		}
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_UPDATE);
			ps.setTimestamp(1, (move.getDeadline() != null) ? new Timestamp(move.getDeadline().getTime()) : null);
			ps.setTimestamp(2, (move.getReceived() != null) ? new Timestamp(move.getReceived().getTime()) : null);
			ps.setString(3, move.getUserName());
			ps.setString(4, move.getTurnSecret());
			ps.setBlob(5, createCommandListBlob(c, move));
			ps.setLong(6, move.getId());
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

	protected PlayerMove processRow(ResultSet rs) throws SQLException {
		PlayerMove playerMove = new PlayerMove();
		playerMove.setId(rs.getLong("id"));
		playerMove.setGameNr(rs.getInt("game_nr"));
		playerMove.setPlayerNr(rs.getInt("player_nr"));
		playerMove.setTurn(rs.getInt("turn"));
		playerMove.setDeadline(rs.getTimestamp("deadline"));
		playerMove.setReceived(rs.getTimestamp("received"));
		playerMove.setUserName(rs.getString("user_name"));
		playerMove.setTurnSecret(rs.getString("turn_secret"));
		playerMove.setCommands(readCommandList(rs.getBinaryStream("command_list")));
		return playerMove;
	}
	
	private CommandList readCommandList(InputStream binaryStream) throws SQLException {
		if (binaryStream == null) {
			return null;
		}
		try (Reader in = new InputStreamReader(new GZIPInputStream(binaryStream), CHARSET)) {
			return new CommandList().fromJson(Json.parse(in).asArray());
		} catch (IOException ioe) {
			throw new SQLException("Error on reading commandList from binary stream.", ioe);
		}
	}
	
	private Blob createCommandListBlob(Connection connection, PlayerMove move) throws SQLException {
		if ((connection == null) || (move == null) || (move.getCommands() == null) || (move.getCommands().size() == 0)) {
			return null;
		}
		Blob blob = connection.createBlob();
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();		
		try (Writer out = new OutputStreamWriter(new GZIPOutputStream(byteArrayOut), CHARSET)) {
			move.getCommands().toJson().writeTo(out);
			blob.setBytes(0, byteArrayOut.toByteArray());
			return blob;
		} catch (IOException ioe) {
			throw new SQLException("Error on writing commandList to binary stream.", ioe);
		}
	}

}