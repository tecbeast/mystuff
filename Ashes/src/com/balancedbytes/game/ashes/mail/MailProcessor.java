package com.balancedbytes.game.ashes.mail;

import java.io.StringReader;
import java.util.Date;
import java.util.Scanner;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.command.CmdTurnsecret;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.command.CommandType;
import com.balancedbytes.game.ashes.command.ValidationResult;
import com.balancedbytes.game.ashes.model.Game;
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
		if (scanner.hasNext()) {
			String token = scanner.next();
			if ("game".equalsIgnoreCase(token)) {
				handleGameSubject(mail, scanner);
			} else if ("user".equalsIgnoreCase(token)) {
				handleUserSubject(mail, scanner);
			} else {
				// TODO ...
			}
		}
	}
	
	// game 1 player 2 turn 3 move
	// game 1 player 2 turn 3 message 5
	// game 1 player 2 turn 3 message gm
	// game 1 player 2 turn 3 message all
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
		String action = scanner.hasNext() ? scanner.next() : null;
		if ("move".equalsIgnoreCase(action)) {
			processGameMove(gameNr, playerNr, turn, mail.getBody());
		} else if ("message".equalsIgnoreCase(action)) {
			String receiver = scanner.hasNext() ? scanner.next() : null;		
			processGameMessage(gameNr, playerNr, turn, receiver, mail.getBody());
		} else {
			// TODO ...
		}
	}
	
	private void handleUserSubject(Mail mail, Scanner scanner) {
		// TODO ...
	}
	
	private void processGameMove(int gameNr, int playerNr, int turn, String mailBody) {
		
		PlayerMoveCache moveCache = AshesOfEmpire.getInstance().getMoveCache();
		PlayerMove move = moveCache.get(gameNr, playerNr, turn);
		
		UserCache userCache = AshesOfEmpire.getInstance().getUserCache();
		User user = userCache.get(move.getUserName());
		move.setUser(user);
		user.setLastProcessed(new Date());
		user.setModified(true);
		
		Mail moveReceipt = null;
		CommandList allCommands = null;
		Parser parser = new Parser();
		try (StringReader in = new StringReader(mailBody)) {
			try {
				allCommands = parser.parse(in, move.getPlayerNr());
			} catch (ParserException pe) {
				moveReceipt = MailCreator.createMoveRejected(move, pe.getMessage());
			}
		}
		
		if (allCommands != null) {
			CommandList turnSecretCmds = allCommands.extract(CommandType.TURNSECRET);
			if (turnSecretCmds.size() == 0) {
				moveReceipt = MailCreator.createMoveRejected(move, "No turnsecret provided.");

			} else {
				CmdTurnsecret turnSecretCmd = (CmdTurnsecret) turnSecretCmds.get(0);
				if (!turnSecretCmd.getSecret().equals(move.getTurnSecret())) {
					moveReceipt = MailCreator.createMoveRejected(move, "Invalid turnsecret provided.");
				
				} else { 
					Game game = AshesOfEmpire.getInstance().getGameCache().get(move.getGameNr());
					if ((game == null) || (game.getTurn() != move.getTurn())) {
						moveReceipt = MailCreator.createMoveRejected(move, "Unknown game or wrong turn.");
					
					} else {
						move.setCommands(allCommands);
						move.setReceived(new Date());
						move.setModified(true);
						
						ValidationResult validationResult = allCommands.validate(game);
						if (validationResult.isValid()) {
							moveReceipt = MailCreator.createMoveAccepted(move);
						} else {
							moveReceipt = MailCreator.createMoveRejected(move, validationResult.toString());
							move.setCommands(null);
						}
					}
				}
			}
		}

		AshesOfEmpire.getInstance().getMailManager().sendMail(moveReceipt);

	}

	private void processGameMessage(int gameNr, int playerNr, int turn, String receiver, String mailBody) {
		// TODO ...
	}

}
