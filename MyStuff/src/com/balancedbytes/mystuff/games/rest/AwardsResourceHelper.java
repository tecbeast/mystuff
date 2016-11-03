package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.data.AwardDataAccess;
import com.balancedbytes.mystuff.games.data.AwardDataFilter;

public class AwardsResourceHelper {

	private UriInfo uriInfo;
	
	public AwardsResourceHelper(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
		
	public Awards findAllAwards() throws SQLException {
		Awards awards = new AwardDataAccess().findAllAwards();
		buildLinks(awards);
		return awards;
	}
	
	public Awards findAwardsFiltered(AwardDataFilter filter) throws SQLException {
		Awards awards = new AwardDataAccess().findAwardsFiltered(filter);
		buildLinks(awards, filter);
		return awards;
	}
	
	public Award findAwardById(String id) throws SQLException {
		Award award = new AwardDataAccess().findAwardById(id);
		award.buildLink(getAwardsUri());
		return award;
	}

	public Award createAward(Award award) throws SQLException {
		new AwardDataAccess().createAward(award);
		award.buildLink(getAwardsUri());
		return award;
	}
	
	public Award updateAward(Award award) throws SQLException {
		new AwardDataAccess().updateAward(award);
		award.buildLink(getAwardsUri());
		return award;
	}

	public Response deleteAward(String id) throws SQLException {
		if (new AwardDataAccess().deleteAward(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}

	private void buildLinks(Awards awards) {
		buildLinks(awards, null);
	}

	private void buildLinks(Awards awards, AwardDataFilter filter) {
		UriBuilder uriBuilderCollection = MyStuffUtil.setQueryParams(uriInfo.getRequestUriBuilder(), filter);
		awards.buildLinks(uriBuilderCollection.build(), getAwardsUri());
	}
	
	private URI getAwardsUri() {
    	return uriInfo.getBaseUriBuilder().path("awards").build();
    }
	
}
