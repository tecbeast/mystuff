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

import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.data.AwardDataAccess;

@Path("/awards")
public class AwardsResource {

	private static final Log _LOG = LogFactory.getLog(AwardsResource.class);

	@Context
	private UriInfo uriInfo;
	private AwardDataAccess awardData = new AwardDataAccess();
	private GamesResourceHelper gamesHelper = new GamesResourceHelper();
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Awards findAwards() throws SQLException {
		_LOG.info("findAllAwards()");
		Awards awards = awardData.findAllAwards();
		awards.setHref(getAwardsUriBuilder().toString());
		awards.buildHrefOnChildren(getAwardsUriBuilder());
		return awards;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Award findAwardById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAwardById(" + id + ")");
		Award award = awardData.findAwardById(id);
		award.buildHref(getAwardsUriBuilder());
		return award;
	}
	
	@GET
	@Path("{id}/games")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Games findGamesByAwardId(@PathParam("id") String id) throws SQLException {
		_LOG.info("findGamesByAwardId(" + id + ")");
		return gamesHelper.findGamesByAwardId(id, uriInfo);
	}
	
	private UriBuilder getAwardsUriBuilder() {
		return uriInfo.getBaseUriBuilder().path("awards");
	}
	
}
