package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.model.Game;

public class CmdPlanetname extends Command {
	
	private int fPlanetNr;
	private String fName;
	
	protected CmdPlanetname() {
		super();
	}
	
	public CmdPlanetname(int playerNr, String name, int planetNr) {
		setPlayerNr(playerNr);
		setName(name);
		setPlanetNr(planetNr);
	}

	public int getPlanetNr() {
		return fPlanetNr;
	}
	
	protected void setPlanetNr(int planetNr) {
		fPlanetNr = planetNr;
	}
	
	public String getName() {
		return fName;
	}
	
	protected void setName(String name) {
		fName = name;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.PLANETNAME;
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
