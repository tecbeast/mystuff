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

import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;
import com.balancedbytes.mystuff.games.data.PublisherDataAccess;

@Path("/publishers")
public class PublishersResource {

	private static final Log _LOG = LogFactory.getLog(PublishersResource.class);

	@Context
	private UriInfo uriInfo;
	private PublisherDataAccess publisherData = new PublisherDataAccess();
	private GamesResourceHelper gamesHelper = new GamesResourceHelper();
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Publishers findPublishers() throws SQLException {
		_LOG.info("findPublishers()");
		Publishers publishers = publisherData.findAllPublishers();
		publishers.setHref(getPublishersUriBuilder().toString());
		publishers.buildHrefOnChildren(getPublishersUriBuilder());
		return publishers;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Publisher findPublisherById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findPublisherById(" + id + ")");
		Publisher publisher = publisherData.findPublisherById(id);
		publisher.buildHref(getPublishersUriBuilder());
		return publisher;
	}
	
	@GET
	@Path("{id}/games")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Games findGamesByPublisherId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findGamesByPublisherId(" + id + ")");
		return gamesHelper.findGamesByPublisherId(id, uriInfo);
	}

	private UriBuilder getPublishersUriBuilder() {
		return uriInfo.getBaseUriBuilder().path("publishers");
	}
	
}
