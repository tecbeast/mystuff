package com.balancedbytes.game.ashes.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Scanner;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.command.CmdTurnsecret;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.command.CommandType;
import com.balancedbytes.game.ashes.command.ValidationResult;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.GameCache;
import com.balancedbytes.game.ashes.model.PlayerMove;
import com.balancedbytes.game.ashes.model.PlayerMoveCache;
import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;
import com.balancedbytes.game.ashes.parser.Parser;
import com.balancedbytes.game.ashes.parser.ParserException;

public class MailProcessor {
	
	protected MailProcessor() {
		super();
	}
	
	public void process(Mail mail) {
		Scanner scanner = new Scanner(mail.getSubject());
		String token = scanner.hasNext() ? scanner.next() : null;
		if ("game".equalsIgnoreCase(token)) {
			handleGameSubject(mail, scanner);
		} 
		if ("user".equalsIgnoreCase(token)) {
			handleUserSubject(mail, scanner);
		}
	}
	
	// game 1 player 2 turn 3 move
	// game 1 player 2 turn 3 message 5
	private void handleGameSubject(Mail mail, Scanner scanner) {
		int gameNr = scanner.hasNextInt() ? scanner.nextInt() : 0;
		if (gameNr <= 0) {
			return;
		}
		int playerNr = 0;
		if ("player".equalsIgnoreCase(scanner.hasNext() ? scanner.next() : null)) {
			playerNr = scanner.hasNextInt() ? scanner.nextInt() : 0;
		}
		if (playerNr <= 0) {
			return;
		}
		int turn = 0;
		if ("turn".equalsIgnoreCase(scanner.hasNext() ? scanner.next() : null)) {
			turn = scanner.hasNextInt() ? scanner.nextInt() : 0;
		}
		if (turn <= 0) {
			return;
		}
		Mail result = null;
		String action = scanner.hasNext() ? scanner.next() : null;
		if ("move".equalsIgnoreCase(action)) {
			result = processGameMove(createMove(gameNr, playerNr, turn), mail.getBody());
		}
		if ("message".equalsIgnoreCase(action) && scanner.hasNextInt()) {
			int receiver = scanner.nextInt();
			if ((receiver >= 1) && (receiver <= Game.NR_OF_PLAYERS)) {
				result = processGameMessage(createMove(gameNr, playerNr, turn), receiver, mail.getBody());
			}
		}
		AshesOfEmpire.getInstance().getMailManager().sendMail(result);
	}
	
	private PlayerMove createMove(int gameNr, int playerNr, int turn) {
		PlayerMoveCache moveCache = AshesOfEmpire.getInstance().getMoveCache();
		PlayerMove move = moveCache.get(gameNr, playerNr, turn);
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
	
	private Mail processGameMove(PlayerMove move, String mailBody) {
		
		if ((move == null) || !AshesUtil.isProvided(mailBody)) {
			return null;
		}		
		
		CommandList allCommands = null;
		Parser parser = new Parser();
		try (StringReader in = new StringReader(mailBody)) {
			try {
				allCommands = parser.parse(in, move.getPlayerNr());
			} catch (ParserException pe) {
				return MailCreator.createMoveRejected(move, pe.getMessage());
			}
		}
		
		if (allCommands != null) {
			CommandList turnSecretCmds = allCommands.extract(CommandType.TURNSECRET);
			if (turnSecretCmds.size() == 0) {
				return MailCreator.createMoveRejected(move, "No turnsecret provided.");

			} else {
				CmdTurnsecret turnSecretCmd = (CmdTurnsecret) turnSecretCmds.get(0);
				if (!turnSecretCmd.getSecret().equals(move.getTurnSecret())) {
					return MailCreator.createMoveRejected(move, "Invalid turnsecret provided.");
				
				} else { 
					Game game = AshesOfEmpire.getInstance().getGameCache().get(move.getGameNr());
					if ((game == null) || (game.getTurn() != move.getTurn())) {
						return MailCreator.createMoveRejected(move, "Unknown game or wrong turn.");
					
					} else {
						move.setCommands(allCommands);
						move.setReceived(new Date());
						move.setModified(true);
						
						ValidationResult validationResult = allCommands.validate(game);
						if (validationResult.isValid()) {
							return MailCreator.createMoveAccepted(move);
						} else {
							Mail mail = MailCreator.createMoveRejected(move, validationResult.toString());
							move.setCommands(null);
							return mail;
						}
					}
				}
			}
		}
		
		return null;

	}

	private Mail processGameMessage(PlayerMove move, int receiver, String mailBody) {
		
		if ((move == null) || !AshesUtil.isProvided(mailBody)) {
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
			return MailCreator.createMoveRejected(move, "No turnsecret provided.");
		
		} else if (!turnsecret.equals(move.getTurnSecret())) {
			return MailCreator.createMoveRejected(move, "Invalid turnsecret provided.");
		
		} else {

			GameCache gameCache = AshesOfEmpire.getInstance().getGameCache();
			UserCache userCache = AshesOfEmpire.getInstance().getUserCache();
		
			Game game = gameCache.get(move.getGameNr());
			User otherUser = userCache.get(game.getPlayer(receiver).getUserName());
			
			return MailCreator.createPlayerMessage(move, otherUser, message.toString());
			
		}

	}
	
	private void handleUserSubject(Mail mail, Scanner scanner) {
		// TODO ...
	}

}
