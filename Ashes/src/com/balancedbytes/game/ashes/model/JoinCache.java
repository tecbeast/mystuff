package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.db.DbManager;
import com.balancedbytes.game.ashes.db.JoinDataAccess;

public class JoinCache {
	
	private static final Log LOG = LogFactory.getLog(JoinCache.class);
	
	private Map<String, Join> fJoinByUserName;
	private JoinDataAccess fDataAccess;
	
	public JoinCache() {
		fJoinByUserName = new HashMap<String, Join>();
	}
	
	public void init(DbManager dbManager) {
		if (dbManager != null) {
			fDataAccess = dbManager.getJoinDataAccess();
		}
	}
	
	public Join getByUserName(String userName) {
		if (!AshesUtil.provided(userName)) {
			return null;
		}
		Join join = fJoinByUserName.get(userName);
		if (join != null) {
			return join;
		}
		if (fDataAccess != null) {
			try {
				join = fDataAccess.findByUserName(userName);
			} catch (SQLException sqle) {
				LOG.error("Error finding join(" + userName + ") in database.", sqle);
			}
			add(join);
		}
		return join;
	}
	
	public List<Join> getByGameName(String gameName) {
		String myGameName = AshesUtil.print(gameName);
		List<Join> joins = new ArrayList<Join>();
		if (fDataAccess != null) {
			try {
				joins.addAll(fDataAccess.findByGameName(myGameName));
			} catch (SQLException sqle) {
				LOG.error("Error finding joins(" + myGameName + ") in database.", sqle);
			}
		}
		for (Join join : fJoinByUserName.values()) {
			if ((join.getId() == 0) && myGameName.equals(join.getGameName())) {
				joins.add(join);
			}
		}
		return joins;
	}
	
	public void add(Join join) {
		if (join == null) {
			return;
		}
		fJoinByUserName.put(join.getUserName(), join);
	}

	public boolean save() {
		boolean success = true;
		for (Join join : fJoinByUserName.values()) {
			if (join.isModified()) {
				success &= save(join);
			}
		}
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

}
