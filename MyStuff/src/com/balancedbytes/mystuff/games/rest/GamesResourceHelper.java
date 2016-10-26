package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

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

	private GameDataAccess gameData = new GameDataAccess();
	private AuthorDataAccess authorData = new AuthorDataAccess();
	private PublisherDataAccess publisherData = new PublisherDataAccess();
	private AwardDataAccess awardData = new AwardDataAccess();
	
	public Games findAllGames(UriInfo uriInfo) throws SQLException {
		return expand(gameData.findAllGames(), uriInfo);
	}
	
	public Games findGamesByName(String name, UriInfo uriInfo) throws SQLException {
		return expand(gameData.findGamesByName(name), uriInfo, name);
	}
	
	public Game findGameById(String gameId, UriInfo uriInfo) throws SQLException {
		return expand(gameData.findGameById(gameId), uriInfo);
	}

	public Games findGamesByAuthorId(String authorId, UriInfo uriInfo) throws SQLException {
		return expand(gameData.findGamesByAuthorId(authorId), uriInfo);
	}

	public Games findGamesByPublisherId(String publisherId, UriInfo uriInfo) throws SQLException {
		return expand(gameData.findGamesByPublisherId(publisherId), uriInfo);
	}

	public Games findGamesByAwardId(String awardId, UriInfo uriInfo) throws SQLException {
		return expand(gameData.findGamesByAwardId(awardId), uriInfo);
	}

	public Authors findAuthorsByGameId(String gameId, UriInfo uriInfo) throws SQLException {
		if ((gameId == null) || (uriInfo == null)) {
			return null;
		}
		Authors authors = authorData.findAuthorsByGameId(gameId);
		authors.setHref(getGamesUriBuilder(uriInfo, gameId).path("authors").toString());
		authors.buildHrefOnChildren(uriInfo.getBaseUriBuilder().path("authors"));
		return authors;
	}

	public Publishers findPublishersByGameId(String gameId, UriInfo uriInfo) throws SQLException {
		if ((gameId == null) || (uriInfo == null)) {
			return null;
		}
		Publishers publishers = publisherData.findPublishersByGameId(gameId);
		publishers.setHref(getGamesUriBuilder(uriInfo, gameId).path("publishers").toString());
		publishers.buildHrefOnChildren(uriInfo.getBaseUriBuilder().path("publishers"));
		return publishers;
	}

	public Awards findAwardsByGameId(String gameId, UriInfo uriInfo) throws SQLException {
		if ((gameId == null) || (uriInfo == null)) {
			return null;
		}
		Awards awards = awardData.findAwardsByGameId(gameId);
		awards.setHref(getGamesUriBuilder(uriInfo, gameId).path("awards").toString());
		awards.buildHrefOnChildren(uriInfo.getBaseUriBuilder().path("awards"));
		return awards;
	}
	
    private Game expand(Game game, UriInfo uriInfo) throws SQLException {
		if ((game == null) || (uriInfo == null)) {
			return game;
		}
		game.setAuthors(findAuthorsByGameId(game.getId(), uriInfo));
		game.setPublishers(findPublishersByGameId(game.getId(), uriInfo));
		game.setAwards(findAwardsByGameId(game.getId(), uriInfo));
    	return game;
    }

    private Games expand(Games games, UriInfo uriInfo) throws SQLException {
    	return expand(games, uriInfo, null);
    }

    private Games expand(Games games, UriInfo uriInfo, String name) throws SQLException {
		if ((games == null) || (uriInfo == null)) {
			return games;
		}
		UriBuilder uriBuilder = getGamesUriBuilder(uriInfo);
		if (name != null) {
			uriBuilder.queryParam("name", name);
		}
		games.setHref(uriBuilder.toString());
		games.buildHrefOnChildren(getGamesUriBuilder(uriInfo));
		for (Game game : games.getGames()) {
			expand(game, uriInfo);
		}
    	return games;
    }
    
    private UriBuilder getGamesUriBuilder(UriInfo uriInfo) {
		return uriInfo.getBaseUriBuilder().path("games");
    }
    
    private UriBuilder getGamesUriBuilder(UriInfo uriInfo, String gameId) {
		return getGamesUriBuilder(uriInfo).path(gameId);
    }
    
}
