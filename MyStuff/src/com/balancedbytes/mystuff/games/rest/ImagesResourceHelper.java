package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Image;
import com.balancedbytes.mystuff.games.Images;
import com.balancedbytes.mystuff.games.data.ImageDataAccess;

public class ImagesResourceHelper extends ResourceHelper {

	public static final String BASE_PATH = "images";
	
	public ImagesResourceHelper(UriInfo uriInfo) {
		super(uriInfo);
	}
		
	public Images findAllImages(RestDataPaging paging) throws SQLException {
		Images images = new ImageDataAccess().findAllImages(paging);
		return complete(images, paging);
	}
	
	public Image findImageById(String id) throws SQLException {
		Image image = new ImageDataAccess().findImageById(id);
		return complete(image);
	}
	
	public Image createImage(Image image) throws SQLException {
		new ImageDataAccess().createImage(image);
		return complete(image);
	}

	public Image updateImage(Image image) throws SQLException {
		new ImageDataAccess().updateImage(image);
		return complete(image);
	}

	public Response deleteImage(String id) throws SQLException {
		if (new ImageDataAccess().deleteImage(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}
	
	private Image complete(Image image) {
		addLinks(image, BASE_PATH);
		return image;
	}
	
	private Images complete(Images images, RestDataPaging paging) {
		addLinks(images, paging, null, BASE_PATH);
		addPaging(images, paging);
		return images;
	}
	
}
