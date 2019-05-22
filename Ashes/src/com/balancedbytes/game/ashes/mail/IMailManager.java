package com.balancedbytes.game.ashes.mail;

import java.util.Properties;

public interface IMailManager {
	
	public static final String MAIL_MODE_IMAP = "imap";
	public static final String MAIL_MODE_FILES = "files";
	
	public void init(Properties properties);
	
	public void processMails();
	
	public void sendMail(Mail mail);

}
