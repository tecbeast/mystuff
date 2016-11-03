package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.RestDataCollection;

@XmlRootElement
public class Images extends RestDataCollection<Image> {
	
	@XmlElement(name="image")
	public List<Image> getImages() {
		return getElements();
	}

}
