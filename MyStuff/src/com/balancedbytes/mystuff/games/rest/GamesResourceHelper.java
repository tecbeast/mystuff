package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

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
import com.balancedbytes.mystuff.games.data.GameDataFilter;
import com.balancedbytes.mystuff.games.data.PublisherDataAccess;

public class GamesResourceHelper {

	private UriInfo uriInfo;
	
	public GamesResourceHelper(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	public Games findAllGames() throws SQLException {
		return expand(new GameDataAccess().findAllGames());
	}
	
	public Games findGamesFiltered(GameDataFilter filter) throws SQLException {
		return expand(new GameDataAccess().findGamesFiltered(filter), filter);
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

	public Games findGamesByAwardIdFiltered(String awardId, GameDataFilter filter) throws SQLException {
		return expand(new GameDataAccess().findGamesByAwardIdFiltered(awardId, filter), filter);
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
	
	public Game createGame(Game game) throws SQLException {
		new GameDataAccess().createGame(game);
		game.buildLink(getGamesUri());
		return game;
	}
	
	public Game updateGame(Game game) throws SQLException {
		new GameDataAccess().updateGame(game);
		game.buildLink(getGamesUri());
		return game;
	}

	public Response deleteGame(String id) throws SQLException {
		if (new GameDataAccess().deleteGame(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}
	
    private Game expand(Game game) throws SQLException {
		if (game == null) {
			return null;
		}
		game.setAuthors(findAuthorsByGameId(game.getId()));
		game.setPublishers(findPublishersByGameId(game.getId()));
		game.setAwards(findAwardsByGameId(game.getId()));
		game.buildLink(getGamesUri());
		return game;
    }

    private Games expand(Games games) throws SQLException {
    	return expand(games, null);
    }

    private Games expand(Games games, GameDataFilter filter) throws SQLException {
		for (Game game : games.getGames()) {
			expand(game);
		}
		UriBuilder uriBuilderCollection = MyStuffUtil.setQueryParams(uriInfo.getRequestUriBuilder(), filter);
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
