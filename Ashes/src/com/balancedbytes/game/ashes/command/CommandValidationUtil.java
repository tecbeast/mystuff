package com.balancedbytes.game.ashes.command;

import java.util.List;

import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Planet;
import com.balancedbytes.game.ashes.model.Player;

public final class CommandValidationUtil {

	public static int findPlayerNr(Game game, String playerName, List<String> messages) {
		if ((game == null) || (playerName == null)) {
			return 0;
		}
		for (int i = 1; i <= 8; i++) {
			Player player = game.getPlayer(i);
			if ((player != null) && (player.getName() != null) && player.getName().equalsIgnoreCase(playerName)) {
				return i;
			}
		}
		if (messages != null) {
			messages.add("Unknown player " + playerName);
		}
		return 0;
	}
	
	public static int findPlanetNr(Game game, String planetName, List<String> messages) {
		if ((game == null) || (planetName == null)) {
			return 0;
		}
		for (int i = 1; i <= 40; i++) {
			Planet planet = game.getPlanet(i);
			if ((planet != null) && (planet.getName() != null) && planet.getName().equalsIgnoreCase(planetName)) {
				return i;
			}
		}
		if (messages != null) {
			messages.add("Unknown planet " + planetName);
		}
		return 0;
	}
	
}
