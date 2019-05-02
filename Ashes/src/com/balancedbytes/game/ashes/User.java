package com.balancedbytes.game.ashes;

import java.util.Date;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class User implements IJsonSerializable {
	
	private static final String ID = "id"; 
	private static final String NAME = "name"; 
	private static final String EMAIL = "email"; 
	private static final String REGISTERED = "registered"; 
	private static final String GAMES_JOINED = "gamesJoined"; 
	private static final String GAMES_FINISHED = "gamesFinished"; 
	private static final String GAMES_WON = "gamesWon"; 
	
	private String fId;
	private String fName;
	private String fEmail;
	private Date fRegistered;	
	private int fGamesJoined;	
	private int fGamesFinished;	
	private int fGamesWon;

	public String getId() {
		return fId;
	}
	
	public void setId(String id) {
		fId = id;
	}
	
	public String getName() {
		return fName;
	}
	
	public void setName(String name) {
		fName = name;
	}
	
	public String getEmail() {
		return fEmail;
	}
	
	public void setEmail(String email) {
		fEmail = email;
	}
	
	public Date getRegistered() {
		return fRegistered;
	}
	
	public void setRegistered(Date registered) {
		fRegistered = registered;
	}
	
	public int getGamesJoined() {
		return fGamesJoined;
	}
	
	public void setGamesJoined(int gamesJoined) {
		fGamesJoined = gamesJoined;
	}
	
	public int getGamesFinished() {
		return fGamesFinished;
	}
	
	public void setGamesFinished(int gamesFinished) {
		fGamesFinished = gamesFinished;
	}
	
	public int getGamesWon() {
		return fGamesWon;
	}
	
	public void setGamesWon(int gamesWon) {
		fGamesWon = gamesWon;
	}
	
	@Override
	public JsonValue toJson() {
		JsonWrapper json = new JsonWrapper(new JsonObject());
		json.add(ID,  getId());
		json.add(NAME, getName());
		json.add(EMAIL, getEmail());
		json.add(REGISTERED, getRegistered());
		json.add(GAMES_JOINED, getGamesJoined());
		json.add(GAMES_FINISHED, getGamesFinished());
		json.add(GAMES_WON, getGamesWon());
		return json.getJsonObject();
	}
	
	@Override
	public User fromJson(JsonValue jsonValue) {
		JsonWrapper json = new JsonWrapper(jsonValue.asObject());
		setId(json.getString(ID));
		setName(json.getString(NAME));
		setEmail(json.getString(EMAIL));
		setRegistered(json.getDate(REGISTERED));
		setGamesJoined(json.getInt(GAMES_JOINED));
		setGamesFinished(json.getInt(GAMES_FINISHED));
		setGamesWon(json.getInt(GAMES_WON));
		return this;
	}	

}
