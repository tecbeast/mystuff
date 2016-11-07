package com.balancedbytes.mystuff;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class RestData {

	@XmlAttribute
	private String id;
	@XmlElement(name="link")
	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
	private List<Link> links = new ArrayList<Link>();
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public List<Link> getLinks() {
		return links;
	}
	
	public void addLink(Link link) {
		if (link != null) {
			links.add(link);
		}
	}
		
	public boolean hasLinks() {
		return (links.size() > 0);
	}
	
	public void clearLinks() {
		links.clear();
	}

}
