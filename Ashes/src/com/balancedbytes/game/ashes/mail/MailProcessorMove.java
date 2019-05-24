package com.balancedbytes.game.ashes.mail;

import java.io.StringReader;
import java.util.Date;
import java.util.Map;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.command.CmdTurnsecret;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.command.CommandType;
import com.balancedbytes.game.ashes.command.ValidationResult;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.PlayerMove;
import com.balancedbytes.game.ashes.parser.Parser;
import com.balancedbytes.game.ashes.parser.ParserException;

public class MailProcessorMove {
	
	public static final String MOVE = "move";
	
	private static final String GAME = "game";
	private static final String TURN = "turn";
	private static final String PLAYER = "player";
	
	protected MailProcessorMove() {
		super();
	}
	
	// move game 1 turn 3 player 2
	public void process(Mail mail) {
		
		if ((mail == null) || !AshesUtil.provided(mail.getSubject())) {
			return;
		}
		
		Map<String, String> tokenMap = MailProcessorUtil.buildTokenMap(
			MOVE, new String[] { GAME, TURN, PLAYER }, mail.getSubject()
		);
		
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
		
		IMailManager mailManager = AshesOfEmpire.getInstance().getMailManager();
		PlayerMove move = MailProcessorUtil.findMove(gameNr, playerNr, turn);
		mailManager.sendMail(processMove(move, mail.getBody()));

	}
	
	private Mail processMove(PlayerMove move, String mailBody) {
		
		if ((move == null) || !AshesUtil.provided(mailBody)) {
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
				return createMailMoveRejected(move, "ERROR: No turnsecret provided.");

			} else {
				CmdTurnsecret turnSecretCmd = (CmdTurnsecret) turnSecretCmds.get(0);
				if (!turnSecretCmd.getSecret().equals(move.getTurnSecret())) {
					return createMailMoveRejected(move, "ERROR: Invalid turnsecret provided.");
				
				} else { 
					Game game = AshesOfEmpire.getInstance().getGameCache().get(move.getGameNr());
					if ((game == null) || (game.getTurn() != move.getTurn())) {
						return createMailMoveRejected(move, "ERROR: Unknown game or wrong turn.");
					
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

	private Mail createMailMoveAccepted(PlayerMove move) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject(new StringBuilder()
			.append("Game ").append(move.getGameNr())
			.append(" Player ").append(move.getPlayerNr())
			.append(" Turn ").append(move.getTurn())
			.append(" Move accepted.")
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
			.append(" Move rejected.")
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

}
