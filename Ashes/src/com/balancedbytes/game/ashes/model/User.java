package com.balancedbytes.game.ashes.model;

import java.util.Date;

import com.balancedbytes.game.ashes.db.IDataObject;

public class User implements IDataObject {
	
	private String fId;
	private String fName;
	private String fEmail;
	private Date fRegistered;
	private Date fLastProcessed;
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
	
	public Date getLastProcessed() {
		return fLastProcessed;
	}
	
	public void setLastProcessed(Date lastProcessed) {
		fLastProcessed = lastProcessed;
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

}
