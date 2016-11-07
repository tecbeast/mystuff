package com.balancedbytes.mystuff.games;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.RestData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Note extends RestData {

	private String timestamp;
	private String text;

	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

}
