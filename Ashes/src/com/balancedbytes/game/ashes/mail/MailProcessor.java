package com.balancedbytes.game.ashes.mail;

import com.balancedbytes.game.ashes.AshesUtil;

public class MailProcessor {
	
	protected MailProcessor() {
		super();
	}
	
	public void process(Mail mail) {
		if ((mail == null) || !AshesUtil.isProvided(mail.getSubject())) {
			return;
		}
		String subject = mail.getSubject().toLowerCase();
		if (subject.startsWith("game")) {
			new MailProcessorGameSubject().process(mail);
		} 
		if (subject.startsWith("user")) {
			new MailProcessorUserSubject().process(mail);
		}
	}

}
