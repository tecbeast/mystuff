package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.games.rest.RestDataCollection;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Awards extends RestDataCollection<Award> {
	
	@XmlElement(name="award")
	@JsonProperty("awards")
	@Override
	public List<Award> getElements() {
		return super.getElements();
	}
}
