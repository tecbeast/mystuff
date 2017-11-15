package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.games.rest.RestDataCollection;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Authors extends RestDataCollection<Author> {
	
	@XmlElement(name="author")
	@JsonProperty("authors")
	@Override
	public List<Author> getElements() {
		return super.getElements();
	}
	
}
