package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.games.rest.RestDataCollection;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Notes extends RestDataCollection<Note> {
	
	@XmlElement(name="note")
	@JsonProperty("notes")
	@Override
	public List<Note> getElements() {
		return super.getElements();
	}

}
