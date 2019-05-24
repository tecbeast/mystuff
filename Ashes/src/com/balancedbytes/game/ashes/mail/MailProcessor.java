package com.balancedbytes.game.ashes.mail;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;

public class MailProcessor {
	
	protected MailProcessor() {
		super();
	}
	
	public void process(Mail mail) {
		if ((mail == null) || !AshesUtil.isProvided(mail.getSubject())) {
			return;
		}
		Mail result = null;
		String subject = mail.getSubject().toLowerCase();
		if (subject.startsWith("game")) {
			result = new MailProcessorGame().process(mail);
		} 
		if (subject.startsWith("user")) {
			result = new MailProcessorUser().process(mail);
		}
		AshesOfEmpire.getInstance().getMailManager().sendMail(result);
	}

}
