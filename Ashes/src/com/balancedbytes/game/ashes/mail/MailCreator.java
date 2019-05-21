package com.balancedbytes.game.ashes.mail;

import com.balancedbytes.game.ashes.model.PlayerMove;

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

}
