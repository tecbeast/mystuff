package com.balancedbytes.mystuff.games.rest.api;

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

import com.balancedbytes.mystuff.games.Note;
import com.balancedbytes.mystuff.games.Notes;
import com.balancedbytes.mystuff.games.rest.RestDataPaging;
import com.balancedbytes.mystuff.games.rest.helper.NotesResourceHelper;
import com.balancedbytes.mystuff.rest.compress.Compress;

@Path("/notes")
public class NotesResource {

	private static final Log _LOG = LogFactory.getLog(NotesResource.class);

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Compress
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Notes findNotes(
		@QueryParam("page") String page,
		@QueryParam("pageSize") String pageSize
	) throws SQLException {
		_LOG.info("findAllNotes()");
		RestDataPaging paging = new RestDataPaging();
		paging.init(page, pageSize);
		return new NotesResourceHelper(uriInfo).findAllNotes(paging);
	}
	
	@POST
	@Compress
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Note createNote(Note note) throws SQLException {
		_LOG.info("createNote()");
		return new NotesResourceHelper(uriInfo).createNote(note);
	}
	
	@GET
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Note findNoteById(@PathParam("id") String id) throws SQLException {
		_LOG.info("findNoteById(" + id + ")");
		return new NotesResourceHelper(uriInfo).findNoteById(id);
	}
	
	@PUT
	@Path("{id}")
	@Compress
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Note updateNote(@PathParam("id") String id, Note note) throws SQLException {
		_LOG.info("updateImage(" + id + ")");
		note.setId(id);
		return new NotesResourceHelper(uriInfo).updateNote(note);
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteImage(@PathParam("id") String id) throws SQLException {
		_LOG.info("deleteImage(" + id + ")");
		return new NotesResourceHelper(uriInfo).deleteNote(id);
	}

}
