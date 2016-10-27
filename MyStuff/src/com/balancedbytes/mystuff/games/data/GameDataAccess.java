package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Game;
import com.balancedbytes.mystuff.games.Games;

public class GameDataAccess extends RestDataAccess<Game> {
	
	private static final String _SQL_FIND_ALL_GAMES = 
		"SELECT * FROM games ORDER BY name";
	private static final String _SQL_FIND_GAME_BY_ID =
		"SELECT * FROM games WHERE id = ?";
	private static final String _SQL_FIND_GAMES_BY_NAME =
		"SELECT * FROM games WHERE UPPER(name) LIKE ? ORDER BY name";
	private static final String _SQL_FIND_GAMES_BY_AUTHOR_ID =
		"SELECT game_authors.author_id, games.*"
		+ " FROM game_authors LEFT JOIN games"
		+ " ON game_authors.game_id = games.id"
		+ " WHERE game_authors.author_id = ?"
		+ " ORDER BY games.name";
	private static final String _SQL_FIND_GAMES_BY_PUBLISHER_ID =
		"SELECT game_publishers.publisher_id, games.*"
		+ " FROM game_publishers LEFT JOIN games"
		+ " ON game_publishers.game_id = games.id"
		+ " WHERE game_publishers.publisher_id = ?"
		+ " ORDER BY games.name";
	private static final String _SQL_FIND_GAMES_BY_AWARD_ID =
		"SELECT game_awards.award_id, games.*"
		+ " FROM game_awards LEFT JOIN games"
		+ " ON game_awards.game_id = games.id"
		+ " WHERE game_awards.award_id = ?"
		+ " ORDER BY games.name";
	private static final String _SQL_FIND_GAMES_BY_AWARD_ID_AND_YEAR =
		"SELECT game_awards.award_id, games.*, game_awards.year"
		+ " FROM game_awards LEFT JOIN games"
		+ " ON game_awards.game_id = games.id"
		+ " WHERE game_awards.award_id = ? AND game_awards.year = ?"
		+ " ORDER BY games.name";

    public Games findAllGames() throws SQLException {
    	Games games = new Games();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_GAMES);
            processResultSet(ps.executeQuery(), games);
		}
        return games;
    }
    
    public Game findGameById(String id) throws SQLException {
    	Game game = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_GAME_BY_ID);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            game = processResultSet(ps.executeQuery());
		}
        return game;
    }
    
    public Games findGamesByName(String name) throws SQLException {
    	Games games = new Games();
    	if (name == null) {
    		return games;
    	}
    	String pattern = name.trim().toUpperCase();
    	if (pattern.length() == 0) {
    		return games;
    	}
    	pattern = new StringBuilder().append("%").append(pattern).append("%").toString();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_GAMES_BY_NAME);
            ps.setString(1, pattern);
            processResultSet(ps.executeQuery(), games);
		}
        return games;
    }
    
    public Games findGamesByAuthorId(String authorId) throws SQLException {
    	return findGamesBySomeId(_SQL_FIND_GAMES_BY_AUTHOR_ID, authorId);
    }

    public Games findGamesByPublisherId(String publisherId) throws SQLException {
    	return findGamesBySomeId(_SQL_FIND_GAMES_BY_PUBLISHER_ID, publisherId);
    }

    public Games findGamesByAwardId(String awardId) throws SQLException {
    	return findGamesBySomeId(_SQL_FIND_GAMES_BY_AWARD_ID, awardId);
    }

    private Games findGamesBySomeId(String sql, String id) throws SQLException {
    	Games games = new Games();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                games.add(processRow(rs));
            }
		}
        return games;
    }

    public Games findGamesByAwardIdAndYear(String awardId, String year) throws SQLException {
    	Games games = new Games();
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_GAMES_BY_AWARD_ID_AND_YEAR);
            ps.setLong(1, MyStuffUtil.parseLong(awardId));
            ps.setInt(2, MyStuffUtil.parseInt(year));
            processResultSet(ps.executeQuery(), games);
		}
        return games;
    }

    @Override
    protected Game processRow(ResultSet rs) throws SQLException {
    	Game game = new Game();
    	game.setId(rs.getString("id"));
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
