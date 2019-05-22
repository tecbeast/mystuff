package com.balancedbytes.game.ashes.mail;

import com.balancedbytes.game.ashes.model.PlayerMove;
import com.balancedbytes.game.ashes.model.User;

public class MailCreator {
	
	public static Mail createMoveAccepted(PlayerMove move) {
		Mail mail = new Mail();
		mail.setSubject(new StringBuilder()
			.append("Game ").append(move.getGameNr())
			.append(" Player ").append(move.getPlayerNr())
			.append(" Turn ").append(move.getTurn())
			.append(" Move accepted")
			.toString());
		mail.setTo(move.getUser().getEmail());
		StringBuilder body = new StringBuilder();
		body.append(System.lineSeparator());
		body.append(move.getCommands().toString());
		mail.setBody(body.toString());
		return mail;
	}
	
	public static Mail createMoveRejected(PlayerMove move, String error) {
		Mail mail = new Mail();
		mail.setSubject(new StringBuilder()
			.append("Game ").append(move.getGameNr())
			.append(" Player ").append(move.getPlayerNr())
			.append(" Turn ").append(move.getTurn())
			.append(" Move rejected")
			.toString());
		mail.setTo(move.getUser().getEmail());
		StringBuilder body = new StringBuilder();
		body.append(System.lineSeparator());
		if ((move.getCommands() != null) && (move.getCommands().size() > 0)) {
			body.append(move.getCommands().toString());
			body.append(System.lineSeparator());
		}
		body.append(error);
		mail.setBody(body.toString());
		return mail;
	}

	public static Mail createMessageRejected(PlayerMove move, String error) {
		Mail mail = new Mail();
		mail.setSubject(new StringBuilder()
			.append("Game ").append(move.getGameNr())
			.append(" Player ").append(move.getPlayerNr())
			.append(" Turn ").append(move.getTurn())
			.append(" Message rejected")
			.toString());
		mail.setTo(move.getUser().getEmail());
		StringBuilder body = new StringBuilder();
		body.append(System.lineSeparator());
		body.append(error);
		mail.setBody(body.toString());
		return mail;
	}

	public static Mail createPlayerMessage(PlayerMove move, User receiver, String message) {
		Mail mail = new Mail();
		mail.setSubject(new StringBuilder()
			.append("Message from Player ").append(move.getPlayerNr())
			.toString());
		mail.setTo(receiver.getEmail());
		StringBuilder body = new StringBuilder();
		body.append(System.lineSeparator());
		body.append(message);
		mail.setBody(body.toString());
		return mail;
	}

}
