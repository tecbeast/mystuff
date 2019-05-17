package com.balancedbytes.game.ashes.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.db.UserDataAccess;

public class UserCache {
	
	private static final Log LOG = LogFactory.getLog(UserCache.class);
	
	private Map<String, User> fUserById;
	private UserDataAccess fUserDataAccess;
	
	public UserCache() {
		fUserById = new HashMap<String, User>();
	}
	
	public void init(UserDataAccess userDataAccess) {
		fUserDataAccess = userDataAccess;		
	}	
	
	public User get(String userId) {
		if (!AshesUtil.isProvided(userId)) {
			return null;
		}
		User user = fUserById.get(userId);
		if (user == null) {
			try {
				user = fUserDataAccess.findUserById(userId);
			} catch (SQLException sqle) {
				LOG.error("Error finding user " + userId + " in database.", sqle);
			}
			add(user);
		}
		return user;
	}
	
	public void add(User user) {
		if ((user != null) && AshesUtil.isProvided(user.getId())) {
			fUserById.put(user.getId(), user);
		}
	}

	public void save() {
		for (User user : fUserById.values()) {
			save(user);
		}
	}
	
	private boolean save(User user) {
		if (user.getRegistered() != null) {
			try {
				return fUserDataAccess.updateUser(user);
			} catch (SQLException sqle) {
				LOG.error("Error updating user " + user.getId() + " in database.", sqle);
			}
		} else {
			try {
				return fUserDataAccess.createUser(user);
			} catch (SQLException sqle) {
				LOG.error("Error creating user " + user.getId() + " in database.", sqle);
			}
		}
		return false;
	}

}
