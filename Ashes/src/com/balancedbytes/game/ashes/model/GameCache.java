package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.db.DbManager;
import com.balancedbytes.game.ashes.db.GameDataAccess;

public class GameCache {
	
	private Map<Integer, Game> fGameByNumber;
	private GameDataAccess fDataAccess;
	
	public GameCache() {
		fGameByNumber = new HashMap<Integer, Game>();
	}
	
	public void init(DbManager dbManager) {
		if (dbManager != null) {
			fDataAccess = dbManager.getGameDataAccess();
		}
	}
	
	public void add(Game game) {
		if (game == null) {
			return;
		}
		fGameByNumber.put(game.getNumber(), game);
	}
	
	public Game get(int gameNr) {
		Game game = fGameByNumber.get(gameNr);
		if (game != null) {
			return game;
		}
		if (fDataAccess != null) {
			try {
				game = fDataAccess.findByNumber(gameNr);
			} catch (SQLException sqle) {
				throw new AshesException("Error finding game(" + gameNr + ") in database.", sqle);
			}
			add(game);
		}
		return game;
	}
	
	public boolean save() {
		boolean success = true;
		for (Game game : fGameByNumber.values()) {
			success &= save(game);
		}
		return success;
	}
	
	private boolean save(Game game) {
		if ((game == null) || (fDataAccess == null)) {
			return false;
		}
		try {
			if (game.getId() > 0) {
				return fDataAccess.update(game);
			} else {
				return fDataAccess.create(game);
			}
		} catch (SQLException sqle) {
			throw new AshesException("Error saving game(" + game.getNumber() + ") in database.", sqle);
		}
	}

}
