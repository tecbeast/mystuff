package com.balancedbytes.mystuff.games.rest.api;

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

import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.data.filter.AuthorDataFilter;
import com.balancedbytes.mystuff.games.rest.RestDataPaging;
import com.balancedbytes.mystuff.games.rest.helper.AuthorsResourceHelper;
import com.balancedbytes.mystuff.games.rest.helper.GamesResourceHelper;
import com.balancedbytes.mystuff.rest.compress.Compress;

@Path("/authors")
public class AuthorsResource {

	private static final Log LOG = LogFactory.getLog(AuthorsResource.class);
	
	@GET
	@Compress
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Authors findAuthors(
		@QueryParam("name") String name,
		@QueryParam("page") String page,
		@QueryParam("pageSize") String pageSize,
		@Context UriInfo uriInfo
	) throws SQLException {
		RestDataPaging paging = new RestDataPaging();
		paging.init(page, pageSize);
		AuthorDataFilter filter = new AuthorDataFilter();
		filter.setName(name);
		if (filter.isEmpty()) {
			LOG.info("findAllAuthors()");
			return new AuthorsResourceHelper(uriInfo).findAllAuthors(paging);
		} else {
			LOG.info("findAuthorsFiltered(" + filter + ")");
			return new AuthorsResourceHelper(uriInfo).findAuthorsFiltered(filter, paging);
		}
	}

	@POST
	@Compress
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Author createAuthor(
		Author author,
		@Context UriInfo uriInfo
	) throws SQLException {
		LOG.info("createAuthor()");
		return new AuthorsResourceHelper(uriInfo).createAuthor(author);
	}

	@GET
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Author findAuthorById(
		@PathParam("id") String id,
		@Context UriInfo uriInfo
	) throws SQLException {
		LOG.info("findAuthorById(" + id + ")");
		return new AuthorsResourceHelper(uriInfo).findAuthorById(id);
	}
	
	@PUT
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Author updateAuthor(
		@PathParam("id") String id,
		Author author,
		@Context UriInfo uriInfo
	) throws SQLException {
		LOG.info("updateAuthor(" + id + ")");
		author.setId(id);
		return new AuthorsResourceHelper(uriInfo).updateAuthor(author);
	}

	@DELETE
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteAuthor(
		@PathParam("id") String id,
		@Context UriInfo uriInfo
	) throws SQLException {
		LOG.info("deleteAuthor(" + id + ")");
		return new AuthorsResourceHelper(uriInfo).deleteAuthor(id);
	}

	@GET
	@Path("{id}/games")
	@Compress
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Games findGamesByAuthorId(
		@PathParam("id") String id,
		@QueryParam("page") String page,
		@QueryParam("pageSize") String pageSize,
		@Context UriInfo uriInfo
	) throws SQLException {
		LOG.info("findGamesByAuthorId(" + id + ")");
		RestDataPaging paging = new RestDataPaging();
		paging.init(page, pageSize);
		return new GamesResourceHelper(uriInfo).findGamesByAuthorId(id, paging);
	}
	
}
