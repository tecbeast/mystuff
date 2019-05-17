package com.balancedbytes.game.ashes.mail;

public class Mail {
	
	private String fSubject;
	private String fBody;

	public Mail(String subject, String body) {
		fSubject = subject;
		fBody = body;
	}
	
	public String getSubject() {
		return fSubject;
	}

	public String getBody() {
		return fBody;
	}

}
