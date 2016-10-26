package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.data.AuthorDataAccess;

@Path("/authors")
public class AuthorsResource {

	private static final Log _LOG = LogFactory.getLog(AuthorsResource.class);

	@Context
	private UriInfo uriInfo;
	private AuthorDataAccess authorData = new AuthorDataAccess();
	private GamesResourceHelper gamesHelper = new GamesResourceHelper();
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Authors findAuthors() throws SQLException {
		_LOG.info("findAllAuthors()");
		Authors authors = authorData.findAllAuthors();
		authors.setHref(getAuthorsUriBuilder().toString());
		authors.buildHrefOnChildren(getAuthorsUriBuilder());
		return authors;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Author findAuthorById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAuthorById(" + id + ")");
		Author author = authorData.findAuthorById(id);
		author.buildHref(getAuthorsUriBuilder());
		return author;
	}
	
	@GET
	@Path("{id}/games")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Games findGamesByAuthorId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findGamesByAuthorId(" + id + ")");
		return gamesHelper.findGamesByAuthorId(id, uriInfo);
	}

	private UriBuilder getAuthorsUriBuilder() {
		return uriInfo.getBaseUriBuilder().path("authors");
	}
	
}
