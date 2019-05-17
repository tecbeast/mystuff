package com.balancedbytes.game.ashes.model;

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

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.AshesUtil;
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
	
	public void init(File userDir, boolean compressed) {
		if ((userDir == null) || !userDir.exists() || !userDir.isDirectory()) {
			throw new AshesException("Error locating user directory.");
		}
		fCompressed = compressed;
		fUserFile = new File(userDir, fCompressed ? "users.json.gz" : "users.json");
		load();
	}

	public User get(String userId) {
		return (userId != null) ? fUserById.get(userId) : null;
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
			throw new AshesException("Error reading users file.", ioe);
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
			throw new AshesException("Error writing users file.", ioe);
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
