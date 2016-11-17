package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Game;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Note;
import com.balancedbytes.mystuff.games.Notes;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;

public class GameDataAccess extends RestDataAccess<Game> {
	
	private static final String _SQL_FIND_ALL_GAMES = 
		"SELECT * FROM games ORDER BY name";
	private static final String _SQL_FIND_GAME_BY_ID =
		"SELECT * FROM games WHERE id = ?";
	private static final String _SQL_FIND_GAMES_FILTERED =
		"SELECT * FROM games"
		+ " WHERE UPPER(name) LIKE ? AND players_min >= ? AND players_max <= ?"
		+ " ORDER BY name";
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
	private static final String _SQL_FIND_GAMES_BY_AWARD_ID_FILTERED =
		"SELECT game_awards.award_id, games.*, game_awards.year"
		+ " FROM game_awards LEFT JOIN games"
		+ " ON game_awards.game_id = games.id"
		+ " WHERE game_awards.award_id = ? AND game_awards.year = ?"
		+ " ORDER BY games.name";
	private static final String _SQL_CREATE_GAME =
		"INSERT INTO games"
		+ " (name, published_year, players_min, players_max, playtime_min,"
		+ " playtime_max, playtime_per_player, age_min, description, rating)"
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
	private static final String _SQL_CREATE_GAME_NOTES =
		"INSERT INTO game_notes"
		+ " (game_id, note_id)"
		+ " VALUES (?, ?)";
	private static final String _SQL_UPDATE_GAME =
		"UPDATE games"
		+ " SET name = ?, published_year = ?, players_min = ?, players_max = ?,"
		+ " playtime_min = ?, playtime_max = ?, playtime_per_player = ?,"
		+ " age_min = ?, description = ?, rating = ?"
		+ " WHERE id = ?";
	private static final String _SQL_DELETE_GAME =
		"DELETE FROM games WHERE id = ?";
	private static final String _SQL_DELETE_GAME_AUTHORS =
		"DELETE FROM game_authors WHERE game_id = ?";
	private static final String _SQL_DELETE_GAME_PUBLISHERS =
		"DELETE FROM game_publishers WHERE game_id = ?";
	private static final String _SQL_DELETE_GAME_AWARDS =
		"DELETE FROM game_awards WHERE game_id = ?";

    public Games findAllGames(RestDataPaging paging) throws SQLException {
    	Games games = new Games();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_GAMES);
            processResultSet(ps.executeQuery(), games, paging);
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
    
