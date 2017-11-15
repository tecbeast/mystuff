package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.Note;
import com.balancedbytes.mystuff.games.Notes;
import com.balancedbytes.mystuff.games.rest.RestDataAccess;
import com.balancedbytes.mystuff.games.rest.RestDataPaging;

public class NoteDataAccess extends RestDataAccess<Note> {

	private static final String SQL_FIND_ALL_NOTES = 
		"SELECT * FROM publishers ORDER BY name";
	private static final String SQL_FIND_NOTE_BY_ID =
		"SELECT * FROM publishers WHERE id = ?";
	private static final String SQL_FIND_NOTES_BY_GAME_ID =
		"SELECT game_notes.game_id, notes.*"
		+ " FROM game_notes LEFT JOIN notes"
		+ " ON game_notes.note_id = notes.id"
		+ " WHERE game_notes.game_id = ?"
		+ " ORDER BY notes.timestamp DESC";
	private static final String SQL_CREATE_NOTE =
		"INSERT INTO notes"
		+ " (timestamp, text)"
		+ " VALUES (?, ?)";
	private static final String SQL_UPDATE_NOTE =
		"UPDATE notes"
		+ " SET timestamp = ?, text = ?"
		+ " WHERE id = ?";
	private static final String SQL_DELETE_NOTE =
		"DELETE FROM notes WHERE id = ?";

    public Notes findAllNotes(RestDataPaging paging) throws SQLException {
    	Notes notes = new Notes();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(SQL_FIND_ALL_NOTES);
            processResultSet(ps.executeQuery(), notes, paging);
		}
        return notes;
    }

    public Note findNoteById(String id) throws SQLException {
    	Note note = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(SQL_FIND_NOTE_BY_ID);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            note = processResultSet(ps.executeQuery());
		}
        return note;
    }
    
    public Notes findNotesByGameId(String gameId, RestDataPaging paging) throws SQLException {
    	Notes notes = new Notes();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(SQL_FIND_NOTES_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            processResultSet(ps.executeQuery(), notes, paging);
		}
        return notes;
    }

    public void createNote(Note note) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(SQL_CREATE_NOTE, new String[] { "id" });
            ps.setTimestamp(1, MyStuffUtil.toTimestamp(note.getTimestamp()));
            ps.setString(2, note.getText());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	note.setId(rs.getString(1));
            }
        }
    }
    
    public boolean updateNote(Note note) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(SQL_UPDATE_NOTE);
            ps.setTimestamp(1, MyStuffUtil.toTimestamp(note.getTimestamp()));
            ps.setString(2, note.getText());
            ps.setLong(3, MyStuffUtil.parseLong(note.getId()));
            return (ps.executeUpdate() == 1);
        }
    }
    
    public boolean deleteNote(String id) throws SQLException {
    	int count = 0;
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(SQL_DELETE_NOTE);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            count = ps.executeUpdate();
        }
        return (count == 1);
    }

    @Override
    protected Note processRow(ResultSet rs) throws SQLException {
    	Note note = new Note();
    	note.setId(rs.getString("id"));
    	note.setTimestamp(MyStuffUtil.toString(rs.getTimestamp("timestamp")));
    	note.setText(rs.getString("text"));
        return note;
    }
    
}
