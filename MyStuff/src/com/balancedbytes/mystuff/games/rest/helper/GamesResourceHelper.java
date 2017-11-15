package com.balancedbytes.mystuff.games.rest.helper;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Game;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Images;
import com.balancedbytes.mystuff.games.Notes;
import com.balancedbytes.mystuff.games.Publishers;
import com.balancedbytes.mystuff.games.data.AuthorDataAccess;
import com.balancedbytes.mystuff.games.data.AwardDataAccess;
import com.balancedbytes.mystuff.games.data.GameDataAccess;
import com.balancedbytes.mystuff.games.data.ImageDataAccess;
import com.balancedbytes.mystuff.games.data.NoteDataAccess;
import com.balancedbytes.mystuff.games.data.PublisherDataAccess;
import com.balancedbytes.mystuff.games.data.filter.GameDataFilter;
import com.balancedbytes.mystuff.games.rest.RestDataPaging;

public class GamesResourceHelper extends ResourceHelper {
	
	public static final String BASE_PATH = "games";

	public GamesResourceHelper(UriInfo uriInfo) {
		super(uriInfo);
	}
	
	public Games findAllGames(RestDataPaging paging) throws SQLException {
		Games games = new GameDataAccess().findAllGames(paging);
		return expand(games, paging, null);
	}
	
	public Games findGamesFiltered(GameDataFilter filter, RestDataPaging paging) throws SQLException {
		Games games = new GameDataAccess().findGamesFiltered(filter, paging);
		return expand(games, paging, filter);
	}
	
	public Game findGameById(String gameId) throws SQLException {
		Game game = new GameDataAccess().findGameById(gameId);
		return expand(game);
	}

	public Games findGamesByAuthorId(String authorId, RestDataPaging paging) throws SQLException {
		Games games = new GameDataAccess().findGamesByAuthorId(authorId, paging);
		return expand(games, paging, null);
	}

	public Games findGamesByPublisherId(String publisherId, RestDataPaging paging) throws SQLException {
		Games games = new GameDataAccess().findGamesByPublisherId(publisherId, paging);
		return expand(games, paging, null);
	}

	public Games findGamesByAwardId(String awardId, RestDataPaging paging) throws SQLException {
		Games games = new GameDataAccess().findGamesByAwardId(awardId, paging);
		return expand(games, paging, null);
	}

	public Games findGamesByAwardIdFiltered(String awardId, GameDataFilter filter, RestDataPaging paging) throws SQLException {
		Games games = new GameDataAccess().findGamesByAwardIdFiltered(awardId, filter, paging);
		return expand(games, paging, filter);
	}

	public Authors findAuthorsByGameId(String gameId) throws SQLException {
		Authors authors = new AuthorDataAccess().findAuthorsByGameId(gameId, null);
		addLinks(authors, null, null, AuthorsResourceHelper.BASE_PATH);
		return authors;
	}

	public Publishers findPublishersByGameId(String gameId) throws SQLException {
		Publishers publishers = new PublisherDataAccess().findPublishersByGameId(gameId, null);
		addLinks(publishers, null, null, PublishersResourceHelper.BASE_PATH);
		return publishers;
	}
	
	public Images findImagesByGameId(String gameId) throws SQLException {
		Images images = new ImageDataAccess().findImagesByGameId(gameId, null);
		addLinks(images, null, null, ImagesResourceHelper.BASE_PATH);
		return images;
	}

	public Awards findAwardsByGameId(String gameId) throws SQLException {
		Awards awards = new AwardDataAccess().findAwardsByGameId(gameId, null);
		addLinks(awards, null, null, AwardsResourceHelper.BASE_PATH);
		return awards;
	}

	public Notes findNotesByGameId(String gameId) throws SQLException {
		Notes notes = new NoteDataAccess().findNotesByGameId(gameId, null);
		addLinks(notes, null, null, NotesResourceHelper.BASE_PATH);
		return notes;
	}

	public Game createGame(Game game) throws SQLException {
		new GameDataAccess().createGame(game);
		return addLinks(game);
	}
	
	public Game updateGame(Game game) throws SQLException {
		new GameDataAccess().updateGame(game);
		return addLinks(game);
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
		Authors authors = findAuthorsByGameId(game.getId());
		if (authors.hasElements()) {
			game.setAuthors(authors);
		}
		Publishers publishers = findPublishersByGameId(game.getId());
		if (publishers.hasElements()) {
			game.setPublishers(publishers);
		}
		Images images = findImagesByGameId(game.getId());
		if (images.hasElements()) {
			game.setImages(images);
		}
		Awards awards = findAwardsByGameId(game.getId());
		if (awards.hasElements()) {
			game.setAwards(awards);
		}
		Notes notes = findNotesByGameId(game.getId());
		if (notes.hasElements()) {
			game.setNotes(notes);
		}
		addLinks(game, BASE_PATH);
		return game;
    }

    private Games expand(Games games, RestDataPaging paging, GameDataFilter filter) throws SQLException {
		for (Game game : games.getElements()) {
			expand(game);
		}
		addLinks(games, paging, filter, BASE_PATH);
		addPaging(games, paging);
		return games;
    }
    
    private Game addLinks(Game game) {
    	addLinks(game, BASE_PATH);
    	return game;
    }
    
}
