package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.RestDataCollection;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Publishers extends RestDataCollection<Publisher> {
	
	@XmlElement(name="publisher")
	@JsonProperty("publishers")
	@Override
	public List<Publisher> getElements() {
		return super.getElements();
	}

}
