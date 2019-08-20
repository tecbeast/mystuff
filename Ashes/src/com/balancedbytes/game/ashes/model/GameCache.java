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
	private int fMaxGameNr;
	
	public GameCache() {
		fGameByNumber = new HashMap<Integer, Game>();
	}
	
	public void init(DbManager dbManager) throws SQLException {
		if (dbManager != null) {
			fDataAccess = dbManager.getGameDataAccess();
		}
		fMaxGameNr = fDataAccess.findMaxGameNr();
	}
	
	public int getMaxGameNr() {
		return fMaxGameNr;
	}
	
	private void add(Game game) {
		if (game == null) {
			return;
		}
		fGameByNumber.put(game.getGameNr(), game);
	}
	
	public Game create(String[] users) {
		fMaxGameNr++;
		Game game = new Game(getMaxGameNr(), users);
		game.setModified(true);
		add(game);
		return game;
	}
	
	
	public Game get(int gameNr) {
		Game game = fGameByNumber.get(gameNr);
		if (game != null) {
			return game;
		}
		if (fDataAccess != null) {
			try {
				game = fDataAccess.findByGameNr(gameNr);
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
			boolean success = (game.getId() > 0) ? fDataAccess.update(game) : fDataAccess.create(game);
			if (success) {
				game.setModified(false);
			}
			return success;
		} catch (SQLException sqle) {
			throw new AshesException("Error saving game(" + game.getGameNr() + ") in database.", sqle);
		}
	}

}
