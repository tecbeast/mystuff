package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.games.rest.RestDataCollection;

@XmlRootElement
public class Notes extends RestDataCollection<Note> {
	
	@XmlElement(name="note")
	public List<Note> getElements() {
		return super.getElements();
	}

}