    public Games findGamesFiltered(GameDataFilter filter, RestDataPaging paging) throws SQLException {
    	if ((filter == null) || filter.isEmpty()) {
    		return findAllGames(paging);
    	}
    	Games games = new Games();
    	String namePattern = "%";
    	String trimmedUpperCaseName = MyStuffUtil.isProvided(filter.getName()) ? filter.getName().trim().toUpperCase() : null;
    	if (MyStuffUtil.isProvided(trimmedUpperCaseName)) {
        	namePattern = new StringBuilder().append("%").append(trimmedUpperCaseName).append("%").toString();
    	}
    	int playersMin = filter.getMinPlayers();
    	int playersMax = filter.getMaxPlayers();
    	if (playersMax == 0) {
    		playersMax = Byte.MAX_VALUE;
    	}
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_GAMES_FILTERED);
            ps.setString(1, namePattern);
            ps.setInt(2, playersMin);
            ps.setInt(3, playersMax);
            processResultSet(ps.executeQuery(), games, paging);
		}
        return games;
    }
    
    public Games findGamesByAuthorId(String authorId, RestDataPaging paging) throws SQLException {
    	return findGamesBySomeId(_SQL_FIND_GAMES_BY_AUTHOR_ID, authorId, paging);
    }

    public Games findGamesByPublisherId(String publisherId, RestDataPaging paging) throws SQLException {
    	return findGamesBySomeId(_SQL_FIND_GAMES_BY_PUBLISHER_ID, publisherId, paging);
    }

    public Games findGamesByAwardId(String awardId, RestDataPaging paging) throws SQLException {
    	return findGamesBySomeId(_SQL_FIND_GAMES_BY_AWARD_ID, awardId, paging);
    }

    private Games findGamesBySomeId(String sql, String id, RestDataPaging paging) throws SQLException {
    	Games games = new Games();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            processResultSet(ps.executeQuery(), games, paging);
		}
        return games;
    }

    public Games findGamesByAwardIdFiltered(String awardId, GameDataFilter filter, RestDataPaging paging) throws SQLException {
    	if ((filter == null) || filter.isEmpty()) {
    		return findGamesByAwardId(awardId, paging);
    	}
    	Games games = new Games();
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_GAMES_BY_AWARD_ID_FILTERED);
            ps.setLong(1, MyStuffUtil.parseLong(awardId));
            ps.setInt(2, filter.getYear());
            processResultSet(ps.executeQuery(), games, paging);
		}
        return games;
    }
    
    public void createGame(Game game) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
        	createGame(c, game);
        	createGameDependencies(c, game);
        }
    }
    
    private boolean createGameDependencies(Connection c, Game game) throws SQLException {
    	boolean created = false;
    	Authors authors = game.getAuthors();
    	if ((authors != null) && authors.hasElements()) {
    		for (Author author : authors.getElements()) {
    			created |= createGameAuthor(c, game, author);
    		}
    	}
    	Publishers publishers = game.getPublishers();
    	if ((publishers != null) && publishers.hasElements()) {
    		for (Publisher publisher : publishers.getElements()) {
    			created |= createGamePublisher(c, game, publisher);
    		}
    	}
    	Awards awards = game.getAwards();
    	if ((awards != null) && awards.hasElements()) {
    		for (Award award : awards.getElements()) {
    			created |= createGameAward(c, game, award);
    		}
    	}
    	Notes notes = game.getNotes();
    	if ((notes != null) && notes.hasElements()) {
    		for (Note note : notes.getElements()) {
    			created |= createGameNote(c, game, note);
    		}
    	}
    	return created;
    }

    private void createGame(Connection c, Game game) throws SQLException {
    	try (PreparedStatement ps = c.prepareStatement(_SQL_CREATE_GAME, new String[] { "id" })) {
    		ps.setString(1, game.getName());
    		ps.setInt(2, game.getPublishedYear());
    		ps.setInt(3, game.getPlayersMin());
    		ps.setInt(4, game.getPlayersMax());
	        ps.setInt(5, game.getPlaytimeMin());
	        ps.setInt(6, game.getPlaytimeMax());
	        ps.setBoolean(7, game.getPlaytimePerPlayer());
	        ps.setInt(8, game.getAgeMin());
	        ps.setString(9, game.getDescription());
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
    
    private boolean createGameNote(Connection c, Game game, Note note) throws SQLException {
    	try (PreparedStatement ps = c.prepareStatement(_SQL_CREATE_GAME_NOTES)) {
    		ps.setLong(1, MyStuffUtil.parseLong(game.getId()));
    		ps.setLong(2, MyStuffUtil.parseLong(note.getId()));
    		return (ps.executeUpdate() == 1);
    	}
    }

    public boolean updateGame(Game game) throws SQLException {
    	boolean updated = false;
    	long gameId = MyStuffUtil.parseLong(game.getId());
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_UPDATE_GAME);
    		ps.setString(1, game.getName());
    		ps.setInt(2, game.getPublishedYear());
    		ps.setInt(3, game.getPlayersMin());
    		ps.setInt(4, game.getPlayersMax());
	        ps.setInt(5, game.getPlaytimeMin());
	        ps.setInt(6, game.getPlaytimeMax());
	        ps.setBoolean(7, game.getPlaytimePerPlayer());
	        ps.setInt(8, game.getAgeMin());
	        ps.setString(9, game.getDescription());
	        ps.setInt(10, game.getRating());
            ps.setLong(11, MyStuffUtil.parseLong(game.getId()));
            updated |= (ps.executeUpdate() == 1);
            updated |= deleteGameDependencies(c, gameId);
            updated |= createGameDependencies(c, game);
        }
        return updated;
    }

    public boolean deleteGame(String id) throws SQLException {
    	boolean deleted = false;
    	long gameId = MyStuffUtil.parseLong(id);
        try (Connection c = ConnectionHelper.getConnection()) {
        	deleted |= executeDeleteSql(c, _SQL_DELETE_GAME, gameId);
        	deleted |= deleteGameDependencies(c, gameId);
        }
        return deleted;
    }

    private boolean deleteGameDependencies(Connection c, long gameId) throws SQLException {
    	boolean deleted = false;
    	deleted |= executeDeleteSql(c, _SQL_DELETE_GAME_AUTHORS, gameId);
    	deleted |= executeDeleteSql(c, _SQL_DELETE_GAME_PUBLISHERS, gameId);
    	deleted |= executeDeleteSql(c, _SQL_DELETE_GAME_AWARDS, gameId);
    	return deleted;
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
    	int publishedYear = rs.getInt("published_year");
		game.setPublishedYear((publishedYear > 0) ? publishedYear : null);
    	int playersMin = rs.getInt("players_min");
   		game.setPlayersMin((playersMin > 0) ? playersMin : null);
    	int playersMax = rs.getInt("players_max");
		game.setPlayersMax((playersMax > 0) ? playersMax : null);
    	int playtimeMin = rs.getInt("playtime_min");
    	int playtimeMax = rs.getInt("playtime_max");
    	if ((playtimeMin > 0) || (playtimeMax > 0)) {
        	game.setPlaytimeMin((playtimeMin > 0) ? playtimeMin : null);
        	game.setPlaytimeMax((playtimeMax > 0) ? playtimeMax : null);
        	boolean playtimePerPlayer = rs.getBoolean("playtime_per_player");
        	game.setPlaytimePerPlayer(playtimePerPlayer);
    	}
    	int ageMin = rs.getInt("age_min");
    	game.setAgeMin((ageMin > 0) ? ageMin : null);
    	game.setDescription(rs.getString("description"));
    	int rating = rs.getInt("rating");
    	game.setRating((rating > 0) ? rating : null);
        return game;
    }
    
}
