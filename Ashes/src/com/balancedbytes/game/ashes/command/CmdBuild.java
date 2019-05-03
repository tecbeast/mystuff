package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Unit;

public class CmdBuild extends Command {
	
	private int fCount;
	private Unit fUnit;
	private int fPlanetNr;
	
	protected CmdBuild() {
		super();
	}
	
	public CmdBuild(int playerNr, int count, Unit unit, int planetNr) {
		setPlayerNr(playerNr);
		setCount(count);
		setUnit(unit);
		setPlanetNr(planetNr);
	}
	
	public int getCount() {
		return fCount;
	}
	
	protected void setCount(int count) {
		fCount = count;
	}
	
	public Unit getUnit() {
		return fUnit;
	}
	
	protected void setUnit(Unit unit) {
		fUnit = unit;
	}
	
	public int getPlanetNr() {
		return fPlanetNr;
	}
	
	protected void setPlanetNr(int planetNr) {
		fPlanetNr = planetNr;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.BUILD;
	}
	
	@Override
	public List<String> validate(Game game) {
		// TODO: check if player owns planet
		return new ArrayList<String>();
	}
	
	@Override
	public boolean execute(Game game) {
		return false;
	}

}
