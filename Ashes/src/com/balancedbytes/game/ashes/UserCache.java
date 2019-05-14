package com.balancedbytes.game.ashes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
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
	private boolean fCompressed;
	
	public UserCache() {
		fUserById = new HashMap<String, User>();
	}
	
	public void init(File userFile, boolean compressed) {
		if ((userFile == null) || !userFile.exists() || !userFile.isFile()) {
			throw new UserCacheException("Error locating user file.");
		}
		fUserFile = userFile;
		fCompressed = compressed;
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
		try (Reader in = createReader()) {
			JsonArray users = Json.parse(in).asArray();
			for (int i = 0; i < users.size(); i++) {
				add(new User().fromJson(users.get(i)));
			}
		} catch (IOException ioe) {
			throw new UserCacheException("Error reading users file.", ioe);
		}
	}
	
	private Reader createReader() throws IOException {
		if (fCompressed) {
			return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fUserFile)), CHARSET));
		} else {
			return new BufferedReader(new FileReader(fUserFile));
		}
	}

	public void save() {
		try (Writer out = createWriter()) {
			JsonArray users = new JsonArray();
			for (User user : fUserById.values()) {
				users.add(user.toJson());
			}
			users.writeTo(out, WriterConfig.PRETTY_PRINT);
		} catch (IOException ioe) {
			throw new UserCacheException("Error writing users file.", ioe);
		}
	}
	
	private Writer createWriter() throws IOException {
		if (fCompressed) {
			return new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(fUserFile)), CHARSET));
		} else {
			return new BufferedWriter(new FileWriter(fUserFile));
		}
	}

}
