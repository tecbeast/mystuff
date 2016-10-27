package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Game;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Publishers;

@Path("/games")
public class GamesResource {

	private static final Log _LOG = LogFactory.getLog(GamesResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Games findGames(@QueryParam("name") String name) throws SQLException {
		if (MyStuffUtil.isProvided(name)) {
			_LOG.info("findGamesByName(" + name + ")");
			return new GamesResourceHelper(uriInfo).findGamesByName(name);
		} else {
			_LOG.info("findAllGames()");
			return new GamesResourceHelper(uriInfo).findAllGames();
		}
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Game findGameById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findGameById(" + id + ")");
		return new GamesResourceHelper(uriInfo).findGameById(id);
	}

	@GET
	@Path("{id}/authors")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Authors findAuthorsByGameId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAuthorsByGameId(" + id + ")");
		return new GamesResourceHelper(uriInfo).findAuthorsByGameId(id);
	}

	@GET
	@Path("{id}/publishers")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Publishers findPublishersByGameId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findPublishersByGameId(" + id + ")");
		return new GamesResourceHelper(uriInfo).findPublishersByGameId(id);
	}

	@GET
	@Path("{id}/awards")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Awards findAwardsByGameId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAwardsByGameId(" + id + ")");
		return new GamesResourceHelper(uriInfo).findAwardsByGameId(id);
	}

}
