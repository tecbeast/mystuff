package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.db.DbManager;
import com.balancedbytes.game.ashes.db.PlayerMoveDataAccess;

public class PlayerMoveCache {
	
	private Map<String, PlayerMove> fMoveByGamePlayerTurn;
	private PlayerMoveDataAccess fDataAccess;
	private UserCache fUserCache;
	
	public PlayerMoveCache() {
		fMoveByGamePlayerTurn = new HashMap<String, PlayerMove>();
	}
	
	public void init(DbManager dbManager, UserCache userCache) {
		if (dbManager != null) {
			fDataAccess = dbManager.getPlayerMoveDataAccess();
		}
		fUserCache = userCache;
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
		if (move != null) {
			return move;
		}
		if (fDataAccess != null) {
			try {
				move = fDataAccess.findByGamePlayerTurn(gameNr, playerNr, turn);
				if (fUserCache != null) {
					move.setUser(fUserCache.get(move.getUserName()));
				}
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
		if ((move == null) || (fDataAccess == null)) {
			return false;
		}
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
