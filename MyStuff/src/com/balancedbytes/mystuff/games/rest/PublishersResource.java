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

import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;
import com.balancedbytes.mystuff.games.data.PublisherDataFilter;
import com.balancedbytes.mystuff.rest.compress.Compress;

@Path("/publishers")
public class PublishersResource {

	private static final Log _LOG = LogFactory.getLog(PublishersResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Publishers findPublishers(
		@QueryParam("name") String name,
		@QueryParam("page") String page,
		@QueryParam("pageSize") String pageSize
	) throws SQLException {
		RestDataPaging paging = new RestDataPaging();
		paging.init(page, pageSize);
		PublisherDataFilter filter = new PublisherDataFilter();
		filter.setName(name);
		if (filter.isEmpty()) {
			_LOG.info("findAllPublishers()");
			return new PublishersResourceHelper(uriInfo).findAllPublishers(paging);
		} else {
			_LOG.info("findPublishersFiltered(" + filter + ")");
			return new PublishersResourceHelper(uriInfo).findPublishersFiltered(filter, paging);
		}
	}
	
	@POST
	@Compress
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Publisher createPublisher(Publisher publisher) throws SQLException {
		_LOG.info("createPublisher()");
		return new PublishersResourceHelper(uriInfo).createPublisher(publisher);
	}
	
	@GET
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Publisher findPublisherById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findPublisherById(" + id + ")");
		return new PublishersResourceHelper(uriInfo).findPublisherById(id);
	}
	
	@PUT
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Publisher updatePublisher(@PathParam("id") String id, Publisher publisher) throws SQLException {
		_LOG.info("updatePublisher(" + id + ")");
		publisher.setId(id);
		return new PublishersResourceHelper(uriInfo).updatePublisher(publisher);
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deletePublisher(@PathParam("id") String id) throws SQLException {
		_LOG.info("deletePublisher(" + id + ")");
		return new PublishersResourceHelper(uriInfo).deletePublisher(id);
	}

	@GET
	@Path("{id}/games")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Games findGamesByPublisherId(
		@PathParam("id") String id,
		@QueryParam("page") String page,
		@QueryParam("pageSize") String pageSize
	) throws SQLException {
		_LOG.info("findGamesByPublisherId(" + id + ")");
		RestDataPaging paging = new RestDataPaging();
		paging.init(page, pageSize);
		return new GamesResourceHelper(uriInfo).findGamesByPublisherId(id, paging);
	}

}
