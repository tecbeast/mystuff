package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.RestData;
import com.balancedbytes.mystuff.RestDataCollection;
import com.balancedbytes.mystuff.RestDataFilter;
import com.balancedbytes.mystuff.RestDataPaging;

public abstract class ResourceHelper {
	
	private static final String _REL_SELF = "self";
	private static final String _REL_PREV = "prev";
	private static final String _REL_NEXT = "next";
	
	private UriInfo uriInfo;
	
	protected ResourceHelper(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	public UriInfo getUriInfo() {
		return uriInfo;
	}

	protected void addLinks(RestData data, String basePath) {
		if (data != null) {
			data.clearLinks();
			UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(basePath).path(data.getId());
			data.addLink(Link.fromUriBuilder(uriBuilder).rel("self").build());
		}
	}

	protected void addLinks(RestDataCollection<?> dataCollection, RestDataPaging paging, RestDataFilter filter, String basePath) {
		dataCollection.clearLinks();
		UriBuilder uriBuilder = getUriInfo().getRequestUriBuilder();
		uriBuilder.replaceQuery(null);
		URI linkUri = uriBuilder.build();
		if (paging != null) {
			RestDataPaging prevPage = paging.createPrev();
			RestDataPaging nextPage = paging.createNext();
			if ((prevPage != null) || (nextPage != null)) {
				dataCollection.addLink(createLink(linkUri, paging, filter, _REL_SELF));
			} else {
				dataCollection.addLink(createLink(linkUri, null, filter, _REL_SELF));
			}
			if (prevPage != null) {
				dataCollection.addLink(createLink(linkUri, prevPage, filter, _REL_PREV));
			}
			if (nextPage != null) {
				dataCollection.addLink(createLink(linkUri, nextPage, filter, _REL_NEXT));
			}
		} else {
			dataCollection.addLink(createLink(linkUri, paging, filter, _REL_SELF));
		}
		for (RestData data : dataCollection.getElements()) {
			addLinks(data, basePath);
		}
	}

	private Link createLink(URI uri, RestDataPaging paging, RestDataFilter filter, String rel) {
		UriBuilder uriBuilder = UriBuilder.fromUri(uri);
		SortedMap<String, Object> params = new TreeMap<String, Object>();
		if (paging != null) {
			params.putAll(paging.toSortedMap());
		}
		if (filter != null) {
			params.putAll(filter.toSortedMap());
		}
		for (String key : params.keySet()) {
			uriBuilder.queryParam(key, params.get(key));
		}
		return Link.fromUriBuilder(uriBuilder).rel(rel).build();		
	}

}
