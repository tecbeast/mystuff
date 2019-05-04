package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.PoliticalTerm;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdDeclare extends Command {
	
	private static final String OPPONENT_PLAYER_NR = "opponentPlayerNr";
	private static final String POLITICAL_TERM = "politicalTerm";
	
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
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(OPPONENT_PLAYER_NR, getOpponentPlayerNr());
		json.add(POLITICAL_TERM, AshesUtil.toString(getPoliticalTerm()));
		return json.getJsonObject();
	}

	@Override
	public CmdDeclare fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setOpponentPlayerNr(json.getInt(OPPONENT_PLAYER_NR));
		String ptString = json.getString(POLITICAL_TERM);
		setPoliticalTerm(AshesUtil.isProvided(ptString) ? PoliticalTerm.valueOf(ptString) : null);
		return this;
	}

}
