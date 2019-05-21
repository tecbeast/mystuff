package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValidationResult implements Iterable<String> {
	
	private int fLineNr;
	private List<String> fMessages;
	
	public ValidationResult() {
		fMessages = new ArrayList<String>();
	}
	
	public int size() {
		return fMessages.size();
	}

	public boolean isValid() {
		return (size() == 0);
	}	

	public ValidationResult clear() {
		fMessages.clear();
		return this;
	}
	
	public int getLineNr() {
		return fLineNr;
	}
	
	public ValidationResult setLineNr(int lineNr) {
		fLineNr = lineNr;
		return this;
	}

	public ValidationResult add(String message) {
		if (message == null) {
			return this;
		}
		if (fLineNr > 0) {
			StringBuilder line = new StringBuilder();
			line.append("line ").append(fLineNr).append(": ");
			line.append(message);
			fMessages.add(line.toString());
		} else {
			fMessages.add(message);
		}
		return this;
	}
	
	@Override
	public Iterator<String> iterator() {
		return fMessages.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (String message : fMessages) {
			result.append(message).append(System.lineSeparator());
		}
		return result.toString();
	}

}
