package com.balancedbytes.game.ashes.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

public class GameCache {
	
	private static final String CHARSET = "UTF-8";
	private static final String GAME_PREFIX = "game";
	private static final String EXTENSION = ".json.gz";

	private Map<Integer, Game> fGameByNr;
	private File fGameDir;
	
	public GameCache() {
		fGameByNr = new HashMap<Integer, Game>();
	}
	
	public void init(File gameDir) {
		if ((gameDir == null) || !gameDir.exists() || !gameDir.isDirectory()) {
			throw new AshesException("Error locating game directory.");
		}
		fGameDir = gameDir;
	}
	
	private File createFile(int gameNr) {
		String filename = new StringBuilder()
			.append(GAME_PREFIX)
			.append(AshesUtil.toStringWithLeadingZeroes(gameNr, 6))
			.append(EXTENSION)
			.toString();
		return new File(fGameDir, filename);
	}
	
	public Game get(int gameNr) {
		Game game = fGameByNr.get(gameNr);
		if (game == null) {
			game = load(createFile(gameNr));
			add(game);
		}
		return game;
	}
	
	private void add(Game game) {
		if ((game != null) && (game.getNumber() > 0)) {
			fGameByNr.put(game.getNumber(), game);
		}
	}

	private Game load(File gameFile) {
		try (Reader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(gameFile)), CHARSET))) {
			JsonObject gameObject = Json.parse(in).asObject();
			return new Game().fromJson(gameObject);
		} catch (IOException ioe) {
			throw new AshesException("Error reading users file.", ioe);
		}
	}

	public void save() {
		for (Game game : fGameByNr.values()) {
			save(game);
		}
	}
	
	private void save(Game game) {
		File gameFile = createFile(game.getNumber());
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(gameFile)), CHARSET))) {
			game.toJson().writeTo(out, WriterConfig.PRETTY_PRINT);
		} catch (IOException ioe) {
			throw new AshesException("Error writing game file.", ioe);
		}
	}

}
