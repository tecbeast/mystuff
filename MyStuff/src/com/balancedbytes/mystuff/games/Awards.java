package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.RestDataCollection;

@XmlRootElement
public class Awards extends RestDataCollection<Award> {
	
	@XmlElement(name="award")
	public List<Award> getAwards() {
		return getChildren();
	}

}
