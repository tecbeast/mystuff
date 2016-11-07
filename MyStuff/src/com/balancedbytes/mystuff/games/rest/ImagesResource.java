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

import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Image;
import com.balancedbytes.mystuff.games.Images;
import com.balancedbytes.mystuff.rest.compress.Compress;

@Path("/images")
public class ImagesResource {

	private static final Log _LOG = LogFactory.getLog(ImagesResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Images findImages(
		@QueryParam("page") String page,
		@QueryParam("pageSize") String pageSize
	) throws SQLException {
		_LOG.info("findAllImages()");
		RestDataPaging paging = new RestDataPaging();
		paging.init(page, pageSize);
		return new ImagesResourceHelper(uriInfo).findAllImages(paging);
	}
	
	@POST
	@Compress
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Image createImage(Image image) throws SQLException {
		_LOG.info("createImage()");
		return new ImagesResourceHelper(uriInfo).createImage(image);
	}
	
	@GET
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Image findImageById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findImageById(" + id + ")");
		return new ImagesResourceHelper(uriInfo).findImageById(id);
	}
	
	@PUT
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Image updateImage(@PathParam("id") String id, Image image) throws SQLException {
		_LOG.info("updateImage(" + id + ")");
		image.setId(id);
		return new ImagesResourceHelper(uriInfo).updateImage(image);
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteImage(@PathParam("id") String id) throws SQLException {
		_LOG.info("deleteImage(" + id + ")");
		return new ImagesResourceHelper(uriInfo).deleteImage(id);
	}

}
