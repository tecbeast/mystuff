package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.HashMapBuilder;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;
import com.balancedbytes.mystuff.games.data.PublisherDataAccess;

public class PublishersResourceHelper {

	private UriInfo uriInfo;
	
	public PublishersResourceHelper(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
		
	public Publishers findAllPublishers() throws SQLException {
		Publishers publishers = new PublisherDataAccess().findAllPublishers();
		buildLinks(publishers);
		return publishers;
	}
	
	public Publishers findPublishersByName(String name) throws SQLException {
		Publishers publishers = new PublisherDataAccess().findPublishersByName(name);
		buildLinks(publishers, HashMapBuilder.build("name", name));
		return publishers;
	}
	
	public Publisher findPublisherById(String id) throws SQLException {
		Publisher publisher = new PublisherDataAccess().findPublisherById(id);
		publisher.buildLink(getPublishersUri());
		return publisher;
	}
	
	public Publisher createPublisher(Publisher publisher) throws SQLException {
		new PublisherDataAccess().createPublisher(publisher);
		publisher.buildLink(getPublishersUri());
		return publisher;
	}

	public Publisher updatePublisher(Publisher publisher) throws SQLException {
		new PublisherDataAccess().updatePublisher(publisher);
		publisher.buildLink(getPublishersUri());
		return publisher;
	}

	public Response deletePublisher(String id) throws SQLException {
		if (new PublisherDataAccess().deletePublisher(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}

	private void buildLinks(Publishers publishers) {
		buildLinks(publishers, null);
	}

	private void buildLinks(Publishers publishers, Map<String, String> queryParams) {
		UriBuilder uriBuilderCollection = MyStuffUtil.setQueryParams(uriInfo.getRequestUriBuilder(), queryParams);
		publishers.buildLinks(uriBuilderCollection.build(), getPublishersUri());
	}
	
	private URI getPublishersUri() {
    	return uriInfo.getBaseUriBuilder().path("publishers").build();
    }
	
}
