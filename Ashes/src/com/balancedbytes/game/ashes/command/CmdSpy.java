package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.model.Game;

public class CmdSpy extends Command {
	
	private int fPlanetNr;
	
	protected CmdSpy() {
		super();
	}
	
	public CmdSpy(int playerNr, int planetNr) {
		setPlayerNr(playerNr);
	}

	public int getPlanetNr() {
		return fPlanetNr;
	}
	
	protected void setPlanetNr(int planetNr) {
		fPlanetNr = planetNr;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.SPY;
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
