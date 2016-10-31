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
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;

@Path("/publishers")
public class PublishersResource {

	private static final Log _LOG = LogFactory.getLog(PublishersResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Publishers findPublishers(@QueryParam("name") String name) throws SQLException {
		if (MyStuffUtil.isProvided(name)) {
			_LOG.info("findPublishersByName(" + name + ")");
			return new PublishersResourceHelper(uriInfo).findPublishersByName(name);
		} else {
			_LOG.info("findAllPublishers()");
			return new PublishersResourceHelper(uriInfo).findAllPublishers();
		}
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Publisher createPublisher(Publisher publisher) throws SQLException {
		_LOG.info("createPublisher()");
		return new PublishersResourceHelper(uriInfo).createPublisher(publisher);
	}
	
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Publisher findPublisherById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findPublisherById(" + id + ")");
		return new PublishersResourceHelper(uriInfo).findPublisherById(id);
	}
	
	@PUT
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Publisher updatePublisher(@PathParam("id") String id, Publisher publisher) throws SQLException {
		_LOG.info("updatePublisher(" + id + ")");
		publisher.setId(id);
		return new PublishersResourceHelper(uriInfo).updatePublisher(publisher);
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deletePublisher(@PathParam("id") String id) throws SQLException {
		_LOG.info("deletePublisher(" + id + ")");
		return new PublishersResourceHelper(uriInfo).deletePublisher(id);
	}

	@GET
	@Path("{id}/games")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Games findGamesByPublisherId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findGamesByPublisherId(" + id + ")");
		return new GamesResourceHelper(uriInfo).findGamesByPublisherId(id);
	}

}
