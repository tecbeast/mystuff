package com.balancedbytes.game.ashes.model;

import java.util.Date;

import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.db.IDataObject;

public class PlayerTurn implements IDataObject {
	
	private int fGameNr;
	private int fTurn;
	private int fPlayerNr;
	private Date fDeadline;
	private String fTurnSecret;
	private CommandList fTurnCommands;

	public PlayerTurn() {
		super();
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
	
	public String getTurnSecret() {
		return fTurnSecret;
	}
	
	public void setTurnSecret(String turnSecret) {
		fTurnSecret = turnSecret;
	}
	
	public CommandList getTurnCommands() {
		return fTurnCommands;
	}
	
	public void setTurnCommands(CommandList turnCommands) {
		fTurnCommands = turnCommands;
	}

}
