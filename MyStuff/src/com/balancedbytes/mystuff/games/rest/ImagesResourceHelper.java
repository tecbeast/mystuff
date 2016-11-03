package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Image;
import com.balancedbytes.mystuff.games.Images;
import com.balancedbytes.mystuff.games.data.ImageDataAccess;

public class ImagesResourceHelper {

	private UriInfo uriInfo;
	
	public ImagesResourceHelper(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
		
	public Images findAllImages() throws SQLException {
		Images images = new ImageDataAccess().findAllImages();
		buildLinks(images);
		return images;
	}
	
	public Image findImageById(String id) throws SQLException {
		Image image = new ImageDataAccess().findImageById(id);
		image.buildLink(getImagesUri());
		return image;
	}
	
	public Image createImage(Image image) throws SQLException {
		new ImageDataAccess().createImage(image);
		image.buildLink(getImagesUri());
		return image;
	}

	public Image updateImage(Image image) throws SQLException {
		new ImageDataAccess().updateImage(image);
		image.buildLink(getImagesUri());
		return image;
	}

	public Response deleteImage(String id) throws SQLException {
		if (new ImageDataAccess().deleteImage(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}

	private void buildLinks(Images images) {
		UriBuilder uriBuilderCollection = MyStuffUtil.setQueryParams(uriInfo.getRequestUriBuilder(), null);
		images.buildLinks(uriBuilderCollection.build(), getImagesUri());
	}
	
	private URI getImagesUri() {
    	return uriInfo.getBaseUriBuilder().path("images").build();
    }
	
}
