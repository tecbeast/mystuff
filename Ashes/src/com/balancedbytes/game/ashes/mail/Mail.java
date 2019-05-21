package com.balancedbytes.game.ashes.mail;

public class Mail {
	
	private String fSubject;
	private String fBody;
	private String fFrom;
	private String fTo;

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
	
	public String getFrom() {
		return fFrom;
	}
	
	public void setFrom(String from) {
		fFrom = from;
	}
	
	public String getTo() {
		return fTo;
	}
	
	public void setTo(String to) {
		fTo = to;
	}

}
