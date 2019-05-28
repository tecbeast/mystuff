package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.db.DbManager;
import com.balancedbytes.game.ashes.db.MoveDataAccess;

public class MoveCache {
	
	private Map<String, Move> fMoveByGamePlayerTurn;
	private MoveDataAccess fDataAccess;
	
	public MoveCache() {
		fMoveByGamePlayerTurn = new HashMap<String, Move>();
	}
	
	public void init(DbManager dbManager) {
		if (dbManager != null) {
			fDataAccess = dbManager.getMoveDataAccess();
		}
	}

	private String createKey(Move move) {
		if (move == null) {
			return null;
		}
		return createKey(move.getGameNr(), move.getPlayerNr(), move.getTurn());
	}
	
	private String createKey(int gameNr, int playerNr, int turn) {
		return new StringBuilder()
			.append(gameNr).append(".")
			.append(playerNr).append(".")
			.append(turn)
			.toString();
	}
	
	public void add(Move move) {
		if (move == null) {
			return;
		}
		fMoveByGamePlayerTurn.put(createKey(move), move);
	}
	
	public Move get(int gameNr, int playerNr, int turn) {
		Move move = fMoveByGamePlayerTurn.get(createKey(gameNr, playerNr, turn));
		if (move != null) {
			return move;
		}
		if (fDataAccess != null) {
			try {
				move = fDataAccess.findByGameNrPlayerNrTurn(gameNr, playerNr, turn);
			} catch (SQLException sqle) {
				throw new AshesException("Error finding playerMove(" + gameNr + "," + playerNr + "," + turn + ") in database.", sqle);
			}
			add(move);
		}
		return move;
	}
	
	public boolean save() {
		boolean success = true;
		for (Move move : fMoveByGamePlayerTurn.values()) {
			if (move.isModified()) {
				success &= save(move);
			}
		}
		return success;
	}
	
	private boolean save(Move move) {
		if ((move == null) || (fDataAccess == null)) {
			return false;
		}
		try {
			boolean success = (move.getId() > 0)  ? fDataAccess.update(move) : fDataAccess.create(move);
			if (success) {
				move.setModified(false);
			}
			return success;
		} catch (SQLException sqle) {
			throw new AshesException("Error saving playerMove(" + move.getGameNr() + "," + move.getPlayerNr() + "," + move.getTurn() + ") in database.", sqle);
		}
	}

}
