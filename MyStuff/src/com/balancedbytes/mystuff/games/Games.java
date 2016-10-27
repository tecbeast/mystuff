package com.balancedbytes.mystuff.games;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.RestDataCollection;

@XmlRootElement
public class Games extends RestDataCollection<Game> {
	
	@XmlElement(name="game")
	public List<Game> getGames() {
		return getElements();
	}
	
}