package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.model.Game;

public class CmdPlayername extends Command {
	
	private String fName;
	
	protected CmdPlayername() {
		super();
	}
	
	public CmdPlayername(int playerNr, String name) {
		setPlayerNr(playerNr);
		setName(name);
	}

	public String getName() {
		return fName;
	}
	
	protected void setName(String name) {
		fName = name;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.PLAYERNAME;
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
