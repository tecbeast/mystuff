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

public class MailProcessorGameSubject {
	
	protected MailProcessorGameSubject() {
		super();
	}
	
	// game 1 player 2 turn 3 move
	// game 1 player 2 turn 3 message 5
	public void process(Mail mail) {
		if ((mail == null) || !AshesUtil.isProvided(mail.getSubject())) {
			return;
		}
		try (Scanner subjectScanner = new Scanner(mail.getSubject())) {
			if (!subjectScanner.hasNext() || !subjectScanner.next().equalsIgnoreCase("game")) {
				return;
			}
			int gameNr = subjectScanner.hasNextInt() ? subjectScanner.nextInt() : 0;
			if (gameNr <= 0) {
				return;
			}
			int playerNr = 0;
			if ("player".equalsIgnoreCase(subjectScanner.hasNext() ? subjectScanner.next() : null)) {
				playerNr = subjectScanner.hasNextInt() ? subjectScanner.nextInt() : 0;
			}
			if ((playerNr <= 0) || (playerNr > Game.NR_OF_PLAYERS)) {
				return;
			}
			int turn = 0;
			if ("turn".equalsIgnoreCase(subjectScanner.hasNext() ? subjectScanner.next() : null)) {
				turn = subjectScanner.hasNextInt() ? subjectScanner.nextInt() : 0;
			}
			if (turn <= 0) {
				return;
			}
			Mail result = null;
			String action = subjectScanner.hasNext() ? subjectScanner.next() : null;
			if ("move".equalsIgnoreCase(action)) {
				result = processGameMove(findMove(gameNr, playerNr, turn), mail.getBody());
			}
			if ("message".equalsIgnoreCase(action) && subjectScanner.hasNextInt()) {
				int receiver = subjectScanner.nextInt();
				if ((receiver >= 1) && (receiver <= Game.NR_OF_PLAYERS)) {
					result = processGameMessage(findMove(gameNr, playerNr, turn), receiver, mail.getBody());
				}
			}
			AshesOfEmpire.getInstance().getMailManager().sendMail(result);
		}
	}
	
	private PlayerMove findMove(int gameNr, int playerNr, int turn) {
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
				return createMailMoveRejected(move, pe.getMessage());
			}
		}
		
		if (allCommands != null) {
			CommandList turnSecretCmds = allCommands.extract(CommandType.TURNSECRET);
			if (turnSecretCmds.size() == 0) {
				return createMailMoveRejected(move, "No turnsecret provided.");

			} else {
				CmdTurnsecret turnSecretCmd = (CmdTurnsecret) turnSecretCmds.get(0);
				if (!turnSecretCmd.getSecret().equals(move.getTurnSecret())) {
					return createMailMoveRejected(move, "Invalid turnsecret provided.");
				
				} else { 
					Game game = AshesOfEmpire.getInstance().getGameCache().get(move.getGameNr());
					if ((game == null) || (game.getTurn() != move.getTurn())) {
						return createMailMoveRejected(move, "Unknown game or wrong turn.");
					
					} else {
						move.setCommands(allCommands);
						move.setReceived(new Date());
						move.setModified(true);
						
						ValidationResult validationResult = allCommands.validate(game);
						if (validationResult.isValid()) {
							return createMailMoveAccepted(move);
						} else {
							Mail mail = createMailMoveRejected(move, validationResult.toString());
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
			return createMailMessageRejected(move, "No turnsecret provided.");
		
		} else if (!turnsecret.equals(move.getTurnSecret())) {
			return createMailMessageRejected(move, "Invalid turnsecret provided.");
		
		} else {

			GameCache gameCache = AshesOfEmpire.getInstance().getGameCache();
			UserCache userCache = AshesOfEmpire.getInstance().getUserCache();
		
			Game game = gameCache.get(move.getGameNr());
			User otherUser = userCache.get(game.getPlayer(receiver).getUserName());
			
			return createMailPlayerMessage(move, otherUser, message.toString());
			
		}

	}
	
	private Mail createMailMoveAccepted(PlayerMove move) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject(new StringBuilder()
			.append("Game ").append(move.getGameNr())
			.append(" Player ").append(move.getPlayerNr())
			.append(" Turn ").append(move.getTurn())
			.append(" Move accepted")
			.toString());
		mail.setTo(move.getUser().getEmail());
		StringBuilder body = new StringBuilder();
		body.append(move.getCommands().toString());
		mail.setBody(body.toString());
		return mail;
	}
	
	private Mail createMailMoveRejected(PlayerMove move, String error) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject(new StringBuilder()
			.append("Game ").append(move.getGameNr())
			.append(" Player ").append(move.getPlayerNr())
			.append(" Turn ").append(move.getTurn())
			.append(" Move rejected")
			.toString());
		mail.setTo(move.getUser().getEmail());
		StringBuilder body = new StringBuilder();
		if ((move.getCommands() != null) && (move.getCommands().size() > 0)) {
			body.append(move.getCommands().toString());
			body.append(System.lineSeparator());
		}
		body.append(error);
		mail.setBody(body.toString());
		return mail;
	}

	private Mail createMailMessageRejected(PlayerMove move, String error) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject(new StringBuilder()
			.append("Game ").append(move.getGameNr())
			.append(" Player ").append(move.getPlayerNr())
			.append(" Turn ").append(move.getTurn())
			.append(" Message rejected")
			.toString());
		mail.setTo(move.getUser().getEmail());
		StringBuilder body = new StringBuilder();
		body.append(error);
		mail.setBody(body.toString());
		return mail;
	}

	private Mail createMailPlayerMessage(PlayerMove move, User receiver, String message) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject(new StringBuilder()
			.append("Message from Player ").append(move.getPlayerNr())
			.toString());
		mail.setTo(receiver.getEmail());
		StringBuilder body = new StringBuilder();
		body.append(message);
		mail.setBody(body.toString());
		return mail;
	}

}
