package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.db.DbManager;
import com.balancedbytes.game.ashes.db.UserDataAccess;

public class UserCache {
	
	private static final Log LOG = LogFactory.getLog(UserCache.class);
	
	private Map<String, User> fUserByName;
	private UserDataAccess fDataAccess;
	
	public UserCache() {
		fUserByName = new HashMap<String, User>();
	}
	
	public void init(DbManager dbManager) {
		if (dbManager != null) {
			fDataAccess = dbManager.getUserDataAccess();
		}
	}
	
	public User get(String userName) {
		if (!AshesUtil.provided(userName)) {
			return null;
		}
		User user = fUserByName.get(userName);
		if (user != null) {
			return user;
		}
		if (fDataAccess != null) {
			try {
				user = fDataAccess.findByUserName(userName);
			} catch (SQLException sqle) {
				LOG.error("Error finding user(" + userName + ") in database.", sqle);
			}
			add(user);
		}
		return user;
	}
	
	public void add(User user) {
		if (user == null) {
			return;
		}
		fUserByName.put(user.getUserName(), user);
	}

	public boolean save() {
		boolean success = true;
		for (User user : fUserByName.values()) {
			if (user.isModified()) {
				success &= save(user);
			}
		}
		return success;
	}
	
	private boolean save(User user) {
		if ((user == null) || (fDataAccess == null)) {
			return false;
		}
		try {
			boolean success = (user.getId() > 0) ? fDataAccess.update(user) : fDataAccess.create(user);
			if (success) {
				user.setModified(false);
			}
			return success;
		} catch (SQLException sqle) {
			throw new AshesException("Error saving user(" + user.getUserName() + ") in database.", sqle);
		}
	}

}
