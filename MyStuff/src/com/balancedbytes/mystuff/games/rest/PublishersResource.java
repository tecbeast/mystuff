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
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.data.PublisherDataAccess;

@Path("/publishers")
public class PublishersResource {

	private static final Log _LOG = LogFactory.getLog(PublishersResource.class);

	private PublisherDataAccess publisherData = new PublisherDataAccess();
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Publisher> findAll() throws SQLException {
		_LOG.info("findAll()");
		return publisherData.findAll();
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Publisher findById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findById(" + id + ")");
		return publisherData.findById(MyStuffUtil.parseLong(id));
	}
	
}
