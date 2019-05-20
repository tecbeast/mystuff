package com.balancedbytes.game.ashes.mail;

import javax.mail.Address;

public class Mail {
	
	private String fSubject;
	private String fBody;
	private Address fFrom;

	public Mail() {
		super();
	}
	
	public String getSubject() {
		return fSubject;
	}
	
	public void setSubject(String subject) {
		fSubject = subject;
	}

	public String getBody() {
		return fBody;
	}
	
	public void setBody(String body) {
		fBody = body;
	}
	
	public Address getFrom() {
		return fFrom;
	}
	
	public void setFrom(Address from) {
		fFrom = from;
	}

}
