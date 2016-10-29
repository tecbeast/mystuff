package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.HashMapBuilder;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Award;
import com.balancedbytes.mystuff.games.Awards;
import com.balancedbytes.mystuff.games.data.AwardDataAccess;

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
	
	public Awards findAwardsByName(String name) throws SQLException {
		Awards awards = new AwardDataAccess().findAwardsByName(name);
		buildLinks(awards, HashMapBuilder.build("name", name));
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

	private void buildLinks(Awards awards, Map<String, String> queryParams) {
		UriBuilder uriBuilderCollection = MyStuffUtil.setQueryParams(uriInfo.getRequestUriBuilder(), queryParams);
		awards.buildLinks(uriBuilderCollection.build(), getAwardsUri());
	}
	
	private URI getAwardsUri() {
    	return uriInfo.getBaseUriBuilder().path("awards").build();
    }
	
}
