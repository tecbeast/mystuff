package com.balancedbytes.game.ashes.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Message {

	private Topic fTopic;
	private List<String> fLines;

	public Message(Topic topic) {
		setTopic(topic);
		fLines = new ArrayList<String>();
	}
	
	public Topic getTopic() {
		return fTopic;
	}
	
	public Message setTopic(Topic topic) {
		fTopic = topic;
		return this;
	}
	
	public int size() {
		return fLines.size();
	}
	
	public Message clear() {
		fLines.clear();
		return this;
	}
	
	public Message add(String line) {
		if (line != null) {
			fLines.add(line);
		}
		return this;
	}
	
	public String get(int index) {
		if ((index < 0) || (index >= size())) {
			return null;
		}
		return fLines.get(index);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String line : fLines) {
			builder.append(line);
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}
  
}