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

import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.PlanetList;
import com.balancedbytes.game.ashes.model.PlayerList;
import com.eclipsesource.json.Json;

public class GameDataAccess {

	private static final String CHARSET = "UTF-8";
	
	private static final String SQL_FIND_BY_NUMBER =
		"SELECT * FROM games WHERE number = ?";
	private static final String SQL_CREATE =
		"INSERT INTO games"
		+ " (number, turn, last_update, player_list, planet_list)"
		+ " VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE =
		"UPDATE games"
		+ " SET turn = ?, last_update = ?, player_list = ?, planet_list = ?"
		+ " WHERE id = ?";
	private static final String SQL_DELETE =
		"DELETE FROM games WHERE id = ?";

	private DbManager fDbManager;
	
	protected GameDataAccess(DbManager dbManager) {
		fDbManager = dbManager;
	}

	public Game findByNumber(int gameNr) throws SQLException {
		Game game = null;
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_NUMBER);
			ps.setInt(1, gameNr);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
	        	game = processRow(rs);
	        }
			c.commit();
		}
		return game;
	}

	public boolean create(Game game) throws SQLException {
		if (game == null) {
			return false;
		}
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_CREATE, new String[] { "id" });
			ps.setInt(1, game.getTurn());
			ps.setTimestamp(2, (game.getLastUpdate() != null) ? new Timestamp(game.getLastUpdate().getTime()) : null);
			ps.setBlob(3, createPlayerListBlob(c, game));
			ps.setBlob(4, createPlanetListBlob(c, game));
			boolean success = (ps.executeUpdate() > 0);
			c.commit();
			ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	game.setId(rs.getLong(1));
            }
			return success;
		}
	}
	
	public boolean update(Game game) throws SQLException {
		if (game == null) {
			return false;
		}
		try (Connection c = fDbManager.getConnection()) {
			PreparedStatement ps = c.prepareStatement(SQL_UPDATE);
			ps.setInt(1, game.getNumber());
			ps.setInt(2, game.getTurn());
			ps.setTimestamp(3, (game.getLastUpdate() != null) ? new Timestamp(game.getLastUpdate().getTime()) : null);
			ps.setBlob(4, createPlayerListBlob(c, game));
			ps.setBlob(5, createPlanetListBlob(c, game));
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

	protected Game processRow(ResultSet rs) throws SQLException {
		Game game = new Game();
		game.setId(rs.getLong("id"));
		game.setNumber(rs.getInt("number"));
		game.setTurn(rs.getInt("turn"));
		game.setLastUpdate(rs.getTime("last_update"));
		game.setPlayers(readPlayerList(rs.getBinaryStream("player_list")));
		game.setPlanets(readPlanetList(rs.getBinaryStream("planet_list")));
		return game;
	}
	
	private PlayerList readPlayerList(InputStream binaryStream) throws SQLException {
		if (binaryStream == null) {
			return null;
		}
		try (Reader in = new InputStreamReader(new GZIPInputStream(binaryStream), CHARSET)) {
			return new PlayerList().fromJson(Json.parse(in).asArray());
		} catch (IOException ioe) {
			throw new SQLException("Error on reading playerList from binary stream.", ioe);
		}
	}

	private PlanetList readPlanetList(InputStream binaryStream) throws SQLException {
		if (binaryStream == null) {
			return null;
		}
		try (Reader in = new InputStreamReader(new GZIPInputStream(binaryStream), CHARSET)) {
			return new PlanetList().fromJson(Json.parse(in).asArray());
		} catch (IOException ioe) {
			throw new SQLException("Error on reading planetList from binary stream.", ioe);
		}
	}

	private Blob createPlayerListBlob(Connection connection, Game game) throws SQLException {
		if ((connection == null) || (game == null) || (game.getPlayers() == null) || (game.getPlayers().size() == 0)) {
			return null;
		}
		Blob blob = connection.createBlob();
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();		
		try (Writer out = new OutputStreamWriter(new GZIPOutputStream(byteArrayOut), CHARSET)) {
			game.getPlayers().toJson().writeTo(out);
			blob.setBytes(0, byteArrayOut.toByteArray());
			return blob;
		} catch (IOException ioe) {
			throw new SQLException("Error on writing playerList to binary stream.", ioe);
		}
	}
	
	private Blob createPlanetListBlob(Connection connection, Game game) throws SQLException {
		if ((connection == null) || (game == null) || (game.getPlanets() == null) || (game.getPlanets().size() == 0)) {
			return null;
		}
		Blob blob = connection.createBlob();
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();		
		try (Writer out = new OutputStreamWriter(new GZIPOutputStream(byteArrayOut), CHARSET)) {
			game.getPlanets().toJson().writeTo(out);
			blob.setBytes(0, byteArrayOut.toByteArray());
			return blob;
		} catch (IOException ioe) {
			throw new SQLException("Error on writing playerList to binary stream.", ioe);
		}
	}

}