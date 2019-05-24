package com.balancedbytes.game.ashes.command;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.PoliticalTerm;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdDeclare extends Command {
	
	private static final String OTHER_PLAYER_NR = "otherPlayerNr";
	private static final String OTHER_PLAYER_NAME = "otherPlayerName";
	private static final String POLITICAL_TERM = "politicalTerm";
	
	private int fOtherPlayerNr;
	private String fOtherPlayerName;
	private PoliticalTerm fPoliticalTerm;
	
	public CmdDeclare() {
		super();
	}
	
	public int getOtherPlayerNr() {
		return fOtherPlayerNr;
	}
	
	public void setOtherPlayerNr(int otherPlayerNr) {
		fOtherPlayerNr = otherPlayerNr;
	}
	
	public String getOtherPlayerName() {
		return fOtherPlayerName;
	}
	
	public void setOtherPlayerName(String otherPlayerName) {
		fOtherPlayerName = otherPlayerName;
	}
	
	public PoliticalTerm getPoliticalTerm() {
		return fPoliticalTerm;
	}
	
	public void setPoliticalTerm(PoliticalTerm politicalTerm) {
		fPoliticalTerm = politicalTerm;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.DECLARE;
	}
	
	@Override
	public void validate(Game game, ValidationResult result) {
		if ((game == null) || (result == null)) {
			return;
		}
		if (fOtherPlayerName != null) {
			fOtherPlayerNr = ValidationUtil.findPlayerNr(game, fOtherPlayerName);
			if (fOtherPlayerNr == 0) {
				result.add("Unknown player \"" + fOtherPlayerName + "\".");
				return;
			}
		}
		fOtherPlayerName = null;
		if ((fOtherPlayerNr < 1) || (fOtherPlayerNr > Game.NR_OF_PLAYERS)) {
			result.add("Unknown player \"" + fOtherPlayerNr + "\".");
			return;
		}
		if (fOtherPlayerNr == getPlayerNr()) {
			result.add("You cannot delare politics on yourself.");
		}
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(OTHER_PLAYER_NR, getOtherPlayerNr());
		json.add(OTHER_PLAYER_NAME, getOtherPlayerName());
		json.add(POLITICAL_TERM, AshesUtil.print(getPoliticalTerm()));
		return json.toJsonObject();
	}

	@Override
	public CmdDeclare fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setOtherPlayerNr(json.getInt(OTHER_PLAYER_NR));
		setOtherPlayerName(json.getString(OTHER_PLAYER_NAME));
		String ptString = json.getString(POLITICAL_TERM);
		setPoliticalTerm(AshesUtil.provided(ptString) ? PoliticalTerm.valueOf(ptString) : null);
		return this;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("declare ")
			.append((getPoliticalTerm() != null) ? getPoliticalTerm() : "?")
			.append(" on ").append(getOtherPlayerNr())
			.toString();
	}

}
