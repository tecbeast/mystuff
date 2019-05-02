package com.balancedbytes.game.ashes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.WriterConfig;

public class UserCache {
	
	private static final String FILE_SUFFIX = ".json";
	
	private Map<String, User> fUserById;
	private File fUserDir;
	
	public UserCache() {
		fUserById = new HashMap<String, User>();
	}
	
	public UserCache init(File userDir) {
		if ((userDir == null) || !userDir.exists() || !userDir.isDirectory()) {
			throw new UserCacheException("Error reading users directory.");
		}
		fUserDir = userDir;
		return this;
	}
	
	public User getUser(String id) {
		if (id == null) {
			return null;
		}
		User user = fUserById.get(id);
		if (user == null) {
			user = loadUser(id);
			if (user != null) {
				fUserById.put(id, user);
			}
		}
		return user;
	}
	
	public boolean save() {
		for (String id : fUserById.keySet()) {
			if (!saveUser(fUserById.get(id))) {
				return false;
			}
		}
		return true;
	}
	
	private User loadUser(String id) {
		if (id == null) {
			return null;
		}
		File userFile = new File(fUserDir, id + FILE_SUFFIX);
		if (!userFile.exists() || !userFile.isFile()) {
			return null;
		}
		try (BufferedReader in = new BufferedReader(new FileReader(userFile))) {
			return new User().fromJson(Json.parse(in));
		} catch (IOException ioe) {
			throw new UserCacheException("Error on loading user \"" + id + "\".", ioe);
		}
	}
	
	private boolean saveUser(User user) {
		if ((user == null) || (user.getId() == null)) {
			return false;
		}
		File userFile = new File(fUserDir, user.getId() + FILE_SUFFIX);
		try (FileWriter out = new FileWriter(userFile)) {
			user.toJson().writeTo(out, WriterConfig.PRETTY_PRINT);
			return true;
		} catch (IOException ioe) {
			throw new UserCacheException("Error on saving user \"" + user.getId() + "\".", ioe);
		}
	}

}
