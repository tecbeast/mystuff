package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Game;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;

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
	private static final String _SQL_CREATE_GAME =
		"INSERT INTO games"
		+ " (name, edition_year, players_min, players_max, playtime_min,"
		+ " playtime_max, playtime_per_player, age_min, last_played, rating)"
		+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String _SQL_CREATE_GAME_AUTHORS =
		"INSERT INTO game_authors"
		+ " (game_id, author_id)"
		+ " VALUES (?, ?)";
	private static final String _SQL_CREATE_GAME_PUBLISHERS =
		"INSERT INTO game_publishers"
		+ " (game_id, publisher_id)"
		+ " VALUES (?, ?)";
	private static final String _SQL_CREATE_GAME_AWARDS =
		"INSERT INTO game_awards"
		+ " (game_id, award_id, year)"
		+ " VALUES (?, ?)";
	private static final String _SQL_DELETE_GAME =
		"DELETE FROM games WHERE id = ?";
	private static final String _SQL_DELETE_GAME_AUTHORS =
		"DELETE FROM game_authors WHERE game_id = ?";
	private static final String _SQL_DELETE_GAME_PUBLISHERS =
		"DELETE FROM game_publishers WHERE game_id = ?";
	private static final String _SQL_DELETE_GAME_AWARDS =
		"DELETE FROM game_awards WHERE game_id = ?";

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
    
    public void createGame(Game game) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
        	createGame(c, game);
        	Authors authors = game.getAuthors();
        	if ((authors != null) && (authors.size() > 0)) {
        		for (Author author : authors.getAuthors()) {
        			createGameAuthor(c, game, author);
        		}
        	}
        	Publishers publishers = game.getPublishers();
        	if ((publishers != null) && (publishers.size() > 0)) {
        		for (Publisher publisher : publishers.getPublishers()) {
        			createGamePublisher(c, game, publisher);
        		}
        	}
        	Awards awards = game.getAwards();
        	if ((awards != null) && (awards.size() > 0)) {
        		for (Award award : awards.getAwards()) {
        			createGameAward(c, game, award);
        		}
        	}
        }
    }

    private void createGame(Connection c, Game game) throws SQLException {
    	try (PreparedStatement ps = c.prepareStatement(_SQL_CREATE_GAME, new String[] { "id" })) {
    		ps.setString(1, game.getName());
    		ps.setInt(2, game.getEditionYear());
    		ps.setInt(3, game.getPlayersMin());
    		ps.setInt(4, game.getPlayersMax());
	        ps.setInt(5, game.getPlaytimeMin());
	        ps.setInt(6, game.getPlaytimeMax());
	        ps.setBoolean(7, game.isPlaytimePerPlayer());
	        ps.setInt(8, game.getAgeMin());
	        ps.setDate(9, game.getLastPlayed());
	        ps.setInt(10, game.getRating());
	        ps.executeUpdate();
	        ResultSet rs = ps.getGeneratedKeys();
	        while (rs.next()) {
	        	game.setId(rs.getString(1));
	        }
    	}
    }

    private boolean createGameAuthor(Connection c, Game game, Author author) throws SQLException {
    	try (PreparedStatement ps = c.prepareStatement(_SQL_CREATE_GAME_AUTHORS)) {
    		ps.setLong(1, MyStuffUtil.parseLong(game.getId()));
    		ps.setLong(2, MyStuffUtil.parseLong(author.getId()));
    		return (ps.executeUpdate() == 1);
    	}
    }

    private boolean createGamePublisher(Connection c, Game game, Publisher publisher) throws SQLException {
    	try (PreparedStatement ps = c.prepareStatement(_SQL_CREATE_GAME_PUBLISHERS)) {
    		ps.setLong(1, MyStuffUtil.parseLong(game.getId()));
    		ps.setLong(2, MyStuffUtil.parseLong(publisher.getId()));
    		return (ps.executeUpdate() == 1);
    	}
    }

    private boolean createGameAward(Connection c, Game game, Award award) throws SQLException {
    	try (PreparedStatement ps = c.prepareStatement(_SQL_CREATE_GAME_AWARDS)) {
    		ps.setLong(1, MyStuffUtil.parseLong(game.getId()));
    		ps.setLong(2, MyStuffUtil.parseLong(award.getId()));
    		ps.setInt(3, award.getYear());
    		return (ps.executeUpdate() == 1);
    	}
    }

    public boolean deleteGame(String id) throws SQLException {
    	boolean deleted = false;
    	long gameId = MyStuffUtil.parseLong(id);
        try (Connection c = ConnectionHelper.getConnection()) {
        	deleted |= deleteGame(c, gameId);
        	deleted |= deleteGameAuthors(c, gameId);
        	deleted |= deleteGamePublishers(c, gameId);
        	deleted |= deleteGameAwards(c, gameId);
        }
        return deleted;
    }
    
    private boolean deleteGame(Connection c, long gameId) throws SQLException {
    	return executeDeleteSql(c, _SQL_DELETE_GAME, gameId);
    }

    private boolean deleteGameAuthors(Connection c, long gameId) throws SQLException {
    	return executeDeleteSql(c, _SQL_DELETE_GAME_AUTHORS, gameId);
    }

    private boolean deleteGamePublishers(Connection c, long gameId) throws SQLException {
    	return executeDeleteSql(c, _SQL_DELETE_GAME_PUBLISHERS, gameId);
    }

    private boolean deleteGameAwards(Connection c, long gameId) throws SQLException {
    	return executeDeleteSql(c, _SQL_DELETE_GAME_AWARDS, gameId);
    }
    
    private boolean executeDeleteSql(Connection c, String sql, long gameId) throws SQLException {
    	try (PreparedStatement ps = c.prepareStatement(sql)) {
    		ps.setLong(1, gameId);
    		return (ps.executeUpdate() == 1);
    	}
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
