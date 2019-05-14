package com.balancedbytes.game.ashes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.WriterConfig;

public class UserCache {
	
	private static final String CHARSET = "UTF-8";
	
	private Map<String, User> fUserById;
	private File fUserFile;
	
	public UserCache() {
		fUserById = new HashMap<String, User>();
	}
	
	public void init(File userFile) {
		if ((userFile == null) || !userFile.exists() || !userFile.isFile()) {
			throw new UserCacheException("Error reading users file.");
		}
		fUserFile = userFile;
		load();
	}

	public User get(String id) {
		return (id != null) ? fUserById.get(id) : null;
	}
	
	private void add(User user) {
		if ((user != null) && AshesUtil.isProvided(user.getId())) {
			fUserById.put(user.getId(), user);
		}
	}
	
	private void load() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fUserFile)), CHARSET))) {
			JsonArray users = Json.parse(in).asArray();
			for (int i = 0; i < users.size(); i++) {
				add(new User().fromJson(users.get(i)));
			}
		} catch (Exception any) {
			throw new UserCacheException("Error reading users file.", any);
		}
	}

	public void save() {
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(fUserFile)), CHARSET))) {
			JsonArray users = new JsonArray();
			for (User user : fUserById.values()) {
				users.add(user.toJson());
			}
			users.writeTo(out, WriterConfig.PRETTY_PRINT);
		} catch (Exception any) {
			throw new UserCacheException("Error writing users file.", any);
		}
	}

}
