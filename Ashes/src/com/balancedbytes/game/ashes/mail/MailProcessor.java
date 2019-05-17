package com.balancedbytes.game.ashes.mail;

import java.util.Scanner;

import com.balancedbytes.game.ashes.model.Game;

public class MailProcessor {
	
	public MailProcessor() {
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
				
			}
		}
	}
	
	// game 1 player 2 turn 3 move
	// game 1 player 2 turn 3 message 5
	// game 1 player 2 turn 3 message gm
	// game 1 player 2 turn 3 message all
	private boolean handleGameSubject(Mail mail, Scanner scanner) {
		int gameNr = scanner.hasNextInt() ? scanner.nextInt() : 0;
		if (gameNr <= 0) {
			return false;
		}
		int playerNr = 0;
		if ("player".equalsIgnoreCase(scanner.hasNext() ? scanner.next() : null)) {
			playerNr = scanner.hasNextInt() ? scanner.nextInt() : 0;
		}
		if (playerNr <= 0) {
			return false;
		}
		int turnNr = 0;
		if ("turn".equalsIgnoreCase(scanner.hasNext() ? scanner.next() : null)) {
			turnNr = scanner.hasNextInt() ? scanner.nextInt() : 0;
		}
		if (turnNr <= 0) {
			return false;
		}
		String action = scanner.hasNext() ? scanner.next() : null;
		if ("move".equalsIgnoreCase(action)) {
			return processGameMove(null, mail);
		} else if ("message".equalsIgnoreCase(action)) {
			return processGameMessage(null, mail);
		} else {
			return false;
		}
	}
	
	private boolean handleUserSubject(Mail mail, Scanner scanner) {
		return false;
	}
	
	
	private boolean processGameMove(Game game, Mail mail) {
		return true;
	}
	
	private boolean processGameMessage(Game game, Mail mail) {
		return true;
	}

}
