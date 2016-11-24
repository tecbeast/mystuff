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
	
	private static final String _REL_START = "start";
	private static final String _REL_PREV = "prev";
	private static final String _REL_SELF = "self";
	private static final String _REL_NEXT = "next";
	private static final String _REL_END = "end";
	
	private UriInfo uriInfo;
	
	protected ResourceHelper(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	public UriInfo getUriInfo() {
		return uriInfo;
	}

	protected void addLinks(RestData data, String basePath) {
		if (data != null) {
			UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(basePath).path(data.getId());
			data.setLink(Link.fromUriBuilder(uriBuilder).rel("self").build());
		}
	}

	protected void addLinks(RestDataCollection<?> dataCollection, RestDataPaging paging, RestDataFilter filter, String basePath) {
		dataCollection.clearLinks();
		UriBuilder uriBuilder = getUriInfo().getRequestUriBuilder();
		uriBuilder.replaceQuery(null);
		URI linkUri = uriBuilder.build();
		if (paging != null) {
			RestDataPaging startPage = paging.createStartPage();
			RestDataPaging prevPage = paging.createPrevPage();
			RestDataPaging nextPage = paging.createNextPage();
			RestDataPaging endPage = paging.createEndPage();
			if (prevPage != null) {
				if ((startPage != null) && !startPage.equals(prevPage))  {
					dataCollection.addLink(createLink(linkUri, startPage, filter, _REL_START));
				}
				dataCollection.addLink(createLink(linkUri, prevPage, filter, _REL_PREV));
			}
			if ((prevPage != null) || (nextPage != null)) {
				dataCollection.addLink(createLink(linkUri, paging, filter, _REL_SELF));
			} else {
				dataCollection.addLink(createLink(linkUri, null, filter, _REL_SELF));
			}
			if (nextPage != null) {
				dataCollection.addLink(createLink(linkUri, nextPage, filter, _REL_NEXT));
				if ((endPage != null) && !endPage.equals(nextPage)) {
					dataCollection.addLink(createLink(linkUri, endPage, filter, _REL_END));
				}
			}
		} else {
			dataCollection.addLink(createLink(linkUri, paging, filter, _REL_SELF));
		}
		for (RestData data : dataCollection.getElements()) {
			addLinks(data, basePath);
		}
	}
	
	protected void addPaging(RestDataCollection<?> dataCollection, RestDataPaging paging) {
		if ((dataCollection != null) && (paging != null) && (paging.getPage() > 0) && (paging.getPageSize() > 0)) {
			if (paging.getCount() > dataCollection.getEntries()) {
				dataCollection.setEntriesTotal(paging.getCount());
				dataCollection.setPage(paging.getPage());
			}
			if (paging.pagesTotal() > 1) {
				dataCollection.setPagesTotal(paging.pagesTotal());
			}
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
