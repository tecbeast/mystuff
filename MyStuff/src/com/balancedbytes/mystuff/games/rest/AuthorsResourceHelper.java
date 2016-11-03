package com.balancedbytes.mystuff.games.rest;

import java.net.URI;
import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.data.AuthorDataAccess;
import com.balancedbytes.mystuff.games.data.AuthorDataFilter;

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
	
	public Authors findAuthorsFiltered(AuthorDataFilter filter) throws SQLException {
		Authors authors = new AuthorDataAccess().findAuthorsFiltered(filter);
		buildLinks(authors, filter);
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

	public Author updateAuthor(Author author) throws SQLException {
		new AuthorDataAccess().updateAuthor(author);
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

	private void buildLinks(Authors authors, AuthorDataFilter filter) {
		UriBuilder uriBuilderCollection = MyStuffUtil.setQueryParams(uriInfo.getRequestUriBuilder(), filter);
		authors.buildLinks(uriBuilderCollection.build(), getAuthorsUri());
	}
	
	private URI getAuthorsUri() {
    	return uriInfo.getBaseUriBuilder().path("authors").build();
    }
	
}
