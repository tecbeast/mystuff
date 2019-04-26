package com.balancedbytes.game.roborally.model.factory;

import java.util.HashMap;
import java.util.Map;

import com.balancedbytes.game.roborally.json.IJsonReadable;
import com.balancedbytes.game.roborally.json.IJsonWritable;
import com.balancedbytes.game.roborally.model.Coordinate;
import com.balancedbytes.game.roborally.util.StringUtil;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class FactoryBoard implements IJsonReadable, IJsonWritable {

	private String fName;
	private String fAuthor;
	private String fDescription;
	private int fSize;
	private Map<Coordinate, FactoryTile> fTiles;
	
	public FactoryBoard() {
		fTiles = new HashMap<Coordinate, FactoryTile>();
		clear();
	}

	public int getSize() {
		return fSize;
	}
	
	public void setName(String name) {
		fName = name;
	}
	
	public String getName() {
		return fName;
	}
	
	public void setAuthor(String author) {
		fAuthor = author;
	}
	
	public String getAuthor() {
		return fAuthor;
	}
	
	public void setDescription(String description) {
		fDescription = description;
	}
	
	public String getDescription() {
		return fDescription;
	}
	
	public FactoryBoard clear() {
		fTiles.clear();
		fSize = 0;
		return this;
	}
	
	public FactoryBoard add(FactoryTile tile) {
		Coordinate coordinate = (tile != null) ? tile.getCoordinate() : null;
		if (coordinate != null) {
			fTiles.put(coordinate, tile);
			if (Math.max(coordinate.getX(), coordinate.getY()) >= fSize) {
				fSize = Math.max(coordinate.getX(), coordinate.getY()) + 1;
			}
		}
		return this;
	}
	
	public FactoryTile get(Coordinate coordinate) {
		return fTiles.get(coordinate);
	}

	@Override
	// size attribute is not read but calculated
	public FactoryBoard fromJson(JsonValue jsonValue) {
		if ((jsonValue != null) && jsonValue.isObject()) {
			JsonObject jsonObject = jsonValue.asObject();
			setName(jsonObject.getString("name", null));
			setAuthor(jsonObject.getString("author", null));
			setDescription(jsonObject.getString("description", null));
			clear();
			JsonValue tiles = jsonObject.get("tiles");
			if ((tiles != null) && tiles.isArray()) { 
				JsonArray rows = tiles.asArray();
				for (int y = 0; y < rows.size(); y++) {
					if ((rows.get(y) != null) && rows.get(y).isArray()) {
						JsonArray columns = rows.get(y).asArray();
						for (int x = 0; x < columns.size(); x++) {
							FactoryTile tile = new FactoryTile();
							tile.getCoordinate().setY(y).setX(x);
							tile.getElements().fromJson(columns.get(x));
							add(tile);
						}
					}
				}
			}
		}
		return this;
	}
	
	@Override
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		if (StringUtil.isProvided(getName())) {
			jsonObject.add("name", getName());
		}
		if (StringUtil.isProvided(getAuthor())) {
			jsonObject.add("author", getAuthor());
		}
		if (StringUtil.isProvided(getDescription())) {
			jsonObject.add("description", getDescription());
		}
		jsonObject.add("size", getSize());
		JsonArray rows = new JsonArray();
		for (int y = 0; y < fSize; y++) {
			JsonArray columns = new JsonArray();
			for (int x = 0; x < fSize; x++) {
				FactoryTile tile = get(new Coordinate(x, y));
				if (tile != null) { 
					columns.add(tile.getElements().toJson());
				}
			}
			rows.add(columns);
		}
		jsonObject.add("tiles", rows);
		return jsonObject;
	}

}
