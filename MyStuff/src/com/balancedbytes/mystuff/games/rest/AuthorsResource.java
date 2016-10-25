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
import com.balancedbytes.mystuff.games.data.AuthorDataAccess;

@Path("/authors")
public class AuthorsResource {

	private static final Log _LOG = LogFactory.getLog(AuthorsResource.class);

	private AuthorDataAccess authorData = new AuthorDataAccess();
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Author> findAll() throws SQLException {
		_LOG.info("findAll()");
		return authorData.findAll();
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Author findById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findById(" + id + ")");
		return authorData.findById(MyStuffUtil.parseLong(id));
	}
	
}
