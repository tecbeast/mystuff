package com.balancedbytes.game.ashes.mail;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.model.Move;
import com.balancedbytes.game.ashes.model.MoveCache;
import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;

public class MailProcessorUtil {
	
	private static final String TOKEN_REG_EX = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";

	/**
	 * 
	 */
	public static Map<String, String> buildTokenMap(String[] tokens, String text) {
		Map<String, String> tokenMap = new HashMap<String, String>();
		if (!AshesUtil.provided(text) || !AshesUtil.provided(tokens)) {
			return tokenMap;
		}
		Set<String> tokenSet = asSet(tokens);
		String valueToken = null;
		try (Scanner scanner = new Scanner(text)) {
			while (scanner.hasNextLine()) {
				String token = scanner.findInLine(TOKEN_REG_EX);
				if (token == null) {
					scanner.nextLine();
					continue;
				}
				String tokenLc = token.toLowerCase();
				if (tokenSet.contains(tokenLc)) {
					tokenMap.put(tokenLc, "");
					valueToken = tokenLc;
				} else {
					if (valueToken != null) {
						tokenMap.put(valueToken, removeQuotes(token));
						valueToken = null;
					}
				}
			}
		}
		return tokenMap;
	}
	
	/**
	 * 
	 */
	public static Move findMove(int gameNr, int playerNr, int turn) {
		MoveCache moveCache = AshesOfEmpire.getInstance().getMoveCache();
		Move move = moveCache.get(gameNr, playerNr, turn);
		if (move == null) {
			return null;
		}
		UserCache userCache = AshesOfEmpire.getInstance().getUserCache();
		User user = userCache.get(move.getUserName());
		if (user == null) {
			return null;
		}
		move.setUser(user);
		user.setLastProcessed(new Date());
		user.setModified(true);
		return move;
	}
	
	private static String removeQuotes(String str) {
		if ((str != null) && (str.length() >= 2) && str.startsWith("\"") && (str.endsWith("\""))) {
			return str.substring(1, str.length() - 1);
		}
		return str;
	}
	
	private static Set<String> asSet(String[] strArray) {
		Set<String> result = new HashSet<String>();
		if (AshesUtil.provided(strArray)) {
			for (String str : strArray) {
				result.add(str);
			}
		}
		return result;
	}

}
