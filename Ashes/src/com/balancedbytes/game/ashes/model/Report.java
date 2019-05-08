package com.balancedbytes.game.ashes.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Report {

	private Topic fTopic;
	private List<String> fLines;

	/**
	 * 
	 */
	public Report(Topic topic) {
		setTopic(topic);
		fLines = new ArrayList<String>();
	}
	
	/**
	 * 
	 */
	public Topic getTopic() {
		return fTopic;
	}
	
	/**
	 * 
	 */
	public Report setTopic(Topic topic) {
		fTopic = topic;
		return this;
	}
	
	/**
	 * 
	 */
	public int size() {
		return fLines.size();
	}
	
	/**
	 * 
	 */
	public Report clear() {
		fLines.clear();
		return this;
	}
	
	/**
	 * 
	 */
	public Report add(String line) {
		if (line != null) {
			fLines.add(line);
		}
		return this;
	}
	
	/**
	 * 
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String line : fLines) {
			builder.append(line);
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}
  
}