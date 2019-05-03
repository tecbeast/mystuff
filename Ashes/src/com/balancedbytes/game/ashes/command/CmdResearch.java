package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Improvement;

public class CmdResearch extends Command {
	
	private int fCount;
	private Improvement fImprovement;
	private int fPlanetNr;
	
	protected CmdResearch() {
		super();
	}
	
	public CmdResearch(int playerNr, int count, Improvement improvement, int planetNr) {
		setPlayerNr(playerNr);
		setCount(count);
		setImprovement(improvement);
		setPlanetNr(planetNr);
	}
	
	public int getCount() {
		return fCount;
	}
	
	protected void setCount(int count) {
		fCount = count;
	}

	public Improvement getImprovement() {
		return fImprovement;
	}
	
	protected void setImprovement(Improvement improvement) {
		fImprovement = improvement;
	}
	
	public int getPlanetNr() {
		return fPlanetNr;
	}
	
	protected void setPlanetNr(int planetNr) {
		fPlanetNr = planetNr;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.RESEARCH;
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
