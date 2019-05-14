package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Player;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CmdTurnsecret extends Command {
	
	private static final String SECRET = "secret";
	
	private String fSecret;
	
	protected CmdTurnsecret() {
		super();
	}
	
	public CmdTurnsecret(int playerNr, String secret) {
		setPlayerNr(playerNr);
		setSecret(secret);
	}
	
	public String getSecret() {
		return fSecret;
	}
	
	protected void setSecret(String secret) {
		fSecret = secret;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.TURNSECRET;
	}
	
	@Override
	public List<String> validate(Game game) {
		List<String> messages = new ArrayList<String>();
		if (game != null) {
			Player player = game.getPlayer(getPlayerNr());
			if ((player != null) && (fSecret != null) && !fSecret.equals(player.getTurnSecret())) {
				messages.add("Invalid Turnsecret.");
			}
		}
		return messages;
	}
	
	@Override
	public JsonObject toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(super.toJson());
		json.add(SECRET, getSecret());
		return json.toJsonObject();
	}

	@Override
	public CmdTurnsecret fromJson(JsonValue jsonValue) {
		super.fromJson(jsonValue);
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		setSecret(json.getString(SECRET));
		return this;
	}

}
