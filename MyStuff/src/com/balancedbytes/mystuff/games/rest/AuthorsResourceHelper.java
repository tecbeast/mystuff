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
		return complete(authors, paging, null);
	}
	
	public Authors findAuthorsFiltered(AuthorDataFilter filter, RestDataPaging paging) throws SQLException {
		Authors authors = new AuthorDataAccess().findAuthorsFiltered(filter, paging);
		return complete(authors, paging, filter);
	}
	
	public Author findAuthorById(String id) throws SQLException {
		Author author = new AuthorDataAccess().findAuthorById(id); 
		return complete(author);
	}

	public Author createAuthor(Author author) throws SQLException {
		new AuthorDataAccess().createAuthor(author);
		return complete(author);
	}

	public Author updateAuthor(Author author) throws SQLException {
		new AuthorDataAccess().updateAuthor(author);
		return complete(author);
	}

	public Response deleteAuthor(String id) throws SQLException {
		if (new AuthorDataAccess().deleteAuthor(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}
	
	private Author complete(Author author) {
		addLinks(author, BASE_PATH);
		return author;
	}
	
	private Authors complete(Authors authors, RestDataPaging paging, RestDataFilter filter) {
		addLinks(authors, paging, filter, BASE_PATH);
		addPaging(authors, paging);
		return authors;
	}
	
}
