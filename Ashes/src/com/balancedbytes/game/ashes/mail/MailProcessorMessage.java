package com.balancedbytes.game.ashes.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.GameCache;
import com.balancedbytes.game.ashes.model.Move;
import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;

public class MailProcessorMessage {
	
	public static final String MESSAGE = "message";
	
	private static final String TO = "to";	
	private static final String GAME = "game";
	private static final String TURN = "turn";
	private static final String PLAYER = "player";
	
	protected MailProcessorMessage() {
		super();
	}
	
	// message to 5 game 1 turn 3 player 2
	public void process(Mail mail) {
		
		if ((mail == null) || !AshesUtil.provided(mail.getSubject())) {
			return;
		}
		
		Map<String, String> tokenMap = MailProcessorUtil.buildTokenMap(
			new String[] { MESSAGE, GAME, TURN, PLAYER, TO }, mail.getSubject()
		);
		
		if (!tokenMap.containsKey(MESSAGE)) {
			return;
		}
		int gameNr = AshesUtil.isNumeric(tokenMap.get(GAME)) ? Integer.parseInt(tokenMap.get(GAME)) : 0;
		if (gameNr < 1) {
			return;
		}
		int turn = AshesUtil.isNumeric(tokenMap.get(TURN)) ? Integer.parseInt(tokenMap.get(TURN)) : 0;
		if (turn < 1) {
			return;
		}
		int playerNr = AshesUtil.isNumeric(tokenMap.get(PLAYER)) ? Integer.parseInt(tokenMap.get(PLAYER)) : 0;
		if ((playerNr < 1) || (playerNr > Game.NR_OF_PLAYERS)) {
			return;
		}
		int toNr = AshesUtil.isNumeric(tokenMap.get(TO)) ? Integer.parseInt(tokenMap.get(TO)) : 0;
		if ((toNr < 1) || (toNr > Game.NR_OF_PLAYERS)) {
			return;
		}
		
		IMailManager mailManager = AshesOfEmpire.getInstance().getMailManager();
		Move move = MailProcessorUtil.findMove(gameNr, playerNr, turn);
		mailManager.sendMail(processMessage(move, toNr, mail.getBody()));
		
	}
		
	private Mail processMessage(Move move, int receiver, String mailBody) {
		
		if ((move == null) || !AshesUtil.provided(mailBody)) {
			return null;
		}		
		
		String turnsecret = null;
		StringBuilder message = new StringBuilder();
		try (BufferedReader in = new BufferedReader(new StringReader(mailBody))) {
			String line = null;
			while ((line = in.readLine()) != null) {
				if (turnsecret == null) {
					try (Scanner scanner = new Scanner(line)) {
						String token = scanner.hasNext() ? scanner.next() : null;
						if ("turnsecret".equalsIgnoreCase(token)) {
							turnsecret = scanner.hasNext() ? scanner.next() : null;
							continue;
						}
					}
				}
				message.append(line);
				message.append(System.lineSeparator());
			}
		} catch (IOException ioe) {
			return null;
		}
		
		if (turnsecret == null) {
			return createMailMessageRejected(move, "ERROR: No turnsecret provided.");
		
		} else if (!turnsecret.equals(move.getTurnSecret())) {
			return createMailMessageRejected(move, "ERROR: Invalid turnsecret provided.");
		
		} else {

			GameCache gameCache = AshesOfEmpire.getInstance().getGameCache();
			UserCache userCache = AshesOfEmpire.getInstance().getUserCache();
		
			Game game = gameCache.get(move.getGameNr());
			User otherUser = userCache.get(game.getPlayer(receiver).getUserName());
			
			return createMailPlayerMessage(move, otherUser, message.toString());
			
		}

	}

	private Mail createMailMessageRejected(Move move, String error) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getMailFrom());
		mail.setSubject(new StringBuilder()
			.append("Game ").append(move.getGameNr())
			.append(" Player ").append(move.getPlayerNr())
			.append(" Turn ").append(move.getTurn())
			.append(" Message rejected.")
			.toString());
		mail.setTo(move.getUser().getEmail());
		StringBuilder body = new StringBuilder();
		body.append(error);
		mail.setBody(body.toString());
		return mail;
	}

	private Mail createMailPlayerMessage(Move move, User receiver, String message) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getMailFrom());
		mail.setSubject(new StringBuilder()
			.append("Message from Player ").append(move.getPlayerNr()).append(".")
			.toString());
		mail.setTo(receiver.getEmail());
		StringBuilder body = new StringBuilder();
		body.append(message);
		mail.setBody(body.toString());
		return mail;
	}

}
