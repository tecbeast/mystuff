package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Unit;

public class CmdSend extends Command {
	
	private int fCount;
	private Unit fUnit;
	private int fOnPlanetNr;
	private int fToPlanetNr;
	
	protected CmdSend() {
		super();
	}
	
	public CmdSend(int playerNr, int count, Unit unit, int toPlanetNr, int onPlanetNr) {
		setPlayerNr(playerNr);
		setCount(count);
		setUnit(unit);
		setToPlanetNr(toPlanetNr);
		setOnPlanetNr(onPlanetNr);
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
	
	public int getOnPlanetNr() {
		return fOnPlanetNr;
	}
	
	protected void setOnPlanetNr(int onPlanetNr) {
		fOnPlanetNr = onPlanetNr;
	}
	
	public int getToPlanetNr() {
		return fToPlanetNr;
	}
	
	
	protected void setToPlanetNr(int toPlanetNr) {
		fToPlanetNr = toPlanetNr;
	}	
		
	@Override
	public CommandType getType() {
		return CommandType.SEND;
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
