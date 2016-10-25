package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.games.Game;

public class GameDataAccess {
	
	private static final String _SQL_FIND_ALL = 
		"SELECT * FROM games";
	private static final String _SQL_FIND_BY_ID =
		"SELECT * FROM games WHERE id = ?";

    public List<Game> findAll() throws SQLException {
    	List<Game> games = new ArrayList<>();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                games.add(processRow(rs));
            }
		} finally {
			// connection will be automatically closed by try-with-resource
		}
        return games;
    }
    
    public Game findById(long id) throws SQLException {
    	Game game = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_BY_ID);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                game = processRow(rs);
            }
		} finally {
			// connection will be automatically closed by try-with-resource
		}
        return game;
    }

    private Game processRow(ResultSet rs) throws SQLException {
    	Game game = new Game();
    	game.setId(rs.getLong("id"));
    	game.setName(rs.getString("name"));
    	game.setEditionYear(rs.getInt("edition_year"));
    	game.setPlayersMin(rs.getInt("players_min"));
    	game.setPlayersMax(rs.getInt("players_max"));
    	game.setPlaytimeMin(rs.getInt("playtime_min"));
    	game.setPlaytimeMax(rs.getInt("playtime_max"));
    	game.setPlaytimePerPlayer(rs.getBoolean("playtime_per_player"));
    	game.setAgeMin(rs.getInt("age_min"));
    	game.setLastPlayed(rs.getDate("last_played"));
    	game.setRating(rs.getInt("rating"));
        return game;
    }
    
}
