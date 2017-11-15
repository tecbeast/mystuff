package com.balancedbytes.mystuff.games.rest;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public abstract class RestData {

	private String id;
	private Link link;
	
	@XmlAttribute
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
	public Link getLink() {
		return this.link;
	}
	
	public void setLink(Link link) {
		this.link = link;
	}
		
}
