package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.RestDataFilter;
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.data.AwardDataAccess;
import com.balancedbytes.mystuff.games.data.AwardDataFilter;

public class AwardsResourceHelper extends ResourceHelper {
	
	public static final String BASE_PATH = "awards";

	public AwardsResourceHelper(UriInfo uriInfo) {
		super(uriInfo);
	}
		
	public Awards findAllAwards(RestDataPaging paging) throws SQLException {
		Awards awards = new AwardDataAccess().findAllAwards(paging);
		return addLinks(awards, paging, null);
	}
	
	public Awards findAwardsFiltered(AwardDataFilter filter, RestDataPaging paging) throws SQLException {
		Awards awards = new AwardDataAccess().findAwardsFiltered(filter, paging);
		return addLinks(awards, paging, filter);
	}
	
	public Award findAwardById(String id) throws SQLException {
		Award award = new AwardDataAccess().findAwardById(id);
		return addLinks(award);
	}

	public Award createAward(Award award) throws SQLException {
		new AwardDataAccess().createAward(award);
		return addLinks(award);
	}
	
	public Award updateAward(Award award) throws SQLException {
		new AwardDataAccess().updateAward(award);
		return addLinks(award);
	}

	public Response deleteAward(String id) throws SQLException {
		if (new AwardDataAccess().deleteAward(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}
	
	private Award addLinks(Award award) {
		addLinks(award, BASE_PATH);
		return award;
	}
	
	private Awards addLinks(Awards awards, RestDataPaging paging, RestDataFilter filter) {
		addLinks(awards, paging, filter, BASE_PATH);
		return awards;
	}
	
}
