package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.HashMapBuilder;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.data.AuthorDataAccess;

public class AuthorsResourceHelper {

	private UriInfo uriInfo;
	
	public AuthorsResourceHelper(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
		
	public Authors findAllAuthors() throws SQLException {
		Authors authors = new AuthorDataAccess().findAllAuthors();
		buildLinks(authors);
		return authors;
	}
	
	public Authors findAuthorsByName(String name) throws SQLException {
		Authors authors = new AuthorDataAccess().findAuthorsByName(name);
		buildLinks(authors, HashMapBuilder.build("name", name));
		return authors;
	}
	
	public Author findAuthorById(String id) throws SQLException {
		Author author = new AuthorDataAccess().findAuthorById(id);
		author.buildLink(getAuthorsUri());
		return author;
	}

	public Author createAuthor(Author author) throws SQLException {
		new AuthorDataAccess().createAuthor(author);
		author.buildLink(getAuthorsUri());
		return author;
	}

	public Response deleteAuthor(String id) throws SQLException {
		if (new AuthorDataAccess().deleteAuthor(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}

	private void buildLinks(Authors authors) {
		buildLinks(authors, null);
	}

	private void buildLinks(Authors authors, Map<String, String> queryParams) {
		UriBuilder uriBuilderCollection = MyStuffUtil.setQueryParams(uriInfo.getRequestUriBuilder(), queryParams);
		authors.buildLinks(uriBuilderCollection.build(), getAuthorsUri());
	}
	
	private URI getAuthorsUri() {
    	return uriInfo.getBaseUriBuilder().path("authors").build();
    }
	
}
