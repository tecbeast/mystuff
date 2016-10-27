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
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Games;

@Path("/authors")
public class AuthorsResource {

	private static final Log _LOG = LogFactory.getLog(AuthorsResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Authors findAuthors(@QueryParam("name") String name) throws SQLException {
		if (MyStuffUtil.isProvided(name)) {
			_LOG.info("findAuthorsByName(" + name + ")");
			return new AuthorsResourceHelper(uriInfo).findAuthorsByName(name);
		} else {
			_LOG.info("findAllAuthors()");
			return new AuthorsResourceHelper(uriInfo).findAllAuthors();
		}
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Author findAuthorById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAuthorById(" + id + ")");
		return new AuthorsResourceHelper(uriInfo).findAuthorById(id);
	}
	
	@GET
	@Path("{id}/games")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Games findGamesByAuthorId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findGamesByAuthorId(" + id + ")");
		return new GamesResourceHelper(uriInfo).findGamesByAuthorId(id);
	}
	
}
