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
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

public class GameCache {
	
	private static final String CHARSET = "UTF-8";

	private Map<Integer, Game> fGameByNr;
	private File fGameDir;
	private boolean fCompressed;
	
	public GameCache() {
		fGameByNr = new HashMap<Integer, Game>();
	}
	
	public void init(File gameDir, boolean compressed) {
		if ((gameDir == null) || !gameDir.exists() || !gameDir.isDirectory()) {
			throw new AshesException("Error locating game directory.");
		}
		fGameDir = gameDir;
		fCompressed = compressed;
	}
	
	private File createFile(int gameNr) {
		StringBuilder filename = new StringBuilder();
		filename.append("game");
		String numberString = Integer.toString(gameNr);
		if (numberString.length() <= 4) {
			filename.append("0000".substring(0, 5 - numberString.length()));
		}
		filename.append(numberString);
		filename.append(".json");
		if (fCompressed) {
			filename.append(".gz");
		}
		return new File(fGameDir, filename.toString());
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
		try (Reader in = createReader(gameFile)) {
			JsonObject gameObject = Json.parse(in).asObject();
			return new Game().fromJson(gameObject);
		} catch (IOException ioe) {
			throw new AshesException("Error reading users file.", ioe);
		}
	}
	
	private Reader createReader(File gameFile) throws IOException {
		if (fCompressed) {
			return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(gameFile)), CHARSET));
		} else {
			return new BufferedReader(new FileReader(gameFile));
		}
	}

	public void save() {
		for (Game game : fGameByNr.values()) {
			save(game);
		}
	}
	
	private void save(Game game) {
		try (Writer out = createWriter(createFile(game.getNumber()))) {
			game.toJson().writeTo(out, WriterConfig.PRETTY_PRINT);
		} catch (IOException ioe) {
			throw new AshesException("Error writing game file.", ioe);
		}
	}
	
	private Writer createWriter(File gameFile) throws IOException {
		if (fCompressed) {
			return new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(gameFile)), CHARSET));
		} else {
			return new BufferedWriter(new FileWriter(gameFile));
		}
	}

}
