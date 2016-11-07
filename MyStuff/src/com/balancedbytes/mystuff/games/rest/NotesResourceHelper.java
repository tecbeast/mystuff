package com.balancedbytes.mystuff.games.rest;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Note;
import com.balancedbytes.mystuff.games.Notes;
import com.balancedbytes.mystuff.games.data.NoteDataAccess;

public class NotesResourceHelper extends ResourceHelper {

	public static final String BASE_PATH = "notes";
	
	public NotesResourceHelper(UriInfo uriInfo) {
		super(uriInfo);
	}
		
	public Notes findAllNotes(RestDataPaging paging) throws SQLException {
		Notes notes = new NoteDataAccess().findAllNotes(paging);
		return addLinks(notes, paging);
	}
	
	public Note findNoteById(String id) throws SQLException {
		Note note = new NoteDataAccess().findNoteById(id);
		return addLinks(note);
	}
	
	public Note createNote(Note note) throws SQLException {
		new NoteDataAccess().createNote(note);
		return addLinks(note);
	}

	public Note updateNote(Note note) throws SQLException {
		new NoteDataAccess().updateNote(note);
		return addLinks(note);
	}

	public Response deleteNote(String id) throws SQLException {
		if (new NoteDataAccess().deleteNote(id)) {
		    return Response.noContent().build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}
	
	private Note addLinks(Note note) {
		addLinks(note, BASE_PATH);
		return note;
	}
	
	private Notes addLinks(Notes notes, RestDataPaging paging) {
		addLinks(notes, paging, null, BASE_PATH);
		return notes;
	}
	
}
