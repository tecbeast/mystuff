package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.RestDataCollection;

@XmlRootElement
public class Authors extends RestDataCollection<Author> {
	
	@XmlElement(name="author")
	@Override
	public List<Author> getElements() {
		return super.getElements();
	}
	
}
