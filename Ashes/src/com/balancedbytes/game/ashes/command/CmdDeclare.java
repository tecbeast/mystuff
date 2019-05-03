package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.PoliticalTerm;

public class CmdDeclare extends Command {
	
	private int fOpponentPlayerNr;
	private PoliticalTerm fPoliticalTerm;
	
	protected CmdDeclare() {
		super();
	}
	
	public CmdDeclare(int playerNr, PoliticalTerm politicalTerm, int otherPlayerNr) {
		setPlayerNr(playerNr);
		setPoliticalTerm(politicalTerm);
		setOpponentPlayerNr(otherPlayerNr);
	}
	
	public int getOpponentPlayerNr() {
		return fOpponentPlayerNr;
	}
	
	public void setOpponentPlayerNr(int otherPlayerNr) {
		fOpponentPlayerNr = otherPlayerNr;
	}
	
	public PoliticalTerm getPoliticalTerm() {
		return fPoliticalTerm;
	}
	
	protected void setPoliticalTerm(PoliticalTerm politicalTerm) {
		fPoliticalTerm = politicalTerm;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.DECLARE;
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
