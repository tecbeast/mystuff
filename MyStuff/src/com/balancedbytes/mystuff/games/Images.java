package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.games.rest.RestDataCollection;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Images extends RestDataCollection<Image> {
	
	@XmlElement(name="image")
	@JsonProperty("images")
	@Override
	public List<Image> getElements() {
		return super.getElements();
	}

}
