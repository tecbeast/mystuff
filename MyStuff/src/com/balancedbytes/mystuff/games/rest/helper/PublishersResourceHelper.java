package com.balancedbytes.mystuff.games.rest.helper;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.games.Publisher;
import com.balancedbytes.mystuff.games.Publishers;
import com.balancedbytes.mystuff.games.data.PublisherDataAccess;
import com.balancedbytes.mystuff.games.data.filter.PublisherDataFilter;
import com.balancedbytes.mystuff.games.rest.RestDataFilter;
import com.balancedbytes.mystuff.games.rest.RestDataPaging;

public class PublishersResourceHelper extends ResourceHelper {

	public static final String BASE_PATH = "publishers";

	public PublishersResourceHelper(UriInfo uriInfo) {
		super(uriInfo);
	}
		
	public Publishers findAllPublishers(RestDataPaging paging) throws SQLException {
		Publishers publishers = new PublisherDataAccess().findAllPublishers(paging);
		return complete(publishers, paging, null);
	}
	
	public Publishers findPublishersFiltered(PublisherDataFilter filter, RestDataPaging paging) throws SQLException {
		Publishers publishers = new PublisherDataAccess().findPublishersFiltered(filter, paging);
		return complete(publishers, paging, filter);
	}
	
	public Publisher findPublisherById(String id) throws SQLException {
		Publisher publisher = new PublisherDataAccess().findPublisherById(id);
		return complete(publisher);
	}
	
	public Publisher createPublisher(Publisher publisher) throws SQLException {
		new PublisherDataAccess().createPublisher(publisher);
		return complete(publisher);
	}

	public Publisher updatePublisher(Publisher publisher) throws SQLException {
		new PublisherDataAccess().updatePublisher(publisher);
		return complete(publisher);
	}

	public Response deletePublisher(String id) throws SQLException {
		if (new PublisherDataAccess().deletePublisher(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}
	
	private Publisher complete(Publisher publisher) {
		addLinks(publisher, BASE_PATH);
		return publisher;
	}
	
	private Publishers complete(Publishers publishers, RestDataPaging paging, RestDataFilter filter) {
		addLinks(publishers, paging, filter, BASE_PATH);
		addPaging(publishers, paging);
		return publishers;
	}

}
