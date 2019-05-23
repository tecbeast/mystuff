package com.balancedbytes.game.ashes.model;

import java.util.Date;

import com.balancedbytes.game.ashes.db.IDataObject;

public class User implements IDataObject {
	
	private long fId;
	private boolean fModified;
	
	private String fUserName;
	private String fRealName;
	private String fEmail;
	private String fSecret;
	private Date fRegistered;
	private Date fLastProcessed;
	private int fGamesJoined;	
	private int fGamesFinished;	
	private int fGamesWon;

	@Override
	public long getId() {
		return fId;
	}
	
	public void setId(long id) {
		fId = id;
	}
	
	@Override
	public boolean isModified() {
		return fModified;
	}
	
	public void setModified(boolean modified) {
		fModified = modified;
	}
	
	public String getUserName() {
		return fUserName;
	}
	
	public void setUserName(String name) {
		fUserName = name;
	}
	
	public String getRealName() {
		return fRealName;
	}
	
	public void setRealName(String realName) {
		fRealName = realName;
	}
	
	public String getEmail() {
		return fEmail;
	}
	
	public void setEmail(String email) {
		fEmail = email;
	}
	
	public String getSecret() {
		return fSecret;
	}
	
	public void setSecret(String secret) {
		fSecret = secret;
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
