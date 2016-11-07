package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.RestDataFilter;
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Author;
import com.balancedbytes.mystuff.games.Authors;
import com.balancedbytes.mystuff.games.data.AuthorDataAccess;
import com.balancedbytes.mystuff.games.data.AuthorDataFilter;

public class AuthorsResourceHelper extends ResourceHelper {
	
	public static final String BASE_PATH = "authors";

	public AuthorsResourceHelper(UriInfo uriInfo) {
		super(uriInfo);
	}
		
	public Authors findAllAuthors(RestDataPaging paging) throws SQLException {
		Authors authors = new AuthorDataAccess().findAllAuthors(paging);
		return addLinks(authors, paging, null);
	}
	
	public Authors findAuthorsFiltered(AuthorDataFilter filter, RestDataPaging paging) throws SQLException {
		Authors authors = new AuthorDataAccess().findAuthorsFiltered(filter, paging);
		return addLinks(authors, paging, filter);
	}
	
	public Author findAuthorById(String id) throws SQLException {
		Author author = new AuthorDataAccess().findAuthorById(id); 
		return addLinks(author);
	}

	public Author createAuthor(Author author) throws SQLException {
		new AuthorDataAccess().createAuthor(author);
		return addLinks(author);
	}

	public Author updateAuthor(Author author) throws SQLException {
		new AuthorDataAccess().updateAuthor(author);
		return addLinks(author);
	}

	public Response deleteAuthor(String id) throws SQLException {
		if (new AuthorDataAccess().deleteAuthor(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}
	
	private Author addLinks(Author author) {
		addLinks(author, BASE_PATH);
		return author;
	}
	
	private Authors addLinks(Authors authors, RestDataPaging paging, RestDataFilter filter) {
		addLinks(authors, paging, filter, BASE_PATH);
		return authors;
	}
	
}
