package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.db.PlayerMoveDataAccess;

public class PlayerMoveCache {
	
	private Map<String, PlayerMove> fMoveByGamePlayerTurn;
	private PlayerMoveDataAccess fDataAccess;
	
	public PlayerMoveCache(PlayerMoveDataAccess dataAccess) {
		fMoveByGamePlayerTurn = new HashMap<String, PlayerMove>();
		fDataAccess = dataAccess;
	}
	
	private String createKey(PlayerMove move) {
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
	
	public void add(PlayerMove move) {
		if (move == null) {
			return;
		}
		fMoveByGamePlayerTurn.put(createKey(move), move);
	}
	
	public PlayerMove get(int gameNr, int playerNr, int turn) {
		PlayerMove move = fMoveByGamePlayerTurn.get(createKey(gameNr, playerNr, turn));
		if (move == null) {
			try {
				move = fDataAccess.findByGamePlayerTurn(gameNr, playerNr, turn);
			} catch (SQLException sqle) {
				throw new AshesException("Error finding playerMove(" + gameNr + "," + playerNr + "," + turn + ") in database.", sqle);
			}
			add(move);
		}
		return move;
	}
	
	public boolean save() {
		boolean success = true;
		for (PlayerMove move : fMoveByGamePlayerTurn.values()) {
			success &= save(move);
		}
		return success;
	}
	
	private boolean save(PlayerMove move) {
		try {
			if (move.getId() > 0) {
				return fDataAccess.update(move);
			} else {
				return fDataAccess.create(move);
			}
		} catch (SQLException sqle) {
			throw new AshesException("Error saving playerMove(" + move.getGameNr() + "," + move.getPlayerNr() + "," + move.getTurn() + ") in database.", sqle);
		}
	}

}
