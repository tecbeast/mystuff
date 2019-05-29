package com.balancedbytes.game.ashes.command;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdVote extends Command {

	private static final String NR_OF_TURNS = "nrOfTurns";
	
	private int fNrOfTurns;
	
	public CmdVote() {
		super();
	}
	
	public int getNrOfTurns() {
		return fNrOfTurns;
	}
	
	public void setNrOfTurns(int nrOfTurns) {
		fNrOfTurns = nrOfTurns;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.VOTE;
	}
	
	@Override
	public void validate(Game game, ValidationResult result) {
		if ((game == null) || (result == null)) {
			return;
		}
		if ((fNrOfTurns < 10) || (fNrOfTurns > 30)) {
			result.add("You can vote for a number of turns between 10 and 30.");
		}
		if (game.getTurn() > 9) {
			result.add("You cannot vote after the 9th turn.");
		}
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(NR_OF_TURNS, getNrOfTurns());
		return json.toJsonObject();
	}

	@Override
	public CmdVote fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setNrOfTurns(json.getInt(NR_OF_TURNS));
		return this;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("vote ").append(getNrOfTurns())
			.toString();
	}

}
