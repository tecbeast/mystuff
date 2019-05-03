package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.model.Game;

public class CmdTurntoken extends Command {
	
	private String fTurntoken;
	
	protected CmdTurntoken() {
		super();
	}
	
	public CmdTurntoken(int playerNr, String turntoken) {
		setPlayerNr(playerNr);
		setTurntoken(turntoken);
	}
	
	public String getTurntoken() {
		return fTurntoken;
	}
	
	protected void setTurntoken(String turntoken) {
		fTurntoken = turntoken;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.TURNTOKEN;
	}
	
	@Override
	public List<String> validate(Game game) {
		return new ArrayList<String>();
	}
	
	@Override
	public boolean execute(Game game) {
		return false;
	}

}
