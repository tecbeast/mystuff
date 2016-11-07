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
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.Games;
import com.balancedbytes.mystuff.games.data.AwardDataFilter;
import com.balancedbytes.mystuff.games.data.GameDataFilter;
import com.balancedbytes.mystuff.rest.compress.Compress;

@Path("/awards")
public class AwardsResource {

	private static final Log _LOG = LogFactory.getLog(AwardsResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Awards findAwards(
		@QueryParam("name") String name,
		@QueryParam("page") String page,
		@QueryParam("pageSize") String pageSize
	) throws SQLException {
		RestDataPaging paging = new RestDataPaging();
		paging.init(page, pageSize);
		AwardDataFilter filter = new AwardDataFilter();
		filter.setName(name);
		if (filter.isEmpty()) {
			_LOG.info("findAllAwards()");
			return new AwardsResourceHelper(uriInfo).findAllAwards(paging);
		} else {
			_LOG.info("findAwardsFiltered(" + filter + ")");
			return new AwardsResourceHelper(uriInfo).findAwardsFiltered(filter, paging);
		}
	}

	@POST
	@Compress
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Award createAward(Award award) throws SQLException {
		_LOG.info("createAward()");
		return new AwardsResourceHelper(uriInfo).createAward(award);
	}
	
	@GET
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Award findAwardById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findAwardById(" + id + ")");
		return new AwardsResourceHelper(uriInfo).findAwardById(id);
	}
	
	@PUT
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Award updateAward(@PathParam("id") String id, Award award) throws SQLException {
		_LOG.info("updateAward(" + id + ")");
		award.setId(id);
		return new AwardsResourceHelper(uriInfo).updateAward(award);
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteAward(@PathParam("id") String id) throws SQLException {
		_LOG.info("deleteAward(" + id + ")");
		return new AwardsResourceHelper(uriInfo).deleteAward(id);
	}

	@GET
	@Path("{id}/games")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Games findGamesByAwardId(
		@PathParam("id") String id,
		@QueryParam("year") String year,
		@QueryParam("page") String page,
		@QueryParam("pageSize") String pageSize
	) throws SQLException {
		RestDataPaging paging = new RestDataPaging();
		paging.init(page, pageSize);
		GameDataFilter filter = new GameDataFilter();
		filter.setYear(MyStuffUtil.parseInt(year));
		if (filter.isEmpty()) {
			_LOG.info("findGamesByAwardId(" + id + ")");
			return new GamesResourceHelper(uriInfo).findGamesByAwardId(id, paging);
		} else {
			_LOG.info("findGamesByAwardIdFiltered(" + id + "," + filter + ")");
			return new GamesResourceHelper(uriInfo).findGamesByAwardIdFiltered(id, filter, paging);
		}
	}

}
