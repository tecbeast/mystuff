package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.db.DbManager;
import com.balancedbytes.game.ashes.db.JoinDataAccess;

public class JoinCache {
	
	private static final Log LOG = LogFactory.getLog(JoinCache.class);
	
	private Map<Long, Join> fJoinById;
	private Set<Long> fDeletedIds;
	private JoinDataAccess fDataAccess;
	
	public JoinCache() {
		fJoinById = new HashMap<Long, Join>();
		fDeletedIds = new HashSet<Long>();
	}
	
	public void init(DbManager dbManager) {
		if (dbManager != null) {
			fDataAccess = dbManager.getJoinDataAccess();
		}
	}
	
	public List<Join> getByUserName(String userName) {
		List<Join> joins = new ArrayList<Join>();
		if (!AshesUtil.provided(userName)) {
			return joins;
		}
		for (Join join : fJoinById.values()) {
			if (userName.equals(join.getUserName())) {
				joins.add(join);
			}
		}
		if (joins.size() > 0) {
			return joins;
		}
		if (fDataAccess != null) {
			try {
				List<Join> dbJoins = fDataAccess.findByUserName(userName);
				for (Join join : dbJoins) {
					add(join);
					joins.add(join);
				}					
			} catch (SQLException sqle) {
				LOG.error("Error finding joins for username \"" + userName + "\" in database.", sqle);
			}
		}
		return joins;
	}
	
	public List<Join> getByGameName(String gameName) {
		List<Join> joins = new ArrayList<Join>();
		if (!AshesUtil.provided(gameName)) {
			return joins;
		}
		for (Join join : fJoinById.values()) {
			if (gameName.equals(join.getGameName())) {
				joins.add(join);
			}
		}
		if (joins.size() > 0) {
			return joins;
		}
		if (fDataAccess != null) {
			try {
				List<Join> dbJoins = fDataAccess.findByGameName(gameName);
				for (Join join : dbJoins) {
					add(join);
					joins.add(join);
				}
			} catch (SQLException sqle) {
				LOG.error("Error finding joins for gameName \"" + gameName + "\" in database.", sqle);
			}
		}
		return joins;
	}
	
	public Join create(String userName, String gameName) {
		Join join = new Join();
		join.setUserName(userName);
		join.setGameName(gameName);
		join.setJoined(new Date());
		join.setModified(true);
		add(join);
		return join;
	}
	
	private void add(Join join) {
		if (join == null) {
			return;
		}
		fJoinById.put(join.getId(), join);
	}
	
	public void delete(Join join) {
		if ((join != null) && (join.getId() > 0)) {
			fJoinById.remove(join.getId());
			fDeletedIds.add(join.getId());
		}
	}

	public boolean save() {
		boolean success = true;
		for (Join join : fJoinById.values()) {
			if (join.isModified()) {
				success &= save(join);
			}
		}
		for (Long id : fDeletedIds) {
			success &= delete(id);
		}
		fDeletedIds.clear();
		return success;
	}
	
	private boolean save(Join join) {
		if ((join == null) || (fDataAccess == null)) {
			return false;
		}
		try {
			boolean success = (join.getId() > 0) ? fDataAccess.update(join) : fDataAccess.create(join);
			if (success) {
				join.setModified(false);
			}
			return success;
		} catch (SQLException sqle) {
			throw new AshesException("Error saving join(" + join.getUserName() + ") in database.", sqle);
		}
	}
	
	private boolean delete(long id) {
		try {
			return fDataAccess.delete(id);
		} catch (SQLException sqle) {
			throw new AshesException("Error deleting join(" + id + ") in database.", sqle);
		}
	}

}
