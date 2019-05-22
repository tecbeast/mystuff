package com.balancedbytes.game.ashes.mail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Mail {
	
	private static final String SUBJECT_PREFIX = "Subject: ";
	private static final String FROM_PREFIX = "From: ";
	private static final String TO_PREFIX = "To: ";
	
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
	
	public void writeTo(Writer writer) throws IOException {
		if (writer == null) {
			return;
		}
		try (BufferedWriter out = new BufferedWriter(writer)) {
			out.write(SUBJECT_PREFIX);
			if (getSubject() != null) {
				out.write(getSubject());
			}
			out.newLine();
			out.write(FROM_PREFIX);
			if (getFrom() != null) {
				out.write(getFrom());
			}
			out.newLine();
			out.write(TO_PREFIX);
			if (getTo() != null) {
				out.write(getTo());
			}
			out.newLine();
			out.newLine();
			if (getBody() != null) {
				out.write(getBody());
			}
		}
	}
	
	public Mail readFrom(Reader reader) throws IOException {
		if (reader == null) {
			return null;
		}
		try (BufferedReader in = new BufferedReader(reader)) {
			String line = in.readLine();
			if ((line != null) && (line.length() > SUBJECT_PREFIX.length())) {
				setSubject(line.substring(SUBJECT_PREFIX.length()));
			}
			line = in.readLine();
			if ((line != null) && (line.length() > FROM_PREFIX.length())) {
				setFrom(line.substring(FROM_PREFIX.length()));
			}
			line = in.readLine();
			if ((line != null) && (line.length() > TO_PREFIX.length())) {
				setTo(line.substring(TO_PREFIX.length()));
			}
			StringBuilder body = new StringBuilder();
			line = in.readLine();
			if (line != null) {
				while ((line = in.readLine()) != null) {
					body.append(line).append(System.lineSeparator());
				}
			}
			setBody(body.toString());
		}
		return this;
	}

}
