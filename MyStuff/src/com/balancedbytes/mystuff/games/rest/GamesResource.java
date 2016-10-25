package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Game;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.data.AuthorDataAccess;
import com.balancedbytes.mystuff.games.data.GameDataAccess;
import com.balancedbytes.mystuff.games.data.PublisherDataAccess;

@Path("/games")
public class GamesResource {

	private static final Log _LOG = LogFactory.getLog(GamesResource.class);

	private GameDataAccess gameData = new GameDataAccess();
	private AuthorDataAccess authorData = new AuthorDataAccess();
	private PublisherDataAccess publisherData = new PublisherDataAccess();
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Game> findAll() throws SQLException {
		_LOG.info("findAll()");
		return complete(gameData.findAll());
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Game findById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findById(" + id + ")");
		return complete(gameData.findById(MyStuffUtil.parseLong(id)));
	}

	@GET
	@Path("{id}/authors")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Author> findAuthorsById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAuthorsById(" + id + ")");
		return authorData.findAllWithGameId(MyStuffUtil.parseLong(id));
	}

	@GET
	@Path("{id}/publishers")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Publisher> findPublishersById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findPublishersById(" + id + ")");
		return publisherData.findAllWithGameId(MyStuffUtil.parseLong(id));
	}
	
    private Game complete(Game game) throws SQLException {
    	if ((game != null) && (game.getId() > 0)) {
    		game.setAuthors(authorData.findAllWithGameId(game.getId()));
    		game.setPublishers(publisherData.findAllWithGameId(game.getId()));
    	}
    	return game;
    }
    
    private List<Game> complete(List<Game> games) throws SQLException {
    	if (games != null) {
			for (Game boardgame : games) {
				complete(boardgame);
			}
    	}
    	return games;
    }

}
