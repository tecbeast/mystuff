package com.balancedbytes.game.ashes.model;

import java.util.Date;

import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.db.IDataObject;

public class PlayerMove implements IDataObject {
	
	private long fId;
	private int fGameNr;
	private int fTurn;
	private int fPlayerNr;
	private Date fDeadline;
	private Date fReceived;
	private String fTurnSecret;
	private transient CommandList fCommands;	

	public PlayerMove() {
		super();
	}
	
	public long getId() {
		return fId;
	}
	
	public void setId(long id) {
		fId = id;
	}
	
	public int getGameNr() {
		return fGameNr;
	}
	
	public void setGameNr(int gameNr) {
		fGameNr = gameNr;
	}
	
	public int getTurn() {
		return fTurn;
	}
	
	public void setTurn(int turn) {
		fTurn = turn;
	}
	
	public int getPlayerNr() {
		return fPlayerNr;
	}
	
	public void setPlayerNr(int playerNr) {
		fPlayerNr = playerNr;
	}
	
	public Date getDeadline() {
		return fDeadline;
	}
	
	public void setDeadline(Date deadline) {
		fDeadline = deadline;
	}
	
	public Date getReceived() {
		return fReceived;
	}
	
	public void setReceived(Date received) {
		fReceived = received;
	}
	
	public String getTurnSecret() {
		return fTurnSecret;
	}
	
	public void setTurnSecret(String turnSecret) {
		fTurnSecret = turnSecret;
	}
	
	public CommandList getCommands() {
		return fCommands;
	}
	
	public void setCommands(CommandList commands) {
		fCommands = commands;
	}
	
}
