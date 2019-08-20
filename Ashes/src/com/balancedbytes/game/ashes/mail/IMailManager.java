package com.balancedbytes.game.ashes.mail;

import java.util.Properties;

public interface IMailManager {
	
	public String getMailFrom();
	
	public void init(Properties properties);
	
	public void processMails();
	
	public void sendMail(Mail mail);

}
