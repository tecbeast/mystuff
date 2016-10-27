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
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Games;

@Path("/awards")
public class AwardsResource {

	private static final Log _LOG = LogFactory.getLog(AwardsResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Awards findAwards(@QueryParam("name") String name) throws SQLException {
		if (MyStuffUtil.isProvided(name)) {
			_LOG.info("findAwardsByName(" + name + ")");
			return new AwardsResourceHelper(uriInfo).findAwardsByName(name);
		} else {
			_LOG.info("findAllAwards()");
			return new AwardsResourceHelper(uriInfo).findAllAwards();
		}
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Award findAwardById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAwardById(" + id + ")");
		return new AwardsResourceHelper(uriInfo).findAwardById(id);
	}
	
	@GET
	@Path("{id}/games")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Games findGamesByAwardId(@PathParam("id") String id, @QueryParam("year") String year) throws SQLException {
		if (MyStuffUtil.parseInt(year) > 0) {
			_LOG.info("findGamesByAwardIdAndYear(" + id + ", " + year + ")");
			return new GamesResourceHelper(uriInfo).findGamesByAwardIdAndYear(id, year);
		} else {
			_LOG.info("findGamesByAwardId(" + id + ")");
			return new GamesResourceHelper(uriInfo).findGamesByAwardId(id);
		}
	}
	
}
