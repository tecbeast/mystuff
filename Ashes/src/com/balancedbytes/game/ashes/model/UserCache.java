package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesException;
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
	
	public User get(String name) {
		if (name == null) {
			return null;
		}
		User user = fUserByName.get(name);
		if (user != null) {
			return user;
		}
		if (fDataAccess != null) {
			try {
				user = fDataAccess.findByName(name);
			} catch (SQLException sqle) {
				LOG.error("Error finding user(" + name + ") in database.", sqle);
			}
			add(user);
		}
		return user;
	}
	
	public void add(User user) {
		if (user == null) {
			return;
		}
		fUserByName.put(user.getName(), user);
	}

	public boolean save() {
		boolean success = true;
		for (User user : fUserByName.values()) {
			success &= save(user);
		}
		return success;
	}
	
	private boolean save(User user) {
		if ((user == null) || (fDataAccess == null)) {
			return false;
		}
		try {
			if (user.getId() > 0) {
				return fDataAccess.update(user);
			} else {
				return fDataAccess.create(user);
			}
		} catch (SQLException sqle) {
			throw new AshesException("Error saving user(" + user.getName() + ") in database.", sqle);
		}
	}

}
