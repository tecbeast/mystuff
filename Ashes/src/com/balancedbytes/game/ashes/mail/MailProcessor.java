package com.balancedbytes.game.ashes.mail;

import java.util.Scanner;

import com.balancedbytes.game.ashes.AshesUtil;

public class MailProcessor {
	
	protected MailProcessor() {
		super();
	}
	
	public void process(Mail mail) {
		if ((mail == null) || !AshesUtil.provided(mail.getSubject())) {
			return;
		}
		try (Scanner scanner = new Scanner(mail.getSubject().toLowerCase())) {
			while (scanner.hasNext()) {
				String token = scanner.next();
				if (MailProcessorMove.MOVE.contentEquals(token)) {
					new MailProcessorMove().process(mail);
					return;
				}
				if (MailProcessorMessage.MESSAGE.equals(token)) {
					new MailProcessorMessage().process(mail);
					return;
				}
				if (MailProcessorRegister.REGISTER.equals(token)) {
					new MailProcessorRegister().process(mail);
					return;
				}
			}
		}
	}

}
