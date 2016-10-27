package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.HashMapBuilder;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestData;
import com.balancedbytes.mystuff.RestDataCollection;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Game;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Publishers;
import com.balancedbytes.mystuff.games.data.AuthorDataAccess;
import com.balancedbytes.mystuff.games.data.AwardDataAccess;
import com.balancedbytes.mystuff.games.data.GameDataAccess;
import com.balancedbytes.mystuff.games.data.PublisherDataAccess;

public class GamesResourceHelper {

	private UriInfo uriInfo;
	
	public GamesResourceHelper(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	public Games findAllGames() throws SQLException {
		return expand(new GameDataAccess().findAllGames());
	}
	
	public Games findGamesByName(String name) throws SQLException {
		return expand(new GameDataAccess().findGamesByName(name), HashMapBuilder.build("name", name));
	}
	
	public Game findGameById(String gameId) throws SQLException {
		return expand(new GameDataAccess().findGameById(gameId));
	}

	public Games findGamesByAuthorId(String authorId) throws SQLException {
		return expand(new GameDataAccess().findGamesByAuthorId(authorId));
	}

	public Games findGamesByPublisherId(String publisherId) throws SQLException {
		return expand(new GameDataAccess().findGamesByPublisherId(publisherId));
	}

	public Games findGamesByAwardId(String awardId) throws SQLException {
		return expand(new GameDataAccess().findGamesByAwardId(awardId));
	}

	public Games findGamesByAwardIdAndYear(String awardId, String year) throws SQLException {
		return expand(new GameDataAccess().findGamesByAwardIdAndYear(awardId, year), HashMapBuilder.build("year", year));
	}

	public Authors findAuthorsByGameId(String gameId) throws SQLException {
		Authors authors = new AuthorDataAccess().findAuthorsByGameId(gameId);
		buildLinks(authors, gameId, "authors");
		return authors;
	}

	public Publishers findPublishersByGameId(String gameId) throws SQLException {
		Publishers publishers = new PublisherDataAccess().findPublishersByGameId(gameId);
		buildLinks(publishers, gameId, "publishers");
		return publishers;
	}

	public Awards findAwardsByGameId(String gameId) throws SQLException {
		Awards awards = new AwardDataAccess().findAwardsByGameId(gameId);
		buildLinks(awards, gameId, "awards");
		return awards;
	}
	
    private Game expand(Game game) throws SQLException {
		if (game == null) {
			return null;
		}
		game.setAuthors(findAuthorsByGameId(game.getId()));
		game.setPublishers(findPublishersByGameId(game.getId()));
		game.setAwards(findAwardsByGameId(game.getId()));
		return game;
    }

    private Games expand(Games games) throws SQLException {
    	return expand(games, null);
    }

    private Games expand(Games games, Map<String, String> queryParams) throws SQLException {
		for (Game game : games.getGames()) {
			expand(game);
		}
		UriBuilder uriBuilderCollection = MyStuffUtil.setQueryParams(uriInfo.getRequestUriBuilder(), queryParams);
		games.buildLinks(uriBuilderCollection.build(), getGamesUri());
		return games;
    }

    private void buildLinks(RestDataCollection<? extends RestData> dataCollection, String gameId, String path) {
		URI uriCollection = UriBuilder.fromUri(getGamesUri()).path(gameId).path(path).build();
		URI uriElements = uriInfo.getBaseUriBuilder().path(path).build();
		dataCollection.buildLinks(uriCollection, uriElements);    	
    }
    
    private URI getGamesUri() {
    	return uriInfo.getBaseUriBuilder().path("games").build();
    }    
    
}
