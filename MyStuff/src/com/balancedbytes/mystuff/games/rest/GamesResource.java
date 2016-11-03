package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Game;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Publishers;
import com.balancedbytes.mystuff.games.data.GameDataFilter;
import com.balancedbytes.mystuff.rest.compress.Compress;

@Path("/games")
public class GamesResource {

	private static final Log _LOG = LogFactory.getLog(GamesResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Games findGames(
		@QueryParam("name") String name,
		@QueryParam("minPlayers") String minPlayers,
		@QueryParam("maxPlayers") String maxPlayers
	) throws SQLException {
		GameDataFilter filter = new GameDataFilter();
		filter.setName(name);
		filter.setMinPlayers(MyStuffUtil.parseInt(minPlayers));
		filter.setMaxPlayers(MyStuffUtil.parseInt(maxPlayers));
		if (filter.isEmpty()) {
			_LOG.info("findAllGames()");
			return new GamesResourceHelper(uriInfo).findAllGames();
		} else {
			_LOG.info("findGamesFiltered(" + filter + ")");
			return new GamesResourceHelper(uriInfo).findGamesFiltered(filter);
		}
	}

	@POST
	@Compress
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Game createGame(Game game) throws SQLException {
		_LOG.info("createGame()");
		return new GamesResourceHelper(uriInfo).createGame(game);
	}
	
	@GET
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Game findGameById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findGameById(" + id + ")");
		return new GamesResourceHelper(uriInfo).findGameById(id);
	}
	
	@PUT
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Game updateGame(@PathParam("id") String id, Game game) throws SQLException {
		_LOG.info("updateGame(" + id + ")");
		game.setId(id);
		return new GamesResourceHelper(uriInfo).updateGame(game);
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteGame(@PathParam("id") String id) throws SQLException {
		_LOG.info("deleteGame(" + id + ")");
		return new GamesResourceHelper(uriInfo).deleteGame(id);
	}

	@GET
	@Path("{id}/authors")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Authors findAuthorsByGameId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAuthorsByGameId(" + id + ")");
		return new GamesResourceHelper(uriInfo).findAuthorsByGameId(id);
	}

	@GET
	@Path("{id}/publishers")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Publishers findPublishersByGameId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findPublishersByGameId(" + id + ")");
		return new GamesResourceHelper(uriInfo).findPublishersByGameId(id);
	}

	@GET
	@Path("{id}/awards")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Awards findAwardsByGameId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAwardsByGameId(" + id + ")");
		return new GamesResourceHelper(uriInfo).findAwardsByGameId(id);
	}
	
}
